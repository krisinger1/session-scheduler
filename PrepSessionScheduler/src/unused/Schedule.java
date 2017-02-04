package unused;

public abstract class Schedule implements Comparable<Schedule>{
	private int rank;
	protected int[] timeSlots;

	public abstract int findOpenBlock(int startIndex,int numSlots);
	public abstract void determineRank(int blockSize);
	public abstract boolean isBlockOpen(int index, int blockSize);

	public Schedule(int size) {
		rank=-1;
		timeSlots = new int[size];
	}

	public Schedule(int[] scheduleArray){
		//int size=scheduleArray.length;
		timeSlots=scheduleArray;
	}

	public int size(){
		return timeSlots.length;
	}

	public String toString(){
		String scheduleString = "";

		for (int i=0;i<size();i++){
			scheduleString +=timeSlots[i]+"  ";
		}
		return scheduleString;
		//return "timeSlots = "+timeSlots.length;
	}

	public int[] getScheduleArray(){
		return timeSlots;
	}

	public void setSchedule(int[] scheduleArray){
		timeSlots=scheduleArray;
	}
	
	public int getTimeSlot(int index){
		return timeSlots[index];
	}
	
	public void setTimeSlot(int index, int value){
		timeSlots[index]=value;
	}

	public int getRank(){
		return rank;
	}

	public void setRank(int rank){
		this.rank=rank;
	}

//	public String getName(){
//		return name;
//	}
//
//	public String getEmail(){
//		return email;
//	}

	public int compareTo(Schedule s){
		if (rank>s.getRank()) return 1;
		else if (rank<s.getRank()) return -1;
		else return 0;
	}



}
