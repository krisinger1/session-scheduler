package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import model.Session;
import model.Solution;
import model.Student;
import model.StudentDatabase;
import model.TimeSlot;
import model.Tree;

public class Controller {
	StudentDatabase db= new StudentDatabase();
	ArrayList<Solution> solutions;
	int maxStudents, minStudents, maxSessions, blockSize;

	public void addStudent(String firstName, String lastName, String email, int[][] schedule){
		Student student = new Student(firstName, lastName, email,schedule);
		System.out.println("controller addStudent: "+student);
		//System.out.println("controller: "+db.getStudents().get(0));
		System.out.println("controller db: "+db.toString());
		db.addStudent(student);
		System.out.println("controller db after: "+db.toString());
	}

	public void updateStudent(int id, String firstName, String lastName, String email, int[][] schedule){
		Student student = new Student(id, firstName, lastName, email,schedule);
		System.out.println("controller updateStudent: "+schedule[0][0]+" "+schedule[0][1]);
		db.updateStudent(student);
	}

	public List<Student> getStudents(){
		return db.getStudents();
	}

	public void saveToFile(File file) throws IOException{
		db.saveTofile(file);
	}

	public void loadFromFile(File file) throws IOException{
		db.loadFromFile(file);
	}

	public void removeStudent(int row) {
		db.removeStudent(row);
	}

	public ArrayList<String[]> getVariations(int row) {
		System.out.println("getting variationsof solution");
		ArrayList<String[]> arrayList = new ArrayList<String[]>();
		return arrayList;
	}

	public void runScheduler(int maxStudents, int minStudents, int maxSessions, int blockSize) {
		this.maxStudents=maxStudents;
		this.minStudents=minStudents;
		this.maxSessions=maxSessions;
		this.blockSize=blockSize;
		System.out.println("running Scheduler");
		ArrayList<Student> students = db.getStudents();
		ArrayList<Student> studentsCopy = new ArrayList<Student>(students);
		
		solutions=new ArrayList<Solution>();

		if (students.isEmpty()) return;
		for (Student stu:students){
			stu.calculateRank(blockSize);
		}
		//Collections.copy(studentsCopy, students);
		Collections.sort(students);

		Tree solutionTree=new Tree(null, new Session(null));
		int[][] possibleMask=new int[3][17];
		do {
			Scheduler.createTree(studentsCopy, solutionTree,possibleMask,blockSize,maxStudents);
			if (!solutionTree.hasLeaves()) {
				JOptionPane.showMessageDialog(null, "Impossible schedule removed. \nThere are no possible solutions containing \nthis schedule for given parameters.\n> "+studentsCopy.get(0).getFullName(),"Impossible Schedule", JOptionPane.WARNING_MESSAGE);
				//textSolutionOutput.append("Impossible schedule, "+schedulesCopy.get(0).getName()+" removed");
				studentsCopy.remove(0);
			}
		}		while (!solutionTree.hasLeaves());
		if (!(solutionTree==null)) solutionTree.pruneTree();
		createSolutions(solutionTree, new ArrayList<Session>(),studentsCopy);
		System.out.println("out of createSolutions");
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
			//TODO add this back in later
//			for (Session session :solution.getSessions()){ //check parameters to make sure solution satisfies
//				if (session.members.size()<minStudents)goodSolution=false;
//				else if (solution.getSessions().size()>maxSessions)goodSolution=false;
//			}
			if (goodSolution) solutions.add(solution);
			}
		else for (Tree leaf:t.leaves){
			//System.out.println("createSolutions else "+leaf.isEnd+" "+leaf.isDead);
			TimeSlot time=leaf.session.timeSlot;
			boolean preferred=true;
			//TODO add this back in later
//			for (int i=0;i<blockSize;i++){
//				if (preferredMask[time+i]==1) preferred=false;
//			}
			Session s=new Session(time,preferred);
			ArrayList<Session> sessionListCopy=(ArrayList<Session>)sessionList.clone();
			sessionListCopy.add(s);
			createSolutions(leaf, sessionListCopy,studentsCopy);

		}
	}

	public ArrayList<Solution> getSolutions() {
		return solutions;
	}


}
