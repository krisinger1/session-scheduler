package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import model.Parameters;
import model.Session;
import model.Solution;
import model.Student;
import model.StudentDatabase;
import model.TimeSlot;
import model.Tree;

public class Controller {
	StudentDatabase db= new StudentDatabase();
	ArrayList<Solution> solutions, distinctSolutions, variations;
	int maxStudents, minStudents, maxSessions, blockSize;
	//TODO don't explicitly set size of masks
	int[][] preferredMask=new int[3][17];
	int[][] possibleMask;
	boolean changed=true;

	public Controller(){

	}

	public Controller(int[][] allowedMask, int[][] preferredMask){
		possibleMask=allowedMask;
		this.preferredMask=preferredMask;
	}

	public void addStudent(String firstName, String lastName, String email, String area, int[][] schedule){
		Student student = new Student(firstName, lastName, email,area, schedule);
		db.addStudent(student);
		changed=true;
	}

	public void updateStudent(int id, String firstName, String lastName, String email, String area, int[][] schedule){
		Student student = new Student(id, firstName, lastName, email,area, schedule);
		db.updateStudent(student);
		changed=true;
	}

	public List<Student> getStudents(){
		return db.getStudents();
	}

	public void saveToFile(File file) throws IOException{
		db.saveTofile(file);
		changed=false;
	}

	public void loadFromFile(File file) throws IOException{
		db.loadFromFile(file);
		changed=false;
	}

	public void removeStudent(int row) {
		db.removeStudent(row);
		changed=true;
	}

	public boolean databaseChanged(){
		return changed;
	}

	public void runScheduler(int maxStudents, int minStudents, int maxSessions, int blockSize,
			int fewestSessionsWeight,int preferredTimesWeight, int canComeWeight, int mustComeWeight) {
		this.maxStudents=maxStudents;
		this.minStudents=minStudents;
		this.maxSessions=maxSessions;
		this.blockSize=blockSize;
		//System.out.println("controller: running Scheduler");
		ArrayList<Student> students = db.getStudents();
		ArrayList<Student> studentsCopy = new ArrayList<Student>(students);

		solutions=new ArrayList<Solution>();

		if (students.isEmpty()) return;
		for (Student stu:students){
			stu.calculateRank(blockSize);
		}
		//Collections.copy(studentsCopy, students);
		Collections.sort(studentsCopy);

		Tree solutionTree=new Tree(null, new Session(null));
		// initialize mask
		int[][] possibleMask=new int[3][17];
		for (int i=0;i<8;i++) possibleMask[0][i]=1;
		// TODO temporary - where should creation of preferred times mask be?
		for (int i=0;i<8;i++) preferredMask[0][i]=1;
		preferredMask[1][0]=1;
		preferredMask[1][1]=1;
		preferredMask[2][0]=1;
		preferredMask[2][1]=1;

		do {
			Scheduler.createTree(studentsCopy, solutionTree,possibleMask,blockSize,maxStudents);
			// remove impossible schedules if any
			if (!solutionTree.hasLeaves()) {
				JOptionPane.showMessageDialog(null, "Impossible schedule removed. \nThere are no possible solutions containing \nthis schedule for given parameters.\n> "+studentsCopy.get(0).getFullName(),"Impossible Schedule", JOptionPane.WARNING_MESSAGE);
				studentsCopy.remove(0);
			}
		}		while (!solutionTree.hasLeaves());
		if (!(solutionTree==null)) solutionTree.pruneTree();
		createSolutions(solutionTree, new ArrayList<Session>(),studentsCopy);
		//System.out.println("controller: out of createSolutions");
		for (Solution sol:solutions){
			sol.calculateNormalizedRank(fewestSessionsWeight, preferredTimesWeight, canComeWeight, mustComeWeight,solutions);
		}
		Collections.sort(solutions);

		distinctSolutions = new ArrayList<Solution>();

		// find similar solutions
		for (Solution baseSolution:solutions){
			// include the original solution
			baseSolution.addSimilarSolution(baseSolution);
			//System.out.println("controller: base solution: "+ baseSolution);
			for (Solution testSolution:solutions){
				if (!baseSolution.isSame(testSolution)){
					if (baseSolution.isSimilar(testSolution)){
						baseSolution.addSimilarSolution(testSolution);
						//System.out.println("    similar solution: "+testSolution);
					}
				}
			}
		}

		// reduce solutions down to just the ones that are distance from each other - remove similar ones
		ArrayList<Solution> solutionsCopy = (ArrayList<Solution>)solutions.clone();
		int index=0;
		while (solutionsCopy.size()>0){
			Solution sol=solutionsCopy.get(index);
			//for (Solution s:sol.getSimilarSolutions()){
			solutionsCopy.removeAll(sol.getSimilarSolutions());
			solutionsCopy.remove(sol);
			distinctSolutions.add(sol);
			//index++;
			//}
		}
	}

	public void createSolutions(Tree t,ArrayList<Session> sessionList,ArrayList<Student> studentsCopy){
		if (t.isEnd) {
			boolean goodSolution=true;
			Solution solution=new Solution();
			ArrayList<Session> sessionListClone = new ArrayList<Session>();
			//ArrayList<Session> sessionListClone = (ArrayList<Session>) sessionList.clone();
			for (Session session:sessionList){
				sessionListClone.add(new Session(session.timeSlot,session.preferred));
			}
			solution.setSessions(sessionListClone);
			solution.findAllMembers(studentsCopy, blockSize);
			for (Session session :solution.getSessions()){ //check parameters to make sure solution satisfies
				if (session.members.size()<minStudents) goodSolution=false;
				else if (solution.getSessions().size()>maxSessions)goodSolution=false;
			}
			if (goodSolution) solutions.add(solution);
			}
		else for (Tree leaf:t.leaves){
			//System.out.println("createSolutions else "+leaf.isEnd+" "+leaf.isDead);
			TimeSlot timeSlot=leaf.session.timeSlot;
			boolean preferred=true;
			int time=timeSlot.getTime();
			int day = timeSlot.getDay();
			for (int i=0;i<blockSize;i++){
				if (preferredMask[day][time+i]==1) preferred=false;
			}
			Session s=new Session(timeSlot,preferred);
			ArrayList<Session> sessionListCopy=(ArrayList<Session>)sessionList.clone();
			sessionListCopy.add(s);
			createSolutions(leaf, sessionListCopy,studentsCopy);

		}
	}

	public ArrayList<Solution> getSolutions() {
		return distinctSolutions;
	}

	public ArrayList<Solution> getVariations(int index){
		variations = distinctSolutions.get(index).getSimilarSolutions();
		return variations;
	}

	public String getMembers(int index) {
		ArrayList<Session> sessions = variations.get(index).getSessions();
		String list="";
		for (Session s:sessions){
			String day= Parameters.dayNames[s.timeSlot.getDay()];
			String time=Parameters.timeSlotStrings[s.timeSlot.getTime()];
			list+=day+" "+time+"\n";
			list+= s.membersToString();
			//for (Student stu:s.members) list+="\n"+stu;
		}
		return list;
	}

	public String getCsvSingleSolution(int index){
		ArrayList<Session> sessions = variations.get(index).getSessions();
		String result = "";
		for (Session sess:sessions){
			ArrayList<Student> membersCanAttend = sess.members;
			String day= Parameters.dayNames[sess.timeSlot.getDay()];
			String time=Parameters.timeSlotStrings[sess.timeSlot.getTime()];
			result+=day+" "+time+"\n";
			for (Student stu:membersCanAttend){
				if (sess.membersMustAttend.contains(stu)) result+="*";
				result+=stu.getFullName()+","+stu.getEmail()+"\n";
			}
			result+="\n";

		}
		return result;
	}

	public void saveSolutionToFile(File file, int i){
		PrintWriter writer;
		try {
			writer = new PrintWriter(file, "UTF-8");
			writer.println(getCsvSingleSolution(i));
			writer.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File not found. Could not print to file.","Error",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			JOptionPane.showMessageDialog(null, "Unsupported encoding exception. Could not print to file.","Error",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	public void exportToCsv(File file) {
		try {
			db.exportToCsv(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
