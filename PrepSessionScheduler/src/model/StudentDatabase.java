package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import gui.MainFrame;
import gui.Utils;


public class StudentDatabase {
	private ArrayList<Student> students;


	public StudentDatabase(){
		students = new ArrayList<Student>();
	}

	public void addStudent(Student student){
		boolean duplicate = false;
		int index=-1;
		for (Student s:students){
			if (s.isDuplicate(student)) {
				duplicate=true;
				index=students.indexOf(s);
			}
		}
		if (duplicate){
			int choice = JOptionPane.showConfirmDialog(null, student.getFullName()+" already in database. Overwrite?");
			if (choice==JOptionPane.NO_OPTION || choice==JOptionPane.CANCEL_OPTION) return;
			if (choice==JOptionPane.YES_OPTION){
				students.set(index, student);
				System.out.println("student data overwritten");
			}
		}
		else{
			student.setId(maxID()+1);
			System.out.println(students.toString());
			students.add(student);
			System.out.println("database addStudent -->student: "+student.getSchedule()[0][0]+" "+student.getSchedule()[0][1]);
			System.out.println(students.toString());
		}
	}

	private int maxID(){
		int max=0;
		for (Student student:students){
			if (student.getId()>max) max = student.getId();
		}
		return max;
	}

	public Student getStudent(int index){
		return students.get(index);
	}

	public void removeStudent(int index) {
		students.remove(index);
	}

	public void clear(){
		students.clear();
	}

	public void updateStudent(Student student){
		int id = student.getId();
		for (Student s:students){
			if (s.getId()==student.getId()) {
				System.out.println("updating student");
				System.out.println("database updateStudent -->s: "+s.getId()+" "+s.getSchedule()[0][0]+" "+s.getSchedule()[0][1]);
				System.out.println("database updateStudent -->student: "+id+" "+student.getSchedule()[0][0]+" "+student.getSchedule()[0][1]);
				int index=students.indexOf(s);
				students.set(index, student);
				System.out.println("database updateStudent -->students: "+students.get(0).getId()+" "+students.get(0).getSchedule()[0][0]+" "+students.get(0).getSchedule());
				//System.out.println("database updateStudent -->students: "+students.get(1).getId()+" "+students.get(1).getSchedule()[0][0]+" "+students.get(1).getSchedule());
				System.out.println(students.toString());
			}
		}
	}


	public ArrayList<Student> getStudents(){
		//return Collections.unmodifiableList(students); //prevent other classes from being able to modify the data
		//List<Student> studentsCopy = new LinkedList<Student>(students);
//		Collections.copy(studentsCopy, students);
//		if (!studentsCopy.isEmpty())System.out.println("get studentsCopy: "+studentsCopy.get(0).toString());
//		return studentsCopy;
		return students;
	}

//////////////////// for actual database connection - learn later ////////////////////
//	public void connect() throws Exception{
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//		} catch (ClassNotFoundException e) {
//			throw new Exception("Driver not found");
//		}
//
//		String url = "jdbc:mysql://localhost:3306/SGLschedules";
//		Connection connection = DriverManager.getConnection(url, "root", "password");
//	}
//
//	public void disconnect(){
//
//	}

	/**
	 *
	 * @param file
	 * @throws IOException
	 */

	public void saveTofile(File file) throws IOException{
		Student[] arrayStudents = students.toArray(new Student[students.size()]);

		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(Parameters.version);
		oos.writeObject(arrayStudents);
		oos.close();
	}

	public void exportToExcel(File file) throws IOException{
		// export file of students and schedules to excel file
		//Create Blank workbook
	    XSSFWorkbook workbook = new XSSFWorkbook();
	    //Create a blank spreadsheet
	    XSSFSheet spreadsheet = workbook.createSheet(" Students ");
	    //Create row object
	    XSSFRow row;

	    int i=0;
		row=spreadsheet.createRow(i);
		for (int day=0;day<Parameters.dayNames.length;day++){
	    	for (int time=0;time<Parameters.timeSlotStrings.length;time++){
	    		if (time==0){
	        		row.createCell(day*Parameters.timeSlotStrings.length+time+3).setCellValue(Parameters.dayNames[day]);
	    		}
	    		else {
	    			row.createCell(day*Parameters.timeSlotStrings.length+time+3);
	    		}
	    	}
	    }
		i++;
		row=spreadsheet.createRow(i);
	    for (int day=0;day<Parameters.dayNames.length;day++){
	    	for (int time=0;time<Parameters.timeSlotStrings.length;time++){
	    		row.createCell(day*Parameters.timeSlotStrings.length+time+3).setCellValue(Parameters.timeSlotStrings[time]);
	    	}
	    }
	    i++;
	    for (Student stu:students){
	    	row=spreadsheet.createRow(i);
	    	row.createCell(0).setCellValue(stu.getFullName());
	    	row.createCell(1).setCellValue(stu.getArea());
	    	row.createCell(2).setCellValue(stu.getEmail());
	    	int[][] sched=stu.getSchedule();
	    	for (int day=0;day<sched.length;day++){
	    		for (int time=0;time<sched[0].length;time++){
	    			row.createCell(day*sched[0].length+time+3).setCellValue(sched[day][time]);
	    		}
	    	}
	    	i++;
	    }

	    //Create file system using specific name
	    FileOutputStream out = new FileOutputStream(file);
	    //write operation workbook using file out object
	    workbook.write(out);
	    out.close();
	    System.out.println(file.getName()+"written successfully");
	    workbook.close();
	}

	public void exportToCsv(File file) throws IOException{
		PrintWriter writer;
		String text="";
		try {
			writer = new PrintWriter(file, "UTF-8");
			for (Student stu:students){
				text=stu.getFullName()+","+stu.getArea()+","+stu.getEmail()+","+getScheduleAsCsv(stu)+"\n";
				System.out.println(text);
				writer.append(text);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File not found. Could not print to file.","Error",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			JOptionPane.showMessageDialog(null, "Unsupported encoding exception. Could not print to file.","Error",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	public void loadFromFile(File file) throws IOException{
		//FIXME how to deal with older version student objects so can load without error?
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);
		try {
			//TODO check for type of object in load file
			String version = (String)ois.readObject();
			System.out.println("version "+version);

			if (version.equals(Parameters.version)){
				Student[] arrayStudents = (Student[])ois.readObject();
				students.clear();
				students.addAll(Arrays.asList(arrayStudents));
			}

			else{
				//TODO either joptionpane about failure to load or convert older file
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		ois.close();
	}

	public String toString(){
		String data ="";
		for (Student s:students) data+=s.toString();
		return data;
	}

	public String getScheduleAsCsv(Student stu){
		String text="";
		for (int i=0;i<stu.schedule.length;i++){
			text+="2,";
			for (int j=0;j<stu.schedule[0].length;j++){
				text+=stu.schedule[i][j]+",";
			}

		}
		return text;
	}

	public int getNumStudents() {
		return students.size();
	}

}
