package org.intervarsity;

import java.util.ArrayList;

public abstract class GeneralSchedule implements Comparable<GeneralSchedule>{
	int rank;
	String name;
	ArrayList<Object> schedule;
	
	abstract void fillSchedule();
	abstract int findOpenBlock(int startIndex);
	abstract void printSchedule();
	abstract void determineRank();
	abstract boolean isBlockOpen(int index);
	
	public GeneralSchedule(String name) {
		this.name=name;
	}
	
	public String toString(){
		return name;
	}
	
	public int getRank(){
		return rank;
	}
	
	public int compareTo(GeneralSchedule s){
		if (rank>s.rank) return 1;
		else if (rank<s.rank) return -1;
		else return 0;
	}
	
	

}
