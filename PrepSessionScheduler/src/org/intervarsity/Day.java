package org.intervarsity;

public class Day {
	private int slotIncrement;
	private int slotNumber;
	private Time startTime;
	private Time endTime;
	private String name;
	
	/**
	 * constructor
	 * @param increment in minutes as int
	 * @param number number of time slots in a day as Time object
	 * @param start  time of the first slot as Time object
	 * @param dayname  name of the day as a string
	 */
	public Day(int increment, int number, Time start, String dayName){
		slotNumber=number;
		slotIncrement=increment;
		startTime=start;
		name=dayName;
		endTime=start.nextTime(increment*(number));
	}
	
	/**
	 * alternate constructor
	 * @param increment in minutes as int
	 * @param start time of the first slot as Time object
	 * @param end start time of the last slot as Time object
	 * @param dayName name of the day as string
	 */
	public Day(int increment, Time start,Time end, String dayName){
		slotNumber=(start.timeDifference(end)/increment);
		slotIncrement=increment;
		startTime=start;
		name=dayName;
		endTime=end;
	}	
	 
	public int getSlotNumber(){
		 return slotNumber;
	 }
	
	public Time getStartTime(){
		return startTime;
	}
	
	public Time getEndTime(){
		return endTime;
	}
	
	public int getSlotIncrement(){
		return slotIncrement;
	}
	
	public String getName(){
		return name;
		}
/**
 * prints to the console the name of the day, start and end times, 
 * and slot size	
 */
	public void printDay(){
		System.out.println(name);
		System.out.println(startTime.toString()+"-"+endTime.toString());
		System.out.println(slotIncrement+" minute slots");	
		
	}
	
	public String toString(){
		return name;
	}
}
