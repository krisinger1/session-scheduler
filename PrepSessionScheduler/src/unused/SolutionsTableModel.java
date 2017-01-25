package unused;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class SolutionsTableModel extends AbstractTableModel implements TableModel {

	ArrayList<String[]> solutions= new ArrayList<String[]>();
	String[] colNames = {"","Sessions"};

	//temporary
	public SolutionsTableModel(){
		String[] data = {"session 0","session2"};
		String[] data2 = {"session 1","session2"};

		solutions.add(data);
		solutions.add(data2);

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

	public void setData(ArrayList<String[]> solutions){
		this.solutions=solutions;
	}

	@Override
	public String getColumnName(int column) {
		return colNames[column];
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (col==0) return ("Solution "+row);
		else return solutions.get(row);
	}

}
