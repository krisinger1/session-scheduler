package org.intervarsity;

import java.util.ArrayList;

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
	ArrayList<Schedule> members;
	ArrayList<Schedule> membersMustAttend;
	int time; //the index of the session on schedule
	boolean preferred;
	
	public Session(int timeSlot){
		members=new ArrayList<Schedule>();
		membersMustAttend=new ArrayList<Schedule>();
		time=timeSlot;
		preferred=false;
	}
	
	public Session(int timeSlot,boolean preferred){
		members=new ArrayList<Schedule>();
		membersMustAttend=new ArrayList<Schedule>();
		time=timeSlot;
		this.preferred=preferred;
	}
	
	/**
	 * Adds a student's schedule to the list of members for this session
	 * @param s the schedule to be added
	 */
	public void add(Schedule s){
		members.add(s);
	}
	
	/**
	 * Adds a schedule of a student who can only attend this session and no other
	 * @param s the schedule to be added
	 */
	public void addMustAttend(Schedule s){
		membersMustAttend.add(s);
	}
	
	/**
	 * Prints to console names of all students who can attend this session	
	 */
	public void print(){
		System.out.print(time+":");
		for (Schedule s:members)System.out.println("\t"+s.getName()+"\t"+s.getEmail());
		System.out.println("");

	}
	
	/**
	 * Prints to console names of all students who must attend this session
	 */
	public void printMustAttend(){
		System.out.print(time+":");
		for (Schedule s:membersMustAttend)System.out.println("\t"+s.getName());
		System.out.println("");

	}
	
	/**
	 * Returns a string with time and names of all students who can attend this session
	 */
	public String toString(){
		String result=time+":";
		for (Schedule s:members)result+=" "+s.getName()+" "+s.getEmail();
		result+="\n";
		return result;

	}
	
	/**
	 * Compares two sessions by their times
	 * @param s session to compare to this session
	 * @return 1 if this session is later than, 
	 *  -1 if earlier, otherwise 0. 
	 */
	public int compareTo(Session s){
		if (time>s.time) return 1;
		else if(time<s.time) return -1;
		else return 0;
	}
}
