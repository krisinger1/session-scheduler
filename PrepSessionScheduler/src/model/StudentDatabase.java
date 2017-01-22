package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class StudentDatabase {
	private List<Student> students;

	public StudentDatabase(){
		students = new LinkedList<Student>();
	}

	public void addStudent(Student student){
		System.out.println(students.toString());
		students.add(student);
		System.out.println("database addStudent -->student: "+student.getSchedule()[0][0]+" "+student.getSchedule()[0][1]);
		System.out.println(students.toString());

	}
	
	public Student getStudent(int index){
		return students.get(index);
	}

	public void removeStudent(int index) {
		students.remove(index);
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
				System.out.println("database updateStudent -->students: "+students.get(1).getId()+" "+students.get(1).getSchedule()[0][0]+" "+students.get(1).getSchedule());
				System.out.println(students.toString());
			}
		}
	}


	public List<Student> getStudents(){
		return Collections.unmodifiableList(students); //prevent other classes from being able to modify the data
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

	public void saveTofile(File file) throws IOException{
		Student[] arrayStudents = students.toArray(new Student[students.size()]);

		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(arrayStudents);
		oos.close();
	}

	public void loadFromFile(File file) throws IOException{
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);
		try {
			Student[] arrayStudents = (Student[])ois.readObject();
			students.clear();
			students.addAll(Arrays.asList(arrayStudents));
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
	
}
