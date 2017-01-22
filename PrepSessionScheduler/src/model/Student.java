package model;

import java.io.Serializable;

public class Student implements Serializable, Comparable<Student>{
	static private int count=0;
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	//private int[] schedule;
	//StudentSchedule schedule;
	int[][] schedule;

	public Student(String firstName, String lastName, String email, int[][] schedule){
		id = count;
		this.firstName=firstName;
		this.lastName=lastName;
		this.email=email;
		this.schedule=schedule;
//		for (int i=0; i<3;i++){
//			for (int j=0; j<17;j++){
//				schedule[i][j]=0;
//			}
//		}
		//schedule=new int[50];
		count++;
	}

	public Student(int id,String firstName, String lastName, String email, int[][] schedule){
		this.id = id;
		this.firstName=firstName;
		this.lastName=lastName;
		this.email=email;
		this.schedule=schedule;
	}

//	public void setSchedule(StudentSchedule schedule){
//		this.schedule=schedule;
//	}
//
//	public StudentSchedule getSchedule() throws NullPointerException{
//		if (schedule!= null) return schedule;
//		else throw new NullPointerException();
//	}

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
		return (getFullName()+" "+email+" "+schedule[0][0]);
	}

	@Override
	public int compareTo(Student stu) {
		int lastNameCompare = this.lastName.compareTo(stu.lastName);
		if (lastNameCompare!=0) return lastNameCompare;
		else return this.firstName.compareTo(stu.firstName);

	}

	public String getFName() {
		// TODO Auto-generated method stub
		return firstName;
	}

	public String getLName(){
		return lastName;
	}

	public int[][] getSchedule(){
		return schedule;
	}

//	public int[] getSchedule() {
//		return schedule;
//	}
//
//	public void setSchedule(int[] schedule) {
//		this.schedule = schedule;
//	}
}
