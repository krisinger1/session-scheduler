package org.intervarsity;

import java.util.ArrayList;
import java.util.Collections;


public class Scheduler {
	static Schedule s1,s2,s3,s4,s5,s6,s7,s8,s9,s10;
	static ArrayList<Schedule> schedules ;
	static ArrayList<Solution> solutions=new ArrayList<Solution>();
	static Tree solutionTree;
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
	public static void createTree(ArrayList<Schedule> sList,Tree parent, int[] mask, int minSessionSize, int SCHED_SIZE, int blockSize, int maxStudents){
		//System.out.println("in createTree");
		if (sList.size()==0) {
			//System.out.println("sList.size=0");
			parent.isEnd=true;
			return;
		}
		else {
			int index=0;
			Schedule worst=sList.get(0);
			//System.out.print(worst.getName()+" is worst schedule");
			boolean foundOne=false;
			while (sList.size()>0 && index<SCHED_SIZE-1 && index>=0){
				index=worst.findOpenBlock(index,blockSize);
				boolean maskOpen=false;
				int numOpen=0;
				for (int i=0;i<blockSize;i++){
					if (index>=0 && mask[index+i]==0) numOpen++;
				}
				if (numOpen==blockSize)maskOpen = true;
				if (index>=0 && maskOpen){ //if this is a valid index
					ArrayList<Schedule> clone = (ArrayList<Schedule>)sList.clone();
					ArrayList<Schedule> smallerList=clone;
					ArrayList<Schedule> tempList=new ArrayList<Schedule>();
					for (Schedule s:sList){ //see how many of remaining people can come
						if (s.isBlockOpen(index,blockSize) && tempList.size()<maxStudents){ //keep adding schedules to max students to then force more sessions
							tempList.add(s);
						}
					}

					//if (tempList.size()>=minSessionSize){ //if enough people can come
						foundOne=true;
						Session session =new Session(index);
						//System.out.println("index in createTree is (found is true): "+index);

						Tree solutionLeaf=new Tree(parent,session);
						parent.addLeaf(solutionLeaf);
						int[] newMask=mask.clone();
						for (int j=0;j<blockSize;j++){ //block out a blockSize section at "index" so no overlapping sessions
							newMask[index+j]=1;
						}
						session.members.addAll(tempList);
						smallerList.removeAll(tempList);
						createTree(smallerList,solutionLeaf,newMask,minSessionSize,SCHED_SIZE,blockSize,maxStudents);
						//Tree.printTree(solutionLeaf, 0);
					//}
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
}
