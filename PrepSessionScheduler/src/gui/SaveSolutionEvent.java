package gui;

import java.util.EventObject;

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
