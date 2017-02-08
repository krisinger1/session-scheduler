package gui;

import java.util.EventObject;

public class InputEvent extends EventObject{

	private int maxStudents;
	private int minStudents;
	private int maxSessions;
	private int blockSize;
	private int numSessionsWeight, preferredWeight, canWeight, mustWeight;

	public InputEvent(Object source, int maxStudents,int minStudents, int maxSessions, int blockSize,
			int numSessionsWeight, int preferredWeight, int canWeight, int mustWeight) {
		super(source);
		this.maxStudents = maxStudents;
		this.minStudents=minStudents;
		this.maxSessions=maxSessions;
		this.blockSize=blockSize;
		this.numSessionsWeight=numSessionsWeight;
		this.preferredWeight=preferredWeight;
		this.canWeight=canWeight;
		this.mustWeight=mustWeight;

	}

	public int getNumSessionsWeight() {
		return numSessionsWeight;
	}

	public void setNumSessionsWeight(int numSessionsWeight) {
		this.numSessionsWeight = numSessionsWeight;
	}

	public int getPreferredWeight() {
		return preferredWeight;
	}

	public void setPreferredWeight(int preferredWeight) {
		this.preferredWeight = preferredWeight;
	}

	public int getCanWeight() {
		return canWeight;
	}

	public void setCanWeight(int canWeight) {
		this.canWeight = canWeight;
	}

	public int getMustWeight() {
		return mustWeight;
	}

	public void setMustWeight(int mustWeight) {
		this.mustWeight = mustWeight;
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
