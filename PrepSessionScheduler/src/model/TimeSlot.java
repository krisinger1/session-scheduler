package model;

public class TimeSlot implements Comparable<TimeSlot>{
	int day;
	int time;

	public TimeSlot(int day,int time){
		this.day=day;
		this.time=time;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String toString(){
		return "day: "+day+" time: "+time;
	}

	@Override
	public int compareTo(TimeSlot t) {
		if (day<t.getDay()) return -1;
		else if (day>t.getDay()) return 1;
		else if (time<t.getTime()) return -1;
		else if (time>t.getTime()) return 1;
		return 0;
	}

}
