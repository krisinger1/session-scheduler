package controller;

import model.Student;
import model.StudentDatabase;

public class Controller {
	StudentDatabase db= new StudentDatabase();

	public void addStudent(String name, String email){
		Student student = new Student(name, email);
		db.addStudent(student);
		System.out.println("controller: "+student);
	}
}
