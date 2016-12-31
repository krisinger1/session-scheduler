package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ois.close();
	}
}
