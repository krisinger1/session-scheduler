package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.intervarsity.Parameters;

import model.Student;

public class TablePanel extends JPanel{
	private JTable studentTable;
	private StudentTableModel studentTableModel;
	//private JScrollPane tableScrollPane=new JScrollPane();


	public TablePanel(){
		studentTableModel = new StudentTableModel();

		studentTable = new JTable(studentTableModel);

		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(500,500));
		setMinimumSize(new Dimension(400,500));
		setBackground(Parameters.schemeColor2);
		//tableScrollPane.setViewportView(studentTable);

		add(new JScrollPane(studentTable),BorderLayout.CENTER);
	}

	public void setData(List<Student> db){
		studentTableModel.setData(db);
	}

	public void refresh(){
		studentTableModel.fireTableDataChanged();
	}
}