package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

import org.intervarsity.Parameters;

public class ScheduleInputPanel extends JPanel {
	private JTable scheduleTable;
	private ScheduleTableModel scheduleTableModel;
	//private ScheduleListener scheduleTableListener;

	public ScheduleInputPanel(){
		scheduleTableModel = new ScheduleTableModel();
		scheduleTable = new JTable(scheduleTableModel);
		scheduleTable.setDefaultRenderer(Object.class, new ScheduleTableRenderer());

		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(250,450));
		setMinimumSize(new Dimension(150,500));
		setBackground(Parameters.schemeColor2);

		add(new JScrollPane(scheduleTable),BorderLayout.CENTER);

	}

	public void setData(int[][] days){
		scheduleTableModel.setData(days);
	}

	public int[][] getData(){
		return scheduleTableModel.getData();
	}

//	public void clearSchedule(){
//		int[][] days = new int[3][17];
//		for (int i=0; i<3;i++){
//			for (int j=0;j<17;j++){
//				days[i][j]=0;
//			}
//		}
//		scheduleTableModel.setData(days);
//	}

	public void refresh(){
		//scheduleTableModel.fireTableDataChanged();
	}

}
