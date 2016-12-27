package model;

public class Student {
	static private int count=0;
	private int id;
	private String name;
	private String email;
	//private int[] schedule;

	public Student(String name, String email){
		id = count;
		this.name=name;
		this.email=email;
		//schedule=new int[50];
		count++;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	
	public String toString(){
		return (name+" "+email);
	}

//	public int[] getSchedule() {
//		return schedule;
//	}
//
//	public void setSchedule(int[] schedule) {
//		this.schedule = schedule;
//	}
}
