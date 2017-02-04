package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.intervarsity.Parameters;

import model.Solution;

public class ResultsPanel extends JPanel {
	private JButton saveButton = new JButton("Save Solution");
	private JTable solutionsTable;
	private SolutionsTableModel solutionsTableModel = new SolutionsTableModel();
	private JTable variationsTable;
	private SolutionsTableModel variationsTableModel = new SolutionsTableModel();
	private JScrollPane solScrollPane,varScrollPane;
	private ResultsTableListener listener;
	public ResultsPanel(){
		super();

		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		setBackground(Parameters.schemeColor2);
		//add(testButton);

		MultiLineTblCellRenderer renderer = new MultiLineTblCellRenderer();
		solutionsTable= new JTable(solutionsTableModel);

		solutionsTable.setMaximumSize(new Dimension(400,100));
		solutionsTable.getColumn("").setMaxWidth(100);
		int rowHeight = (solutionsTable.getFont().getSize()+8)*2;
		solutionsTable.setRowHeight(rowHeight);
		//solutionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		solutionsTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton()==MouseEvent.BUTTON1){
					super.mouseClicked(e);
					int row=solutionsTable.rowAtPoint(e.getPoint());
					System.out.println("in mouseclicked...");
					listener.rowSelected(row);
				}
			}

		});

//		solutionsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//
//			@Override
//			public void valueChanged(ListSelectionEvent event) {
//				//System.out.println("in valuechanged...");
//				//listener.rowSelected(solutionsTable.getSelectedRow());
//			}
//		});

		//solutionsTable.getColumnModel().getColumn(1).setCellRenderer(renderer);
		solutionsTable.getColumn("Sessions").setCellRenderer(renderer);

		variationsTable = new JTable(solutionsTableModel);
		//variationsTable = new JTable(variationsTableModel);
		variationsTable.setRowHeight(rowHeight);
		variationsTable.getColumn("").setMaxWidth(100);
		variationsTable.getColumn("Sessions").setCellRenderer(renderer);

		solScrollPane = new JScrollPane(solutionsTable);
		solScrollPane.setMaximumSize(new Dimension(400,800));
		solScrollPane.setMinimumSize(new Dimension(300,300));

		varScrollPane = new JScrollPane(variationsTable);
		varScrollPane.setMaximumSize(new Dimension(400,800));
		varScrollPane.setMinimumSize(new Dimension(300,300));

		gc.insets = new Insets(50, 20, 0, 0);
		gc.gridx=0;
		gc.gridy=0;
		gc.weightx=1;
		gc.weighty=0;
		gc.anchor=GridBagConstraints.SOUTHWEST;
		add(new JLabel("Solutions:"),gc);

		gc.insets = new Insets(50, 20, 0, 20);
		gc.gridx=1;
		add(new JLabel("Variations of selected solution:"),gc);

		gc.anchor=GridBagConstraints.NORTHWEST;
		gc.insets = new Insets(0, 20, 0, 0);
		gc.gridx=0;
		gc.gridy++;
		gc.weightx=1;
		gc.weighty=5;
		add(solScrollPane,gc);

		gc.insets = new Insets(0, 20, 0, 20);
		gc.gridx=1;
		gc.weightx=1;
		gc.weighty=5;
		add(varScrollPane,gc);

		gc.gridx=0;
		gc.gridy++;
		gc.weightx=1;
		gc.weighty=1;
		add(saveButton,gc);

		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Save button pressed");
			}
		});

	}
	public void setResultsTableListener(ResultsTableListener resultsTableListener) {
		listener = resultsTableListener;
	}
	public void setVariationsData(ArrayList<Solution> variations) {
		variationsTableModel.setData(variations);
	}
	public void setSolutionsData(ArrayList<Solution> solutions) {
		solutionsTableModel.setData(solutions);
	}
	public void refreshSolutions(){
		solutionsTableModel.fireTableDataChanged();
	}
}
