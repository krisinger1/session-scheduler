package org.intervarsity;

import java.util.ArrayList;

import javax.swing.text.DefaultEditorKit.CopyAction;

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
		
	public void add(Schedule s){
		members.add(s);
	}
	
	public void addMustAttend(Schedule s){
		membersMustAttend.add(s);
	}
	
		
	public void print(){
		System.out.print(time+":");
		for (Schedule s:members)System.out.println("\t"+s.getName());
		System.out.println("");

	}
	
	public void printMustAttend(){
		System.out.print(time+":");
		for (Schedule s:membersMustAttend)System.out.println("\t"+s.getName());
		System.out.println("");

	}
	
	public String toString(){
		String result=time+":";
		for (Schedule s:members)result+=" "+s.getName();
		result+="\n";
		return result;

	}
	
	public int compareTo(Session s){
		if (time>s.time) return 1;
		else if(time<s.time) return -1;
		else return 0;
	}
}
