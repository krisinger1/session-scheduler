package org.intervarsity;

import java.util.Random;

public class SimpleSchedule2 implements Comparable<SimpleSchedule2>{
	boolean[] schedule;
	int rank;String name;
	
	
	public SimpleSchedule2(String scheduleName){
		schedule=new boolean[12];
		rank=0;
		name=scheduleName;
	}
	
	public void fillSchedule(){
		Random randomValue=new Random();
		for (int i=0;i<12;i++){
			int value=randomValue.nextInt(3);
			if (value==0)schedule[i]=true;
			else schedule[i]=false;
		}
	}
	
	public void setAll(boolean value){
		for (int i=0;i<12;i++) schedule[i]=value;
	}
	
	public int findOpenBlock(int startIndex){
		// int numFound=0;
		boolean found1=false;
		for (int i=startIndex; i<12; i++){
			if (schedule[i]==false) {
				if (found1==true)return i-1;
				else found1=true;
			}
			else found1=false;
		}
		return -1;
	}
	
	public int countOpenBlocks(){
		int i=0;
		int count=0;
		while (i<12 && i!=-1){
			i=findOpenBlock(i);
			if (i!=-1) {count++;i++;}
		}
		return count;
	}
	
	public void determineRank(){
		rank=countOpenBlocks();
	}
	
	public int getRank(){
		return rank;
	}
	
	public boolean isBlockOpen(int index){
		if (schedule[index]==false && schedule[index+1]==false)return true;
		else return false;
	}
	
	public String toString(){
		String theString=schedule[0]+"";
		for (int i=1;i<12;i++){
			theString+="\n"+schedule[i];
		}
		return theString;
	}
	
	public void printSchedule(){
		int full;
		System.out.print("\t");
		for (int i=0;i<12;i++){
			if (schedule[i]) full=1;
			else full=0;
			System.out.print(full+"  ");
		}
	}
	
	public int compareTo(SimpleSchedule2 s){
		if (rank<s.getRank()) return -1;
		else if (rank>s.getRank()) return 1;
		else return 0;
	}
	
}
