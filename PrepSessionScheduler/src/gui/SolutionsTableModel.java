package gui;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import model.Parameters;
import model.Session;
import model.Solution;
import model.TimeSlot;

public class SolutionsTableModel extends AbstractTableModel implements TableModel {

	ArrayList<Solution> solutions= new ArrayList<Solution>();
	String[] colNames = {"","Sessions"};
	String tableName;  // how should this data be described in column 0? "Solution"? "Variation"? etc...

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		return solutions.size();
	}

	public void setData(ArrayList<Solution> solutions,String name){
		this.solutions=solutions;
		this.tableName=name;
	}

	@Override
	public String getColumnName(int column) {
		return colNames[column];
	}

	@Override
	public Object getValueAt(int row, int col) {

		String[][] solutionArray = new String[solutions.size()][2];
		//TODO come up with simpler way to do this...
		//...use solution or session objects in table instead of strings?

		for (int i=0;i<solutions.size();i++){
			String printString="";
			Solution sol=solutions.get(i);
			solutionArray[i][0]=tableName+" "+i;
			Collections.sort(sol.getSessions());
			for (Session session:sol.getSessions()){
				TimeSlot index=session.timeSlot;
				String day= Parameters.dayNames[index.getDay()];
				String time=Parameters.timeSlotStrings[index.getTime()];
				printString+=day+" "+time+",";

				//printString+=index+",";
			}
			solutionArray[i][1]=printString;
		}

		if (col==0) return (tableName+" "+row);
		//else return (ArrayList<Session>)solutions.get(row).getSessions();
		else return solutionArray[row][1];
	}

}
