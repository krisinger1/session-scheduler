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
	
	private static void setup(){
			
//		s1=new SimpleSchedule("one",SCHED_SIZE);
//		//s1.fillSchedule();
//		s1.schedule[0]=FULL;
//		s1.schedule[1]=EMPTY;
//		s1.schedule[2]=EMPTY;
//		s1.schedule[3]=FULL;
//		s1.schedule[4]=EMPTY;
//		s1.schedule[5]=0;
//		s1.schedule[6]=0;
//		s1.schedule[7]=1;
//		s1.schedule[8]=0;
//		s1.schedule[9]=1;
//		s1.schedule[10]=0;
//		s1.schedule[11]=0;
//		s1.determineRank(blockSize);
//		
//
//		s2=new SimpleSchedule("two",SCHED_SIZE);
//		s2.fillSchedule();
//		/*s2.schedule[0]=false;
//		s2.schedule[1]=false;
//		s2.schedule[2]=false;
//		s2.schedule[3]=false;
//		s2.schedule[4]=false;
//		s2.schedule[5]=false;
//		s2.schedule[6]=false;
//		s2.schedule[7]=true;
//		s2.schedule[8]=false;
//		s2.schedule[9]=true;
//		s2.schedule[10]=false;
//		s2.schedule[11]=true;*/
//		s2.determineRank(blockSize);
//		
//		s3=new SimpleSchedule("three",SCHED_SIZE);
//		s3.fillSchedule();
//		/*s3.schedule[0]=true;
//		s3.schedule[1]=true;
//		s3.schedule[2]=true;
//		s3.schedule[3]=true;
//		s3.schedule[4]=true;
//		s3.schedule[5]=true;
//		s3.schedule[6]=false;
//		s3.schedule[7]=false;
//		s3.schedule[8]=false;
//		s3.schedule[9]=false;
//		s3.schedule[10]=false;
//		s3.schedule[11]=false;*/
//		s3.determineRank(blockSize);
//		
//		s4=new SimpleSchedule("four",SCHED_SIZE);
//		s4.fillSchedule();
//		/*s4.schedule[0]=true;
//		s4.schedule[1]=true;
//		s4.schedule[2]=true;
//		s4.schedule[3]=true;
//		s4.schedule[4]=true;
//		s4.schedule[5]=true;
//		s4.schedule[6]=true;
//		s4.schedule[7]=false;
//		s4.schedule[8]=false;
//		s4.schedule[9]=false;
//		s4.schedule[10]=true;
//		s4.schedule[11]=true;*/
//		s4.determineRank(blockSize);
//		
//		s5 =new SimpleSchedule("five",SCHED_SIZE);
//		s5.fillSchedule();
//		s5.determineRank(blockSize);
//		
//		s6 =new SimpleSchedule("six",SCHED_SIZE);
//		s6.fillSchedule();
//		s6.determineRank(blockSize);
//
//		s7 =new SimpleSchedule("seven",SCHED_SIZE);
//		s7.fillSchedule();
//		s7.determineRank(blockSize);
//		
//		s8 =new SimpleSchedule("eight",SCHED_SIZE);
//		s8.fillSchedule();
//		s8.determineRank(blockSize);
//		
//		s9 =new SimpleSchedule("nine",SCHED_SIZE);
//		s9.fillSchedule();
//		s9.determineRank(blockSize);
//		
//		s10 =new SimpleSchedule("ten",SCHED_SIZE);
//		s10.fillSchedule();
//		s10.determineRank(blockSize);

	}

	
	
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
	
//	public static ArrayList<Solution> createSolutions(Tree t,ArrayList<Session> sessionList,ArrayList<Schedule> schedules,int blockSize){
//		ArrayList<Solution> solutions=new ArrayList<Solution>();
//
//		if (t.isEnd) {
//			Solution solution=new Solution();
//			ArrayList<Session> sessionListClone = (ArrayList<Session>) sessionList.clone();
//			solution.setSessions(sessionList);
//			//solution.print();
//			solution.findAllMembers(schedules, blockSize);
//			solutions.add(solution);
//			//solution.print();
//		}
//		else for (Tree leaf:t.leaves){
//			int time=leaf.session.time;
//			boolean preferred;
//			if (preferredMask[time]==0) preferred=true;
//			else preferred = false;
//			Session s=new Session(time,preferred);
//			ArrayList<Session> sessionListCopy=(ArrayList<Session>)sessionList.clone();
//			sessionListCopy.add(s);
//			createSolutions(leaf, sessionListCopy,blockSize);
//			
//		}
//		return solutions;
//	}
	
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
	
//	public static void printSolutions(){
//		//for (Solution sol:solutions){
//		for (int i=0; i<solutions.size();i++){
//			if (solutions.get(i).getNumSessions()<=maxSessions){
//				System.out.println();
//				Solution sol=solutions.get(i);
//				System.out.println("*******Solution "+i+"********");
//				System.out.format("Rank: %.2f\n",sol.getRank());
//				Collections.sort(sol.getSessions());
//				for (Session session:sol.getSessions()){
//					int index=session.time;
//					System.out.println("\t"+times[index]);
//					
//					//print ratio of must attend to can attend
//					//System.out.println(session.membersMustAttend.size()+"/"+session.members.size());
//					
//					//print names of those who can attend
//					//session.print();
//					//System.out.println();
//					
//					//print names of those who must attend:
//					//session.printMustAttend();
//					//System.out.println();
//				}
//			}
//		}
//	}
	
	public static void getSetUpFromUser(int increment, int maxNumSessions,int minStudents, int blkSize){
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
		
	}
	
	public static void runScheduler(int increment, int maxSessions, int minStudents, int blockSize) {
		// TODO get constraints from user- blockSize, mask of allowable times
		//setup();
		//getSetUpFromUser();
		//TODO finish getting user input then remove these lines
//		Day day1 = new Day(30, 8,new Time(13,30) , "Wednesday");
//		Day day2 = new Day(30, 17, new Time(9, 0), "Thursday");
//		Day day3 = new Day(30, 17, new Time(9, 0), "Friday");
//		days.add(day1);
//		days.add(day2);
//		days.add(day3);
		//createTimeArray();
		//schedules=new ArrayList<Schedule>();
		//readSchedulesFromFile("schedule_fall_2015_test.csv");
		//ArrayList<Schedule> schedulesCopy = (ArrayList<Schedule>)schedules.clone();
		//solutionTree=new Tree(null, new Session(-1));
		// find worst schedule
		//sortSchedules(schedulesCopy);
		
		//int[] possibleMask=new int[SCHED_SIZE];
		// print out each students schedule:
		//for (Schedule s:schedulesCopy){
		//	System.out.print(s.getName()+" ");
		//	s.printSchedule();
		//	System.out.println();
		//	}
//		do {
//			createTree(schedulesCopy, 0,solutionTree,possibleMask);
//			if (!solutionTree.hasLeaves()) {
//				System.out.println("Impossible schedule, "+schedulesCopy.get(0).getName()+"removed");
//				schedulesCopy.remove(0);
//			}
//		}
//		while (!solutionTree.hasLeaves());
//		//System.out.println("*******Before Pruning***********");
		//Tree.printTree(solutionTree,0);
		//Tree.printSolutionToFile(solutionTree, 0,"solution.txt");
		//if (!(solutionTree==null)) solutionTree.pruneTree();
		//System.out.println("*******After Pruning***********");
		//Tree.printTree(solutionTree,0);
		//Tree.printRankedSolutionToFile(solutionTree, "solutionList.txt");
		//TODO if tree is dead, remove worst schedule and try again
		
		//createSolutions(solutionTree, new ArrayList<Session>());
		// prints the days and times of all slots:
		//for (int i=0;i<times.length;i++){
		//	System.out.println(times[i]);
		//}
//		for (Solution sol:solutions){
//			sol.calculateNormalizedRank(fewestSessionsWeight, preferredTimesWeight, canComeWeight, mustComeWeight, solutions);
//		}
		//Collections.sort(solutions);
		/*for (Solution sol:solutions){
			System.out.format("%.2f",sol.getRank());
			Collections.sort(sol.getSessions());
			for (Session session:sol.getSessions()){
				int index=session.time;
				System.out.print("\t"+times[index]);
				System.out.println();
			}
		}*/
		//printSolutions();
	}

}
