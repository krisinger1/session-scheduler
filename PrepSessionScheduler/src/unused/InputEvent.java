package unused;

import java.util.EventObject;

public class InputEvent extends EventObject{

	int maxStudents;
	int minStudents;
	int maxSessions;
	int blockSize;
	
	public InputEvent(Object source, int maxStudents) {
		super(source);
		this.maxStudents = maxStudents;
		
	}
	
	public int getMaxStudents() {
		return maxStudents;
	}

	public void setMaxStudents(int maxStudents) {
		this.maxStudents = maxStudents;
	}

	public int getMinStudents() {
		return minStudents;
	}

	public void setMinStudents(int minStudents) {
		this.minStudents = minStudents;
	}

	public int getMaxSessions() {
		return maxSessions;
	}

	public void setMaxSessions(int maxSessions) {
		this.maxSessions = maxSessions;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}



}
