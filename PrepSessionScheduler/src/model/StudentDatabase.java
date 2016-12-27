package model;

import java.util.List;
import java.util.ArrayList;

public class StudentDatabase {
	private ArrayList<Student> students;

	public StudentDatabase(){
		students = new ArrayList<Student>();
	}

	public void addStudent(Student student){
		students.add(student);
	}

	public List<Student> getStudents(){
		return students;
	}
}
