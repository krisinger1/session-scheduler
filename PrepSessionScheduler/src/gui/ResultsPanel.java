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
import javax.swing.JTextArea;
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
	private JTextArea membersArea;
	private JScrollPane membersScrollPane;
	private SolutionsTableListener solListener;
	private VariationsTableListener varListener;
	private SaveEventListener saveEventListener;
	private int saveRow;
	public ResultsPanel(){
		super();

		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		setBackground(Parameters.schemeColor2);

		MultiLineTblCellRenderer renderer = new MultiLineTblCellRenderer();
		solutionsTable= new JTable(solutionsTableModel);

		solutionsTable.setMaximumSize(new Dimension(400,100));
		solutionsTable.getColumn("").setMaxWidth(100);
		//int rowHeight = (solutionsTable.getFont().getSize()+8)*4;

		//solutionsTable.setRowHeight(rowHeight);
		//solutionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		solutionsTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton()==MouseEvent.BUTTON1){
					super.mouseClicked(e);
					int row=solutionsTable.rowAtPoint(e.getPoint());
						solListener.rowSelected(row);
				}
			}

		});

		solutionsTable.getColumn("Sessions").setCellRenderer(renderer);

		variationsTable = new JTable(variationsTableModel);
		variationsTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				int row=variationsTable.rowAtPoint(e.getPoint());
				varListener.rowSelected(row);
				saveRow=row;
			}
		});

		//variationsTable = new JTable(variationsTableModel);
		//variationsTable.setRowHeight(rowHeight);
		variationsTable.getColumn("").setMaxWidth(100);
		variationsTable.getColumn("Sessions").setCellRenderer(renderer);

		solScrollPane = new JScrollPane(solutionsTable);
		solScrollPane.setMaximumSize(new Dimension(400,300));
		solScrollPane.setMinimumSize(new Dimension(300,300));

		varScrollPane = new JScrollPane(variationsTable);
		varScrollPane.setMaximumSize(new Dimension(400,300));
		varScrollPane.setMinimumSize(new Dimension(300,300));

		membersArea = new JTextArea();
		membersArea.setMaximumSize(new Dimension(800,500));
		membersArea.setMinimumSize(new Dimension(400,300));

		membersScrollPane=new JScrollPane(membersArea);
		membersScrollPane.setMinimumSize(new Dimension(400,300));
		membersScrollPane.setMaximumSize(new Dimension(800,500));


		gc.insets = new Insets(20, 20, 5, 0);
		gc.gridx=0;
		gc.gridy=0;
		gc.weightx=1;
		gc.weighty=1;
		gc.anchor=GridBagConstraints.SOUTHWEST;
		add(new JLabel("Solutions:"),gc);

		gc.insets = new Insets(20, 20, 5, 20);
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

		gc.insets = new Insets(20, 20, 5, 20);
		gc.gridx=0;
		gc.gridy++;
		gc.weightx=1;
		gc.weighty=1;
		gc.anchor=GridBagConstraints.NORTHWEST;
		add(new JLabel("Students for this solution:"),gc);

		gc.insets=new Insets(0, 20, 5, 0);
		gc.gridwidth=2;
		gc.gridx=0;
		gc.gridy++;
		gc.weightx=1;
		gc.weighty=10;
		add(membersScrollPane,gc);

		gc.insets = new Insets(0, 20, 20, 20);
		gc.gridx=0;
		gc.gridy++;
		gc.weightx=1;
		gc.weighty=1;
		add(saveButton,gc);

		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (saveRow!=-1){
					saveEventListener.saveEventOccurred(new SaveSolutionEvent(this, saveRow));
					}
				}
		});

	}
	public void setSolutionsTableListener(SolutionsTableListener solTableListener) {
		solListener = solTableListener;
	}
	public void setVariationsTableListener(VariationsTableListener varTableListener){
		varListener=varTableListener;
	}
	public void setVariationsData(ArrayList<Solution> variations) {
		variationsTableModel.setData(variations,"Variation");
	}
	public void setSolutionsData(ArrayList<Solution> solutions) {
		solutionsTableModel.setData(solutions,"Solution");
	}
	public void refreshSolutions(){
		solutionsTableModel.fireTableDataChanged();
	}
	public void refreshVariations(){
		variationsTableModel.fireTableDataChanged();
	}

	public void setMembersData(String members) {
		membersArea.setText(members);
	}
	public void setSaveEventListener(SaveEventListener listener) {
		saveEventListener=listener;
	}




}
