package gui;

import java.util.EventObject;

public class StudentDataEvent extends EventObject{
	int id;
	String FName;
	String LName;
	String email;
	String area;

	int [][] schedule;

	public StudentDataEvent(Object source,int id, String FName, String LName, String email, String area, int[][] schedule) {
		super(source);
		this.id=id;
		this.FName=FName;
		this.LName= LName;
		this.email=email;
		this.area=area;
		this.schedule=schedule;
		System.out.println("StudentDataEvent: "+schedule[0][0]+" "+schedule[0][1]);

	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public int getId(){
		return this.id;
	}

	public String getLName() {
		return LName;
	}

	public void setName(String FName, String LName) {
		this.FName = FName;
		this.LName=LName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFName() {
		return FName;
	}

	public int[][] getSchedule(){
		return schedule;
	}

	public void setSchedule(int[][] schedule){
		this.schedule=schedule;
	}


}
