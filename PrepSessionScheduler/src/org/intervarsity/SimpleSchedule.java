package org.intervarsity;

import java.util.Random;
public class SimpleSchedule extends Schedule{
	int size;

	public SimpleSchedule(String scheduleName,String email,int size){
		super(scheduleName,email, size);
		this.size=size;
	}

	/**
	 * fills array with random 0's and 1's
	 */
	public void fillSchedule(){
		Random randomValue=new Random();
		for (int i=0;i<size;i++){
			int value=randomValue.nextInt(3);
			//if (value==0)schedule[i]=true;
			//else schedule[i]=false;
			schedule[i]=value;
		}
	}

	/**
	 * sets array to be schedule
	 * @param schedule the array representing open and full spaces in schedule
	 * 0 empty, 1 full
	 */
	public void fillSchedule(int[] schedule){
		this.schedule=schedule;
	}

	/**
	 * set all values this array to value
	 * @param value
	 */
	public void setAll(int value){
		for (int i=0;i<size;i++) schedule[i]=value;
	}

	/**
	 * find the first open block of size blocksize in the schedule
	 * @param blockSize the number of consecutive open slots in schedule needed (0's)
	 * @param startindex index to start searching from
	 */
	public int findOpenBlock(int startIndex, int blockSize ){
		int sum=0;
		for (int i=startIndex; i<=size-blockSize; i++){
			sum=0;
			// sum values (0+0+0+...) means open slot
			//correct code below
			if (schedule[i]==0) {
			//if (schedule[i]==1){
				for (int j=1;j<blockSize;j++){
					//if (schedule[i+j]==2)  sum=blockSize;//remove this line for correct code
					 sum+= schedule[i+j];
				}
				//correct code below
				if (sum==0)return i;
				//for backwards schedules 0/1 flipped - not exactly right, not accounting for "2" slots
				//if (sum>=blockSize) return i;
			}
		}
		return -1;
	}

	/**
	 * count number of open blocks of size blockSize in this schedule
	 * @param blockSize the number of consecutive open slots in schedule needed (0's)
	 * @return the total number of open blocks
	 */
	public int countOpenBlocks(int blockSize){
		int i=0;
		int count=0;
		while (i<size && i!=-1){
			i=findOpenBlock(i,blockSize);
			if (i!=-1) {count++;i++;}
		}
		return count;
	}

	/**
	 * Determine how easy to work with a schedule is based on number of open blocks
	 * Higher rank is better
	 */
	public void determineRank(int blockSize){
		super.setRank(countOpenBlocks(blockSize));
	}

	public int getRank(){
		return super.getRank();
	}

	/**
	 * Determines if a block is open in a schedule
	 * @param index first index of the block to check
	 * @param blockSize the number of consecutive open slots in schedule needed (0's)
	 */
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
		System.out.print("\t\t");
		for (int i=0;i<size;i++){
			System.out.print(schedule[i]+"  ");
		}
	}
}
