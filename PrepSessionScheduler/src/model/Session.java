package model;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents one session of a Bible prep.
 * includes time of the session.
 * and whether that is a preferred time for the staff running it.
 * has list of students' schedules who can attend at that time.
 * and a list of students' schedules who _must_ attend this particular session.
 * @author Kristin
 *
 */
public class Session implements Comparable<Session>{
	public List<Student> members;
	public List<Student> membersMustAttend;
	//int time; //the index of the session on schedule
	public TimeSlot timeSlot;
	public boolean preferred;

	public Session(TimeSlot timeSlot){
		members= new ArrayList<Student>();
		membersMustAttend=new ArrayList<Student>();
		this.timeSlot=timeSlot;
		preferred=false;
	}

	public Session(TimeSlot timeSlot,boolean preferred){
		members=new ArrayList<Student>();
		membersMustAttend=new ArrayList<Student>();
		this.timeSlot=timeSlot;
		this.preferred=preferred;
	}

	/**
	 * Adds a student's schedule to the list of members for this session
	 * @param s the schedule to be added
	 */
	public void add(Student stu){
		members.add(stu);
	}

	/**
	 * Adds a schedule of a student who can only attend this session and no other
	 * @param s the schedule to be added
	 */
	public void addMustAttend(Student stu){
		membersMustAttend.add(stu);
	}

	/**
	 * Prints to console names of all students who can attend this session
	 */
	public void print(){
		System.out.print(timeSlot+" can attend:");
		for (Student s:members)System.out.println("\t"+s.getFullName()+"\t"+s.getEmail());
		System.out.println("");

	}

	/**
	 * Prints to console names of all students who must attend this session
	 */
	public void printMustAttend(){
		System.out.print(timeSlot+" must attend:");
		for (Student s:membersMustAttend)System.out.println("\t"+s.getFullName());
		System.out.println("");

	}

	/**
	 * Returns a string with names of all students who can attend this session
	 * Students who must attend are marked with *
	 */
	public String toString(){
//		String result=timeSlot.toString();
//		for (Student s:members){
//			if (membersMustAttend.contains(s)) result+="*";
//			result+=" "+s.getFullName()+"\t\t "+s.getEmail()+"\n";
//		}
//		result+="\n";
//		return result;

		return timeSlot.toString();
	}

	/**
	 * Compares two sessions by their times
	 * @param s session to compare to this session
	 * @return 1 if this session is later than,
	 *  -1 if earlier, otherwise 0.
	 */
	public int compareTo(Session s){
		return timeSlot.compareTo(s.timeSlot);
	}
}
