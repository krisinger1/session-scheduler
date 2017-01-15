package model;

import java.io.Serializable;

public class Student implements Serializable, Comparable<Student>{
	static private int count=0;
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	//private int[] schedule;
	StudentSchedule schedule;

	public Student(String firstName, String lastName, String email){
		id = count;
		this.firstName=firstName;
		this.lastName=lastName;
		this.email=email;
		//schedule=new int[50];
		count++;
	}

	public void setSchedule(StudentSchedule schedule){
		this.schedule=schedule;
	}

	public StudentSchedule getSchedule() throws NullPointerException{
		if (schedule!= null) return schedule;
		else throw new NullPointerException();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFullName() {
		return firstName+" "+lastName;
	}

	public void setName(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName =  lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String toString(){
		return (getFullName()+" "+email);
	}

	@Override
	public int compareTo(Student stu) {
		int lastNameCompare = this.lastName.compareTo(stu.lastName);
		if (lastNameCompare!=0) return lastNameCompare;
		else return this.firstName.compareTo(stu.firstName);
				
	}

//	public int[] getSchedule() {
//		return schedule;
//	}
//
//	public void setSchedule(int[] schedule) {
//		this.schedule = schedule;
//	}
}
