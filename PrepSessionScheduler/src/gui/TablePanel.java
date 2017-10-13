package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.intervarsity.Parameters;

import model.Student;

public class TablePanel extends JPanel{
	private JTable studentTable;
	private StudentTableModel studentTableModel;
	private JPopupMenu tablePopup;
	private TableListener studentTableListener;
	private int currentRow = 0;

	public TablePanel(){
		studentTableModel = new StudentTableModel();
		studentTable = new JTable(studentTableModel);
		tablePopup = new JPopupMenu();

		JMenuItem deleteRow = new JMenuItem("Delete Row");
		tablePopup.add(deleteRow);

		studentTable.getColumn("Area").setPreferredWidth(40);
		studentTable.getColumn("Area").setMaxWidth(40);
		studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		studentTable.addMouseListener(new MouseAdapter(){

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3){
					int row = studentTable.rowAtPoint(e.getPoint());
					studentTable.getSelectionModel().setSelectionInterval(row, row);
					tablePopup.show(studentTable, e.getX(), e.getY());
				}
				else if (e.getButton()==MouseEvent.BUTTON1){
					int row= studentTable.getSelectedRow();
					fireSelectionEvent(row);
				}
			}

		});

		studentTable.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				String keyText=KeyEvent.getKeyText(key);
				int row = studentTable.getSelectedRow();
				System.out.println(key +" pressed on row "+row+" "+keyText);
				if (key==40 ||key==38){
					fireSelectionEvent(row);
				}
				else if (key==127){
					if (studentTableListener != null){
						studentTableListener.rowDeleted(row);
						studentTableModel.fireTableRowsDeleted(row, row);
					}
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

	private void fireSelectionEvent(int row){
		row= studentTable.getSelectedRow();
		//reset selection on table to current student if cancelled
		boolean success = studentTableListener.rowSelected(row);
		if (!success){
			System.out.println("selection failed. current row= "+currentRow);
			studentTable.setRowSelectionInterval(currentRow, currentRow);
		}
		else currentRow=row;
	}

	public void setData(List<Student> db){
		studentTableModel.setData(db);
	}

	public void refresh(){
		//System.out.println("tablePanel refresh");
		studentTableModel.fireTableDataChanged();
	}

	public void setTableListener(TableListener listener) {
		studentTableListener = listener;
	}
}