package org.intervarsity;

import java.util.Random;

public class SimpleSchedule extends Schedule{
	//boolean[] schedule;
	//int rank;
	//String name;
	int size;
	
	
	public SimpleSchedule(String scheduleName,int size){
		super(scheduleName,size);
		this.size=size;
	}
	
	public void fillSchedule(){
		Random randomValue=new Random();
		for (int i=0;i<size;i++){
			int value=randomValue.nextInt(3);
			//if (value==0)schedule[i]=true;
			//else schedule[i]=false;
			schedule[i]=value;
		}
	}
	
	public void fillSchedule(int[] schedule){
		this.schedule=schedule;
	}
	
	public void setAll(int value){
		for (int i=0;i<size;i++) schedule[i]=value;
	}
	
	public int findOpenBlock(int startIndex, int blockSize ){
		int sum=0;
		for (int i=startIndex; i<=size-blockSize; i++){
			sum=0;
			// sum values (0+0+0+...) means open slot
			if (schedule[i]==0) {
				for (int j=1;j<blockSize;j++){
					sum+= schedule[i+j];
				}
				if (sum==0)return i;	
			}
		}
		return -1;
	}
	
	public int countOpenBlocks(int blockSize){
		int i=0;
		int count=0;
		while (i<size && i!=-1){
			i=findOpenBlock(i,blockSize);
			if (i!=-1) {count++;i++;}
		}
		return count;
	}
	
	public void determineRank(int blockSize){
		super.setRank(countOpenBlocks(blockSize));
	}
	
	public int getRank(){
		return super.getRank();
	}
	
	public boolean isBlockOpen(int index, int blockSize){
		int sum=0;
		if ((index+blockSize)>schedule.length)return false;
		for (int i=index;i<index+blockSize;i++){
			sum+=schedule[i];
		}
		if (sum==0) return true;
		//if (schedule[index]==0 && schedule[index+1]==0)return true;
		else return false;
	}
	
	public String toString(){
		String theString=schedule[0]+"";
		for (int i=1;i<size;i++){
			theString+="\n"+schedule[i];
		}
		return theString;
	}
	
	public void printSchedule(){
		//int full;
		System.out.print("\t\t");
		for (int i=0;i<size;i++){
			//if (schedule[i]==1) full=1;
			//else full=0;
			//System.out.print(full+"  ");
			System.out.print(schedule[i]+"  ");
		}
	}
}
