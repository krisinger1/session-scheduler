package model;

import java.io.Serializable;

public class Student implements Serializable, Comparable<Student>{
	static private int count=0;
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String area;
	//private int[] schedule;
	//StudentSchedule schedule;
	int[][] schedule;
	private int rank=-1;

	public Student(String firstName, String lastName, String email, String area, int[][] schedule){
//		id = count;
		this.firstName=firstName;
		this.lastName=lastName;
		this.email=email;
		this.area=area;
		this.schedule=schedule;
		//count++;
	}

	public Student(int id,String firstName, String lastName, String email, String area, int[][] schedule){
		this.id = id;
		this.firstName=firstName;
		this.lastName=lastName;
		this.email=email;
		this.area=area;
		this.schedule=schedule;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
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

	public void setRank(int rank){
		if (this.rank==-1) this.rank=rank;
	}

	public void calculateRank(int blockSize){
		rank=numOpenBlocks(blockSize);
	}

	public int getRank(){
		return rank;
	}

	public String toString(){
		return (getFullName()+" "+email);
		//return getFullName();
	}

	@Override
	public int compareTo(Student stu) {
//		int lastNameCompare = this.lastName.compareTo(stu.lastName);
//		if (lastNameCompare!=0) return lastNameCompare;
//		else return this.firstName.compareTo(stu.firstName);
		if (rank>stu.getRank()) return 1;
		else if (rank<stu.getRank()) return -1;
		else return 0;

	}

	public String getFName() {
		return firstName;
	}

	public String getLName(){
		return lastName;
	}

	public int[][] getSchedule(){
		return schedule;
	}

	public boolean hasBlockOpen(TimeSlot timeSlot, int blockSize) {
		int sum=0;
		int day = timeSlot.getDay();
		int index=timeSlot.getTime();
		if ((index+blockSize)>schedule[day].length)return false;
		for (int i=index;i<index+blockSize;i++){
			sum+=schedule[day][i];
		}
		if (sum==0) return true;
		else return false;
	}

	/**
	 * find the first open block of size blocksize in the schedule
	 * @param blockSize the number of consecutive open slots in schedule needed (0's)
	 * @param startindex index to start searching from
	 */
	public TimeSlot findOpenBlock(TimeSlot startTimeSlot, int blockSize ){
		int startTime = startTimeSlot.getTime();
		int startDay = startTimeSlot.getDay();
		for (int day=startDay;day<schedule.length;day++){
			if (day>startDay) startTime=0;
			for (int time=startTime; time<schedule[day].length;time++){
				TimeSlot testSlot = new TimeSlot(day, time);
				if (schedule[day][time]==0 && hasBlockOpen(testSlot, blockSize)) return testSlot;
			}
		}
		return null;
	}

	public int numOpenBlocks(int blockSize){
		TimeSlot t;
		int count=0;
		for (int day=0;day<schedule.length;day++){
			for (int time=0; time<schedule[day].length;time++){
				t = new TimeSlot(day, time);
				if (schedule[day][time]==0 && hasBlockOpen(t, blockSize)) count++;
			}
		}
		return count;
	}

	public boolean isDuplicate(Student stu){

		//FIXME remove checking for same area
		if (this.firstName.equals(stu.firstName) &&
				this.lastName.equals(stu.lastName) &&
				this.email.equals(stu.email) &&
				this.area.equals(stu.area)){
			return true;
		}
		else return false;
	}


}
