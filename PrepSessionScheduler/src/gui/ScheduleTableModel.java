package gui;

import javax.swing.table.AbstractTableModel;

public class ScheduleTableModel extends AbstractTableModel{
	private int[][] days;
	private String[] times ={"9:00","9:30","10:00","10:30","11:00","11:30","12:00","12:30","1:00","1:30","2:00","2:30","3:00","3:30","4:00","4:30","5:00"};
	private String[] colNames = {"","Wed","Thurs", "Fri"};

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex==0) return false;
		else return true;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		if (days==null) return;
		days[col-1][row]=(Integer)value;
		//days[col-1][row]=Integer.parseInt((String) value);
		return;
	}

	@Override
	public String getColumnName(int column) {
		return colNames[column];
	}

	public void setData(int[][] timeSlots){
		//System.out.println("days[][] in model: "+days);
		this.days=timeSlots;
		//System.out.println("days[][] in model: "+days);
	}

	public int[][] getData(){
		return days;
	}

	@Override
	public int getColumnCount() {
		//if (days==null) return 0;
		return 4;
	}

	@Override
	public int getRowCount() {
		//if (days==null) return 0;
		return days[0].length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (col==0){
			return times[row];
		}
		else if (days!=null) return days[col-1][row];
		else return null;
	}

}
