package gui;

import java.util.Arrays;

import javax.swing.table.AbstractTableModel;

public class ScheduleTableModel extends AbstractTableModel{
	private int[][] days;
	private int[][] mask;
	//private String[] times ={"9:00","9:30","10:00","10:30","11:00","11:30","12:00","12:30","1:00","1:30","2:00","2:30","3:00","3:30","4:00","4:30","5:00"};
	private String[] times;
	private String[] dayNames;
	//private String[] colNames = {"","Wed","Thurs", "Fri"};

	public ScheduleTableModel(String[] timesStrings,String[] dayStrings){
		super();
		times=timesStrings;
		dayNames=dayStrings;
		mask = new int[dayNames.length][times.length];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex==0) return false;
		//else if (areSameSize(mask, days))
		else {
			return (mask[columnIndex-1][rowIndex]==0);
		}
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		if (days==null) return;
		if (value instanceof String){
			days[col-1][row]=Integer.parseInt((String)value);
		}
		else days[col-1][row]=(Integer)value;
		return;
	}

	@Override
	public String getColumnName(int column) {
		if (column==0) return "";
		else return dayNames[column-1];
		//return colNames[column];
	}

	public void setBlockOutMask(int[][] mask){
		this.mask=mask;
	}

	public void setData(int[][] timeSlots){
		this.days=timeSlots;
		for (int i=0;i<dayNames.length;i++){
			for (int j=0;j<times.length;j++){
				if (mask!=null && mask[i][j]==1 ) this.days[i][j]=1;
			}
		}
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

	private boolean areSameSize(int[][] mask,int[][] days){
		return ((mask.length==days.length)&&(mask[0].length==days[0].length));
	}
}
