package gui;

import java.util.EventObject;

public class StudentDataEvent extends EventObject{
	String name;
	String email;

	public StudentDataEvent(Object source,String name, String email) {
		super(source);
		this.name=name;
		this.email=email;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


}
