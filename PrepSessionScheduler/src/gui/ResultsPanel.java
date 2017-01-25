package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.intervarsity.Parameters;

import com.mysql.jdbc.log.Jdk14Logger;

import unused.SolutionsTableModel;

public class ResultsPanel extends JPanel {
	private JButton saveButton = new JButton("Save Solution");
	private JTable solutionsTable;
	private JTable variationsTable;
	private JScrollPane solScrollPane,varScrollPane;
	public ResultsPanel(){
		super();

		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		setBackground(Parameters.schemeColor2);
		//add(testButton);

		MultiLineTblCellRenderer renderer = new MultiLineTblCellRenderer();
		solutionsTable= new JTable(new SolutionsTableModel());

		solutionsTable.setMaximumSize(new Dimension(400,100));
		solutionsTable.getColumn("").setMaxWidth(100);
		int rowHeight = (solutionsTable.getFont().getSize()+8)*2;
		solutionsTable.setRowHeight(rowHeight);

		//solutionsTable.getColumnModel().getColumn(1).setCellRenderer(renderer);
		solutionsTable.getColumn("Sessions").setCellRenderer(renderer);
		
		variationsTable = new JTable(new SolutionsTableModel());
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
}
