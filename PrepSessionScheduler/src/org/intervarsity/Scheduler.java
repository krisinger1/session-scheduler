package org.intervarsity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class Scheduler {
	static Schedule s1,s2,s3,s4,s5,s6,s7,s8,s9,s10;
	static ArrayList<Schedule> schedules ;
	static ArrayList<Solution> solutions=new ArrayList<Solution>();
	static Tree solutionTree;
	//static int blockSize=3; //number of slots needed
	static ArrayList<Day> days=new ArrayList<Day>();

	/**
	 * sort and arraylist of Schedules from worst to best	
	 * @param sList - an ArrayList of Schedule objects
	 * @param blockSize - integer number of schedule blocks needed
	 */
	public static void sortSchedules(ArrayList<Schedule> sList, int blockSize){
		for (Schedule s:sList){
			if (s.getRank()==-1) s.determineRank(blockSize);
		}
		Collections.sort(sList);
	}
		
	/**
	 * creates a tree of possible session combinations that cover all the schedules
	 * and meet other restrictions 
	 * @param sList - list of schedules trying to put into a tree of solutions
	 * @param parent - node of the tree we are starting from 
	 * @param mask - integer array to mask off any not allowable schedule times
	 * @param minSessionSize - int number of students required for a session to be a valid solution
	 * @param SCHED_SIZE- integer number of total blocks in a schedule
	 * @param blockSize - integer number of schedule blocks needed
	 */
	public static void createTree(ArrayList<Schedule> sList,Tree parent, int[] mask, int minSessionSize, int SCHED_SIZE, int blockSize){
		if (sList.size()==0) {
			parent.isEnd=true;
			return;
		}
		else {
			int index=0;
			Schedule worst=sList.get(0);
			boolean foundOne=false;
			while (sList.size()>0 && index<SCHED_SIZE-1 && index>=0){
				index=worst.findOpenBlock(index,blockSize);
				if (index>=0 && mask[index]==0){ //if this is a valid index
					ArrayList<Schedule> clone = (ArrayList<Schedule>)sList.clone();
					ArrayList<Schedule> smallerList=clone;
					ArrayList<Schedule> tempList=new ArrayList<Schedule>();
					for (Schedule s:sList){ //see how many of remaining people can come
						if (s.isBlockOpen(index,blockSize)){
							tempList.add(s);
						}
					}
					if (tempList.size()>=minSessionSize){ //if enough people can come
						foundOne=true;
						Session session =new Session(index);
						Tree solutionLeaf=new Tree(parent,session);
						parent.addLeaf(solutionLeaf);
						int[] newMask=mask.clone();
						newMask[index]=1;
						newMask[index+1]=1;
						//newMask[index+2]=1;

						//for (int i=0;i<blockSize;i++){newMask[index+i]=1;}
						session.members.addAll(tempList);
						smallerList.removeAll(tempList);					
						createTree(smallerList,solutionLeaf,newMask,minSessionSize,SCHED_SIZE,blockSize);
						//Tree.printTree(solutionTree, 0);
					}
				}
				else if (index<0){
					if (!foundOne)parent.isDead=true;
					return;
					}
				index++;
			}
			if (!foundOne) parent.isDead=true;
		}
		return;
	}
	
	/**
	 * Read in schedules from a .csv file.
	 * [0]Name,[1]email,[2+]0=empty 1=full 2=break for new day 
	 * @param filename - name of the file to read from
	 * @return schedules from file as an ArrayList<Schedule>
	 */
	public static ArrayList<Schedule> readSchedulesFromFile (String filename){
		File csvFile=new File(filename);
		 ArrayList<Schedule> schedules=new ArrayList<Schedule>();
		 try{
			 Scanner scanner = new Scanner(csvFile);
			 while (scanner.hasNextLine()){
				 //TODO learn how to check for empty data
				 //TODO read in email addresses as well and add to schedule
				String line = scanner.nextLine();
			    String[] fields = line.split(",");
			    int arraySize=fields.length;
			    int schedSize=arraySize-2;
			    int[] slots=new int[schedSize];
			    int value;
			    for (int i=2;i<arraySize;i++){
			    	// this code is incorrect because file was setup incorrectly in spring 2016 switch 1 & 0
			    	if (fields[i].equals("1")) value=0;
			    	else if (fields[i].equals("0"))value=1;	
			    	else value=2;
			    	slots[i-2]=value;
			    	}
		        scanner.useDelimiter(",");
		        //create the schedule
		        Schedule studentSchedule=new SimpleSchedule(fields[0], schedSize);
		        studentSchedule.setSchedule(slots);
		        studentSchedule.determineRank(BibleStudySchedulerWindow.blockSize);
		        //add schedule to list
		        schedules.add(studentSchedule);
			 }
	         scanner.close();
	         return schedules;
		 }
		 catch (FileNotFoundException e){
			 System.out.println("File not found");
		 }
		 return null;
	}
	
	
	public static String[] createTimeArray(ArrayList<Day> days, int schedSize){
		int index=0;
		String[] times=new String[schedSize];
		//TODO check that number of slots in student arrays is same as number of slots user said were in schedule
		for (Day d:days){
			int increment=d.getSlotIncrement();
			Time time=d.getStartTime();
			//System.out.println(time);
			String dayName=d.getName();
			int slotNum=d.getSlotNumber();
			for (int j=0;j<slotNum;j++){
				
				times[index]=dayName+" "+time.toString();
				System.out.println(times[index]);

				time=time.nextTime(increment);
				index++;
			}
			if (index<schedSize){
				times[index]="********";
				System.out.println(times[index]);
				index++;
			}
		}
		return times;
	}
	
}
