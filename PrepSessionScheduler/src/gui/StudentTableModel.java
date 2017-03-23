package gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import model.Student;

public class StudentTableModel extends AbstractTableModel{
	private List<Student> db;
	private String[] colNames = {"Name","Area","Email Address"};


	@Override
	public String getColumnName(int column) {
		return colNames[column];
	}

	public void setData(List<Student> db){
		this.db=db;
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public int getRowCount() {
		return db.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		Student student = db.get(row);
		switch(col){
		case 0:
			return student.getFullName();
		case 1:
			return student.getArea();
		case 2:
			return student.getEmail();
			//return student.getSchedule()[0][0];
		}
		return null;
	}


}
