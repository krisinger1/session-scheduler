package org.intervarsity;

import java.util.ArrayList;

public abstract class Schedule implements Comparable<Schedule>{
	private int rank;
	private String name;
	protected int[] schedule;
	
	abstract void fillSchedule();
	abstract int findOpenBlock(int startIndex,int numSlots);
	abstract void printSchedule();
	abstract void determineRank(int blockSize);
	abstract boolean isBlockOpen(int index, int blockSize);
	
	public Schedule(String name, int size) {
		this.name=name;
		rank=-1;
		schedule = new int[size];
	}
	
	public String toString(){
		return name;
	}
	
	public int[] getSchedule(){
		return schedule;
	}
	
	public void setSchedule(int[] schedule){
		this.schedule=schedule;
	}
	
	public int getRank(){
		return rank;
	}
	
	public void setRank(int rank){
		this.rank=rank;
	}
	
	public String getName(){
		return name;
	}
	
	public int compareTo(Schedule s){
		if (rank>s.getRank()) return 1;
		else if (rank<s.getRank()) return -1;
		else return 0;
	}
	
	

}
