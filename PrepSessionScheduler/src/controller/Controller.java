package controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import model.Student;
import model.StudentDatabase;

public class Controller {
	StudentDatabase db= new StudentDatabase();

	public void addStudent(String name, String email){
		Student student = new Student(name, email);
		db.addStudent(student);
		System.out.println("controller: "+student);
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
}
