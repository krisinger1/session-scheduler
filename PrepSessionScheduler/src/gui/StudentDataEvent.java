package gui;

import java.util.EventObject;

public class StudentDataEvent extends EventObject{
	String FName;
	String LName;
	String email;

	public StudentDataEvent(Object source,String FName, String LName, String email) {
		super(source);
		this.FName=FName;
		this.LName= LName;
		this.email=email;

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


}
