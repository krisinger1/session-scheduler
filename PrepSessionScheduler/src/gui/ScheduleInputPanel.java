package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.intervarsity.Parameters;

public class ScheduleInputPanel extends JPanel {
	private boolean dirty=false;
	private JTable scheduleTable;
	private ScheduleTableModel scheduleTableModel;
	private ScheduleChangeListener scheduleChangeListener;

	public ScheduleInputPanel(){

		scheduleTableModel = new ScheduleTableModel();
		scheduleTable = new JTable(scheduleTableModel);
		scheduleTable.setCellSelectionEnabled(true);
		scheduleTable.setDefaultRenderer(Object.class, new ScheduleTableRenderer());
		scheduleTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		scheduleTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				dirty=true;
				if (scheduleChangeListener!=null) scheduleChangeListener.scheduleChanged();
				int rowIndexStart = scheduleTable.getSelectedRow();
		         int rowIndexEnd = scheduleTable.getSelectionModel().getMaxSelectionIndex();
		         int colIndexStart = scheduleTable.getSelectedColumn();
		         int colIndexEnd = scheduleTable.getColumnModel().getSelectionModel().getMaxSelectionIndex();
		         System.out.println(rowIndexStart+" "+rowIndexEnd+" "+colIndexStart+" "+colIndexEnd);
				if (!e.getValueIsAdjusting()&&rowIndexStart>-1&&colIndexStart>0) {
					for (int r=rowIndexStart;r<=rowIndexEnd;r++){
						for (int c = colIndexStart;c<=colIndexEnd;c++){
							toggleValueAt(r, c);
						}
					}
				}
			}
		});

		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(250,450));
		setMinimumSize(new Dimension(150,500));
		setBackground(Parameters.schemeColor2);

		add(new JScrollPane(scheduleTable),BorderLayout.CENTER);

	}

	public void toggleValueAt(int row, int col){
		Integer value = (Integer) scheduleTable.getValueAt(row, col);
		if (value==0) value=1;
		else value=0;
		scheduleTable.setValueAt(value, row, col);
	}

	public void setData(int[][] days){
		scheduleTableModel.setData(days);
	}

	public int[][] getData(){
		return scheduleTableModel.getData();
	}

	public void clear(){
		scheduleTable.clearSelection();
	}

	public boolean isDirty(){
		return dirty;
	}

	public void addScheduleChangeListener(ScheduleChangeListener listener) {
		scheduleChangeListener=listener;
	}

}
