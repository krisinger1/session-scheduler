package gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import model.Session;
import model.Solution;
import model.TimeSlot;

public class SolutionsTableModel extends AbstractTableModel implements TableModel {

	ArrayList<Solution> solutions= new ArrayList<Solution>();
	String[] colNames = {"","Sessions"};

	//temporary
	public SolutionsTableModel(){
//		String[] data = {"session 0","session2"};
//		String[] data2 = {"session 1","session2"};
//
//		solutions.add(data);
//		solutions.add(data2);

		//solutions.add("solution 1");
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		return solutions.size();
	}

	public void setData(ArrayList<Solution> solutions){
		this.solutions=solutions;
	}

	@Override
	public String getColumnName(int column) {
		return colNames[column];
	}

	@Override
	public Object getValueAt(int row, int col) {
		String[][] solutionArray = new String[solutions.size()][2];
		//System.out.println("solutionstablemodel valueat" + solutions.get(row).toString());
		//TODO come up with simpler way to do this...
		//TODO also in "if" limit number of solutions to show
		for (int i=0;i<solutions.size();i++){
			String printString="";
			//listModel= new DefaultListModel();
			Solution sol=solutions.get(i);
			solutionArray[i][0]="Solution "+i;
			//textSolutionOutput.append(""+sol.getRank()+"\n");
			Collections.sort(sol.getSessions());
			for (Session session:sol.getSessions()){
				TimeSlot index=session.timeSlot;
				printString+=index+",";
			}
			solutionArray[i][1]=printString;
		}

		if (col==0) return ("Solution "+row);
		//else return (ArrayList<Session>)solutions.get(row).getSessions();
		else return solutionArray[row][1];
	}

}
