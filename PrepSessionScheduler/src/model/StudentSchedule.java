package model;

public class StudentSchedule extends Schedule {

	public StudentSchedule(int size) {
		super(size);
	}
	
	public StudentSchedule(int[] scheduleArray){
		super(scheduleArray);
	}

	/**
	 * set all values this array to value
	 * @param value
	 */
	public void setAll(int value){
		for (int i=0;i<size();i++) timeSlots[i]=value;
	}

	/**
	 * find the first open block of size blocksize in the schedule
	 * @param blockSize the number of consecutive open slots in schedule needed (0's)
	 * @param startindex index to start searching from
	 */
	public int findOpenBlock(int startIndex, int blockSize ){
		for (int i=startIndex; i<=size()-blockSize; i++){
			if (isBlockOpen(i, blockSize)) return i;
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
		while (i<size() && i!=-1){
			i=findOpenBlock(i,blockSize);
			if (i!=-1) {count++;i++;}
		}
		return count;
	}

	@Override
	public void determineRank(int blockSize) {
		super.setRank(countOpenBlocks(blockSize));

	}

	@Override
	public boolean isBlockOpen(int index, int blockSize) {
//		//TODO check this. it is different from SimpleSchedule in effort to simplify
//		return (index==findOpenBlock(index, blockSize));
		int sum=0;
		if ((index+blockSize)>timeSlots.length)return false;
		for (int i=index;i<index+blockSize;i++){
			sum+=timeSlots[i];
		}
		if (sum==0) return true;
		else return false;
	}

}
