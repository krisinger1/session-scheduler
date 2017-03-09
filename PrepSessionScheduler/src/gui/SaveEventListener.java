package gui;

import java.util.EventListener;

public interface SaveEventListener extends EventListener{
	public void saveEventOccurred(SaveSolutionEvent se);
}
