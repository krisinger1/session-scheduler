package gui;

import java.util.EventObject;

import model.Solution;

public class SaveSolutionEvent extends EventObject{
	int index;

	public SaveSolutionEvent(Object source, int saveRow) {
		super(source);
		index = saveRow;

	}

	public int getIndex() {
		return index;
	}


}
