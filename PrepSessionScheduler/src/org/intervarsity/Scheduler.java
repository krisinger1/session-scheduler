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
	static double fewestSessionsWeight=10;
	static double preferredTimesWeight=12;
	static double canComeWeight=2;
	static double mustComeWeight=1;
	static int slotSize;
	static int maxSessions=5;
	static int minSessionSize = 4;
	static final int SCHED_SIZE=44;
	static int blockSize=3; //number of slots needed
	static final int FULL=1;
	static final int EMPTY=0;
	static final int BREAK=2;
	static ArrayList<Day> days=new ArrayList<Day>();
	//static String[] times= new String[SCHED_SIZE];
	static int preferredMask[]=new int[]{
			1,1,1,1,1,1,1,1,2,
			1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,2,
			1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1};
	
		
	public static void sortSchedules(ArrayList<Schedule> sList, int blockSize){
		for (Schedule s:sList){
			if (s.getRank()==-1) s.determineRank(blockSize);
		}
		Collections.sort(sList);
	}
		
	
	public static void createTree(ArrayList<Schedule> sList, int index,Tree parent, int[] mask){
		if (sList.size()==0) {
			parent.isEnd=true;
			return;
		}
		else {
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
						//index++;
						session.members.addAll(tempList);
						//session.print();
						smallerList.removeAll(tempList);					
						createTree(smallerList,0,solutionLeaf,newMask);
						//Tree.printTree(solutionTree, 0);
					}
					//index++;
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
	
	public static ArrayList<Schedule> readSchedulesFromFile (String filename){
		 File csvFile=new File(filename);
		 ArrayList<Schedule> schedules=new ArrayList<Schedule>();
		 try{
			 Scanner scanner = new Scanner(csvFile);
			 while (scanner.hasNextLine()){
				 //TODO learn how to check for empty data
				String line = scanner.nextLine();
			    String[] fields = line.split(",");
			    int arraySize=fields.length;
			    int schedSize=arraySize-2;
			    int[] slots=new int[schedSize];
			    int value;
			    for (int i=2;i<arraySize;i++){
			    	if (fields[i].equals("1")) value=0;
			    	else if (fields[i].equals("0"))value=1;	
			    	else value=2;
			    	slots[i-2]=value;
			    	}
		        scanner.useDelimiter(",");
		        //create the schedule
		        Schedule studentSchedule=new SimpleSchedule(fields[0], schedSize);
		        studentSchedule.setSchedule(slots);
		        studentSchedule.determineRank(blockSize);
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
	

	/*public static void getSetUpFromUser(int increment, int maxNumSessions,int minStudents, int blkSize){
		//
		//
		//how many days are being scheduled?
		//ArrayList<Day> daysTest=new ArrayList<Day>();
		Scanner input = new Scanner( System.in );
		System.out.println("How many days in schedule?");
		int numDays = input.nextInt();
		//start and end times for each day & day name
		System.out.println("How many minutes in slot: ");
		slotSize = increment;
		System.out.println("How many slots do you need for session? ");
		blockSize = blkSize;
		System.out.println("Minimum number of students in a session: ");
		minSessionSize = minStudents;
		System.out.println("Maximum number of sessions to schedule in a week: ");
		maxSessions = maxNumSessions;
		for (int i=1;i<=numDays;i++){
			//TODO error handling for user input - make sure data entered is right type
			System.out.println("For Day "+i);
			System.out.println("Day name: ");
			String dayName=input.next();
			System.out.println("Start time: ");
			System.out.println("Hour: ");
			int startHour = input.nextInt();
			System.out.println("Minute: ");
			int startMin = input.nextInt();
			System.out.println("End time: ");
			System.out.println("Hour: ");
			int endHour = input.nextInt();
			System.out.println("Minute: ");
			int endMin = input.nextInt();
			
			days.add(new Day(slotSize, new Time(startHour, startMin), new Time(endHour, endMin), dayName));
		}
		days.get(0).printDay();
		days.get(1).printDay();
		input.close();
		//schedule split into __minute increments
		//preferred start and end times for each day
		//minimum number of students in a session
		//# of sessions in solution?? or a max # of sessions??
		
	}*/
	
	public static void runScheduler(int increment, int maxSessions, int minStudents, int blockSize) {
	}

}
