package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.intervarsity.Parameters;

import model.Student;

public class TablePanel extends JPanel{
	private JTable studentTable;
	private StudentTableModel studentTableModel;
	private JPopupMenu tablePopup;
	private TableListener studentTableListener;

	public TablePanel(){
		studentTableModel = new StudentTableModel();
		studentTable = new JTable(studentTableModel);
		tablePopup = new JPopupMenu();

		JMenuItem deleteRow = new JMenuItem("Delete Row");
		tablePopup.add(deleteRow);

		studentTable.addMouseListener(new MouseAdapter(){

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3){
					int row = studentTable.rowAtPoint(e.getPoint());
					studentTable.getSelectionModel().setSelectionInterval(row, row);
					tablePopup.show(studentTable, e.getX(), e.getY());
				}
			}

		});

		deleteRow.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				int row = studentTable.getSelectedRow();
				if (studentTableListener != null){
					studentTableListener.rowDeleted(row);
					studentTableModel.fireTableRowsDeleted(row, row);
				}
			}

		});

		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(500,500));
		setMinimumSize(new Dimension(400,500));
		setBackground(Parameters.schemeColor2);

		add(new JScrollPane(studentTable),BorderLayout.CENTER);
	}

	public void setData(List<Student> db){
		studentTableModel.setData(db);
	}

	public void refresh(){
		studentTableModel.fireTableDataChanged();
	}

	public void setTableListener(TableListener listener) {
		studentTableListener = listener;
	}
}