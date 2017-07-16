package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.jar.Attributes.Name;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import model.Parameters;
import model.Session;
import model.Solution;
import model.Student;
import model.StudentDatabase;
import model.TimeSlot;
import model.Tree;
import unused.StudentSchedule;

public class Controller {
	StudentDatabase db= new StudentDatabase();
	ArrayList<Solution> solutions, distinctSolutions, variations;
	int maxStudents, minStudents, maxSessions, blockSize;
	int[][] preferredTimesMask;
	int[][] allowedTimesMask;
	boolean changed=false;

	public Controller(){

	}

//	public Controller(int[][] allowedMask, int[][] preferredMask){
//		possibleMask=allowedMask;
//		//this.preferredMask=preferredMask;
//	}

	public void addStudent(String firstName, String lastName, String email, String area, int[][] schedule){
		Student student = new Student(firstName, lastName, email,area, schedule);
		db.addStudent(student);
		changed=true;
	}

	public void addStudent(Student student){
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
	public int getNumStudents(){
		return db.getNumStudents();
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

	public void clearDatabase(){
		db.clear();

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
		//Parameters.blockSize=3;
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
		//TODO can import file with mask &/or preferred times
		//TODO how to save mask with database?

		do {
			//Scheduler.createTree(studentsCopy, solutionTree,Parameters.allowedTimesMask,blockSize,maxStudents);

			Scheduler.createTree(studentsCopy, solutionTree,allowedTimesMask,blockSize,maxStudents);
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
				//if (Parameters.preferredTimesMask[day][time+i]==1) preferred=false;
				if (preferredTimesMask[day][time+i]==1) preferred=false;

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

	public ArrayList<Student> importSchedule(File[] files) throws IOException{
		ArrayList<Student> students=new ArrayList<Student>();
		System.out.println("importing schedule");
		for (File file:files){

			FileInputStream fis = new FileInputStream(file);
	        XSSFWorkbook workbookImport = new XSSFWorkbook(fis);
	        XSSFSheet spreadsheetImport = workbookImport.getSheetAt(0);
	        XSSFRow row;
	        ArrayList<Object> data= new ArrayList<Object>();
	        Iterator < Row > rowIterator = spreadsheetImport.iterator();

	        Map<Integer,Object[]> info = new TreeMap<Integer,Object[]>();
	        int cellid=0;

	        while (rowIterator.hasNext())
	        {
		       	 row = (XSSFRow) rowIterator.next();
		       	 Iterator < Cell > cellIterator = row.cellIterator();
		       	 while ( cellIterator.hasNext())
		       	 {
		       		 Cell cell = cellIterator.next();
		       		 switch (cell.getCellType())
		       		 {
		       		 case Cell.CELL_TYPE_NUMERIC:
		       			 data.add((int)cell.getNumericCellValue());
		       			 System.out.println((int)cell.getNumericCellValue());
		       			 break;
		       		 case Cell.CELL_TYPE_STRING:
		       			 data.add(cell.getStringCellValue());
		       			 System.out.println(cell.getStringCellValue());
		       			 break;
		       		 }
		       	 }
		       	 //System.out.print(data.get(i));
		       	 //check for data existing before adding to info
		       	 if (data.toArray().length!=0){
		       		 info.put(cellid,data.toArray());
			       	 System.out.println(info.get(cellid).length);

		       	 }
		       	 System.out.println(cellid+"   "+data.toArray().length);

		       	 data.clear();
		       	 cellid++;
	        }
	        fis.close();
			 workbookImport.close();
			 // check for error like array size = 0 and skip that file, but do rest of them
			// check file is in correct format
			//TODO find keys for each piece of data instead of hardcoding in
			 boolean fileOK =true;
			 int firstKey=0;
			 int lastKey=1;
			 int areaKey=2;
			 int emailKey=3;
			 int schedStartKey=6;
			 String errorString="";
			 for (int i=schedStartKey;i<info.size();i++){
				 //System.out.println(info.get(i)[0].toString());
				 if (!info.get(i)[0].toString().contains(":")) {
					 fileOK=false;
					 System.out.println(fileOK);
					 errorString+="Problem with time labels.\n";
				 }
			 }
			 System.out.println(info.get(0)[0].toString().toLowerCase().equals("first name"));
			 if (!(info.get(firstKey)[0].toString().toLowerCase().equals("first name")
					 && info.get(lastKey)[0].toString().toLowerCase().equals("last name")
					 && info.get(areaKey)[0].toString().toLowerCase().equals("area")
					 && info.get(emailKey)[0].toString().toLowerCase().equals("email"))){
				 errorString+="Problem with name, email, or address labels.\n";
				 fileOK=false;
			 }
			 System.out.println(fileOK);
			 if (info.get(firstKey).length==1
					 || info.get(lastKey).length==1
					 || info.get(emailKey).length==1){
				 errorString+="Extra columns in name or email cells\n";
				 fileOK=false;
				 System.out.println("BAD FILE: info(firstKey).length="+info.get(firstKey).length+ " info(lastKey).length="
					 +info.get(lastKey).length+" info(emailKey).length="
					 +info.get(emailKey).length);
			 }
			 System.out.println("fileOK? "+fileOK);

			 //FIXME move option pane from controller to mainframe
			 if (!fileOK) {
				 JOptionPane.showMessageDialog(null,
						 "Problem with file '"+file.getName()+"'. Skipping file import.\nErrors:\n"+errorString,
						 "Error Loading File", JOptionPane.ERROR_MESSAGE);
				 continue;
			 }

			//create student object
			 int[][] schedule = new int[Parameters.numDays][Parameters.numSlots];
			 for (int key=schedStartKey;key<info.size();key++){
				 schedule[0][key-schedStartKey]=(int)info.get(key)[1];
				 schedule[1][key-schedStartKey]=(int)info.get(key)[2];
				 schedule[2][key-schedStartKey]=(int)info.get(key)[3];
				 }
			 System.out.println(schedule.toString());
			 Student student = new Student(info.get(firstKey)[1].toString(),
					 info.get(lastKey)[1].toString(),
					 info.get(emailKey)[1].toString(),
					 info.get(areaKey)[1].toString(),
					 schedule);
			 students.add(student);
		}
		 return students;
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

	public void saveSolutionToExcel(File file,int i) throws IOException{
		//Create Blank workbook
	    XSSFWorkbook workbook = new XSSFWorkbook();
	    //Create a blank spreadsheet
	    XSSFSheet spreadsheet = workbook.createSheet(" Solution ");
	    //Create row object
	    XSSFRow row;

	    XSSFFont boldFont = workbook.createFont();
	    boldFont.setBold(true);
	    boldFont.setColor(HSSFColor.DARK_BLUE.index);
	    XSSFCellStyle boldStyle = workbook.createCellStyle();
	    boldStyle.setFont(boldFont);

	    XSSFFont font = workbook.createFont();
	    font.setBold(false);
	    font.setColor(HSSFColor.BLACK.index);
	    XSSFCellStyle style = workbook.createCellStyle();
	    style.setFont(font);

	    Solution solution = variations.get(i);
	    ArrayList<Session> sessions = solution.getSessions();
	    ArrayList<Student> students;

	    String sessionTime = "";
	    String studentNameString = "";
	    int rowIndex=0;
	    int columnIndex;
	    Cell cell;

	    spreadsheet.setColumnWidth(0, 6000);
	    spreadsheet.setColumnWidth(1, 6000);

		for (Session sess:sessions){
			columnIndex=0;

		    row = spreadsheet.createRow(rowIndex);
			cell =row.createCell(0);
			cell.setCellStyle(boldStyle);

			students = sess.members;
			String day= Parameters.dayNames[sess.timeSlot.getDay()];
			String time=Parameters.timeSlotStrings[sess.timeSlot.getTime()];
			sessionTime=day+" "+time;

			cell.setCellValue(sessionTime);

			rowIndex++;

			for (Student stu:students){
				boolean mustAttend=false;
				row = spreadsheet.createRow(rowIndex++);

				studentNameString = "";
				if (sess.membersMustAttend.contains(stu)) {
					studentNameString+="*";
					mustAttend=true;
				}
				else {
					//cell.setCellStyle(style);
					mustAttend=false;
				}
				studentNameString+=stu.getFullName();

				cell =row.createCell(0);
				if (mustAttend){
					font.setColor(HSSFColor.RED.index);
					cell.setCellStyle(style);
					}
				cell.setCellValue(studentNameString);

				cell = row.createCell(1);
				cell.setCellValue(stu.getEmail());

			}
			rowIndex++;


		}

	    FileOutputStream out = new FileOutputStream(file);
	    //write operation workbook using file out object
	    workbook.write(out);
	    out.close();
	    System.out.println(file.getName()+"written successfully");
	    workbook.close();
	}

	public void exportToCsv(File file) {
		try {
			db.exportToCsv(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void exportToExcel(File file){
		try {
			db.exportToExcel(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setAllowedTimesMask(int[][] mask){
		allowedTimesMask=mask;
	}

	public void setPreferredTimesMask(int[][] mask){
		preferredTimesMask=mask;
	}

	public int[][] getAllowedTimesMask(){
		return allowedTimesMask;
	}

	public int[][] getPreferredTimesMask(){
		return preferredTimesMask;
	}


}
