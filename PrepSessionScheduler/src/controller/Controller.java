package controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import model.Student;
import model.StudentDatabase;

public class Controller {
	StudentDatabase db= new StudentDatabase();

	public void addStudent(String firstName, String lastName, String email, int[][] schedule){
		Student student = new Student(firstName, lastName, email,schedule);
		System.out.println("controller: "+student);
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

}
