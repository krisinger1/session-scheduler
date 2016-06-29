package org.intervarsity;

public class Time implements Comparable<Time>{
	private int minute;
	private int hour;
	/**
	 * constructor assumes 24 hour clock
	 * @param theHour - the hour portion of the time 0-23
	 * @param theMinute - the minute portion of the time 0-59
	 */
	public Time(int theHour, int theMinute){
		if (theHour>=0 && theMinute>=0){
			hour=theHour%24+theMinute/60;
			//if (hour==0) hour=12;
			minute=theMinute%60;
		}
		else {
			hour=12;
			minute=0;
			System.out.println("Negative time values not allowed.\nTime has been set to 12:00.");
		}
	}
	
	/**
	 * constructor for 12 hour clock
	 * @param theHour hour portion of time 1-12
	 * @param theMinute minute portion of time 0-59
	 * @param ampm - "a" for am "p" for pm, not case sensitive
	 */
	public Time(int theHour, int theMinute, String ampm){
		boolean pm=false;
		if (ampm.equalsIgnoreCase("p")) {
			pm=true;
		}
		else if (ampm.equalsIgnoreCase("a")){
			pm=false;
		}
		else {
			System.out.println("Must enter 'a' or 'p'");
		}
		if (theHour>=0 && theMinute>=0){
			hour=theHour%12+theMinute/60;
			//if (hour==0) hour=12;
			minute=theMinute%60;
			if (pm) hour+=12;
		}
		else {
			hour=12;
			minute=0;
			System.out.println("Negative time values not allowed.\nTime has been set to 12:00.");
		}
	}
	
	/**
	 * gets the hour portion of this time
	 * @return the hour of this time assuming 24 hour clock
	 */
	public int getHour(){
		return hour;
	}
	
	/**
	 * gets the minute portion of this time
	 * @return the minute of this time
	 */
	public int getMinute(){
		return minute;
	}
	
	/**
	 * computes the difference in time |t-this|. 
	 * @param t Time object
	 * @return time difference in minutes unsigned
	 */
	public int timeDifference(Time t){
		if (t!=null){
			int minutesStart=hour*60+minute;
			int minutesEnd=t.getHour()*60+t.getMinute();
			return Math.abs(minutesEnd-minutesStart);
		}
		else return -1;
	}
	/**
	 * compares whether one time is later than another assuming 24 hour clock.
	 */
	public int compareTo(Time t)throws NullPointerException{
		if (t==null)throw new NullPointerException();
		else if (hour<t.getHour()) return -1;
		else if (hour>t.getHour()) return 1;
		else if (minute<t.getMinute()) return -1;
		else if (minute>t.getMinute()) return 1;
		else return 0;
	}
	
	/**
	 * creates a new time equal to this time advanced by increment
	 * @param increment in minutes
	 * @return a new time incremented by increment w/o changing original time
	 */
	public Time nextTime(int increment){
		int m=minute+increment;
		int h=hour+m/60;
		h=h%24;
		//if (h==0) h=12;
		m= m%60;
		return new Time(h, m);
	}
	
	/**
	 * advances this time by increment
	 * @param increment in minutes
	 * @return calling time incremented by increment 
	 */
	public void advanceTime(int increment){
		minute=minute+increment;
		hour=hour+minute/60;
		hour=hour%24;
		//if (hour==0) hour=12;
		minute= minute%60;
	}
	
	/**
	 * returns a formatted string with the time 
	 * using 12 hour clock and appends "am" or "pm" as required
	 */
	public String toString(){
		int h=hour;
		String ampm;
		if (hour<12) {
			ampm="am";
			if (hour==0) h=12;
		}
		else{
			ampm="pm";
			if (hour>12) h=hour%12;
		}
		if (minute>9){
		return h+":"+minute+" "+ampm;
		}
		else return h+":0"+minute+" "+ampm;
	}
}
