package org.intervarsity;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;


import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerListModel;
import javax.swing.JSlider;
import java.awt.Cursor;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableCellRenderer;
import javax.swing.JSeparator;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JScrollPane;
import java.awt.Dimension;



//TODO print solutions to file
public class BibleStudySchedulerWindow implements ActionListener,ChangeListener{
	private ArrayList<Solution> solutions=new ArrayList<Solution>();
	private ArrayList<Solution> distinctSolutions;
	private Solution selectedSolution;
	private String[] times;
	private ArrayList<Schedule> schedules;
	ArrayList<Schedule> schedulesCopy;
	public static int schedSize;
	//private ArrayList<Time> possibleTimes=new ArrayList<Time>();
	private int[] baseMask;
	private int[] preferredMask;
	private JFrame frame;
	//private JCheckBox chckbxMonday,chckbxTuesday,chckbxWednesday,chckbxThursday,chckbxFriday;
	private JTextArea textSolutionOutput;
	private JSlider sldrSessions;
	private double fewestSessionsWeight=5;
	private JSlider sldrPrefTime;
	private double preferredTimesWeight=5;
	private JSlider sldrCan;
	private double canComeWeight=5;
	private JSlider sldrMust;
	private double mustComeWeight=5;
	private JLabel lblPreferred;
	private JLabel lblcan;
	private JLabel lblmust;
	private JLabel lblResults;
	private JLabel lblVarTbl;
	private JPanel panel;
	private JComboBox<Integer> comboMinStudents, comboMaxSessions,comboMaxStudents,comboBlockSize;
	private JScrollPane scrollPane;
	public static int blockSize;
	private int maxSessions;
	private int minStudents;
	private int maxStudents = 25;
	private int maxSolutionsToPrint=10;
	private int increment=30;
	private boolean formChanged=false;
	private JFileChooser chooser = new JFileChooser();
	private File dataFile;
	private JButton btnRun = new JButton("Run");
	private JButton btnSave = new JButton("Save");
	private JLabel lblFileChosen = new JLabel();
	private Color bgColor = new Color(0,206,216);
	private JLabel lblResultsTbl;
	private JTable solutionTable;
	private JTable variationTable;
	private JScrollPane scrollPane2;
	private JScrollPane variationScrollPane;



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BibleStudySchedulerWindow window = new BibleStudySchedulerWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BibleStudySchedulerWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
//		Time t=new Time(8, 0);
//		for (int i=1;i<21;i++){
//			possibleTimes.add(t);
//			t=t.nextTime(increment);
//		}

		frame = new JFrame();
		frame.getContentPane().setBackground(bgColor);

		frame.setBounds(25, 25, 1200, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);


//		   Parameter input section

		  JPanel panelParameterInput = new JPanel();
		  panelParameterInput.setOpaque(false);
		  panelParameterInput.setBounds(10, 30, 384, 153);
		  frame.getContentPane().add(panelParameterInput);
		  GridBagLayout gbl_panelParameterInput = new GridBagLayout();
		  gbl_panelParameterInput.columnWidths = new int[]{0, 280, 0};
		  gbl_panelParameterInput.rowHeights = new int[]{0, 0, 0, 0, 0};
		  gbl_panelParameterInput.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		  gbl_panelParameterInput.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		  panelParameterInput.setLayout(gbl_panelParameterInput);

		  // ********* Max students **********

		  comboMaxStudents = new JComboBox<Integer>();
		  comboMaxStudents.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				maxStudents=(int)comboMaxStudents.getSelectedItem();
				formChanged=true;
			}
		});
		  GridBagConstraints gbc_comboIncrement = new GridBagConstraints();
		  gbc_comboIncrement.insets = new Insets(0, 0, 5, 5);
		  gbc_comboIncrement.gridx = 0;
		  gbc_comboIncrement.gridy = 0;
		  panelParameterInput.add(comboMaxStudents, gbc_comboIncrement);
		  comboMaxStudents.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {15,20,25,30,35}));
		  comboMaxStudents.setSelectedIndex(3);

		  JLabel lblScheduleInc = new JLabel("max # students per session");
		  GridBagConstraints gbc_lblScheduleInc = new GridBagConstraints();
		  gbc_lblScheduleInc.anchor = GridBagConstraints.WEST;
		  gbc_lblScheduleInc.insets = new Insets(0, 0, 5, 0);
		  gbc_lblScheduleInc.gridx = 1;
		  gbc_lblScheduleInc.gridy = 0;
		  panelParameterInput.add(lblScheduleInc, gbc_lblScheduleInc);

		  // ********* Block size **********

		  comboBlockSize = new JComboBox<Integer>();
		  comboBlockSize.addItemListener(new ItemListener() {
		  	public void itemStateChanged(ItemEvent ie) {
		  		blockSize=(int)comboBlockSize.getSelectedItem();
		  		formChanged=true;
		  	}
		  });
		  GridBagConstraints gbc_comboBlockSize = new GridBagConstraints();
		  gbc_comboBlockSize.insets = new Insets(0, 0, 5, 5);
		  gbc_comboBlockSize.gridx = 0;
		  gbc_comboBlockSize.gridy = 1;
		  panelParameterInput.add(comboBlockSize, gbc_comboBlockSize);
		  comboBlockSize.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {1,2,3,4,5}));
		  comboBlockSize.setSelectedIndex(2);


		  JLabel lblNumBocls = new JLabel("number of schedule blocks for session");
		  GridBagConstraints gbc_lblNumBocls = new GridBagConstraints();
		  gbc_lblNumBocls.anchor = GridBagConstraints.WEST;
		  gbc_lblNumBocls.insets = new Insets(0, 0, 5, 0);
		  gbc_lblNumBocls.gridx = 1;
		  gbc_lblNumBocls.gridy = 1;
		  panelParameterInput.add(lblNumBocls, gbc_lblNumBocls);

		  // ********* Max sessions **********

		  comboMaxSessions = new JComboBox<Integer>();
		  comboMaxSessions.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				maxSessions=(int)comboMaxSessions.getSelectedItem();
				formChanged=true;
			}
		});
		  GridBagConstraints gbc_comboMaxSessions = new GridBagConstraints();
		  gbc_comboMaxSessions.insets = new Insets(0, 0, 5, 5);
		  gbc_comboMaxSessions.gridx = 0;
		  gbc_comboMaxSessions.gridy = 2;
		  panelParameterInput.add(comboMaxSessions, gbc_comboMaxSessions);
		  comboMaxSessions.setModel(new DefaultComboBoxModel(new Integer[] {1,2,3,4,5,6,7}));
		  comboMaxSessions.setSelectedIndex(4);

		  JLabel lblMaxSessions = new JLabel("maximum # sessions to schedule per week");
		  GridBagConstraints gbc_lblMaxSessions = new GridBagConstraints();
		  gbc_lblMaxSessions.anchor = GridBagConstraints.WEST;
		  gbc_lblMaxSessions.insets = new Insets(0, 0, 5, 0);
		  gbc_lblMaxSessions.gridx = 1;
		  gbc_lblMaxSessions.gridy = 2;
		  panelParameterInput.add(lblMaxSessions, gbc_lblMaxSessions);


		  // ********* Min students **********

		  comboMinStudents = new JComboBox<Integer>();
		  comboMinStudents.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				minStudents=(int)comboMinStudents.getSelectedItem();
				formChanged=true;
			}
		});
		  GridBagConstraints gbc_comboMinStudents = new GridBagConstraints();
		  gbc_comboMinStudents.insets = new Insets(0, 0, 0, 5);
		  gbc_comboMinStudents.gridx = 0;
		  gbc_comboMinStudents.gridy = 3;
		  panelParameterInput.add(comboMinStudents, gbc_comboMinStudents);
		  comboMinStudents.setModel(new DefaultComboBoxModel(new Integer[] {1,2,3,4,5,6,7,8,9,10}));
		  comboMinStudents.setSelectedIndex(3);

		  JLabel lblMinStudents = new JLabel("minimum # students in a session");
		  GridBagConstraints gbc_lblMinStudents = new GridBagConstraints();
		  gbc_lblMinStudents.anchor = GridBagConstraints.WEST;
		  gbc_lblMinStudents.gridx = 1;
		  gbc_lblMinStudents.gridy = 3;
		  panelParameterInput.add(lblMinStudents, gbc_lblMinStudents);

		// ********* Run button **********
		btnRun.setBounds(110, 531, 133, 23);
		btnRun.addActionListener(this);
		frame.getContentPane().add(btnRun);
		btnRun.setEnabled(false); // not enabled until file chosen


		// ********* Reset button **********
		JButton btnResetForm = new JButton("Reset Form");
		btnResetForm.setBounds(110, 565, 133, 23);
		frame.getContentPane().add(btnResetForm);
		btnResetForm.addActionListener(this);

		// ********* Choose file button **********
		JButton btnChooseFile= new JButton("Choose file");
		btnChooseFile.setBounds(110, 497, 133, 23);
		frame.getContentPane().add(btnChooseFile);
		btnChooseFile.addActionListener(this);

		// ********* Save Button *****************
		btnSave.setBounds(1000,311,133,23);
		btnSave.addActionListener(this);
		frame.getContentPane().add(btnSave);
		btnSave.setEnabled(false); //not enabled till a solution chosen

		// ********* Sliders for weights **********

		panel = new JPanel();
		panel.setOpaque(false);
		//panel.setBounds(529, 54, 327, 244);
		panel.setBounds(29, 204, 327, 244);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNumSessions = new JLabel("#Sessions");
		lblNumSessions.setBounds(10, 7, 73, 14);
		panel.add(lblNumSessions);

		sldrSessions = new JSlider();
		sldrSessions.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				fewestSessionsWeight=(double)sldrSessions.getValue();
			}
		});

		sldrSessions.setOpaque(false);
		sldrSessions.setBounds(10, 32, 43, 200);
		panel.add(sldrSessions);
		sldrSessions.setOrientation(SwingConstants.VERTICAL);
		sldrSessions.setValue(5);
		sldrSessions.setMajorTickSpacing(1);
		sldrSessions.setSnapToTicks(true);
		sldrSessions.setPaintTicks(true);
		sldrSessions.setMaximum(10);
		sldrSessions.setPaintLabels(true);

		lblPreferred = new JLabel("Preferred");
		lblPreferred.setBounds(93, 7, 80, 14);
		panel.add(lblPreferred);

		sldrPrefTime = new JSlider();
		sldrPrefTime.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				preferredTimesWeight=(double)sldrPrefTime.getValue();
			}
		});
		sldrPrefTime.setOpaque(false);
		sldrPrefTime.setBounds(91, 32, 43, 200);
		panel.add(sldrPrefTime);
		sldrPrefTime.setValue(5);
		sldrPrefTime.setSnapToTicks(true);
		sldrPrefTime.setPaintTicks(true);
		sldrPrefTime.setPaintLabels(true);
		sldrPrefTime.setOrientation(SwingConstants.VERTICAL);
		sldrPrefTime.setMaximum(10);
		sldrPrefTime.setMajorTickSpacing(1);

		lblcan = new JLabel("#Can");
		lblcan.setBounds(183, 7, 64, 14);
		panel.add(lblcan);

		sldrCan = new JSlider();
		sldrCan.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				canComeWeight=(double)sldrCan.getValue();
			}
		});
		sldrCan.setOpaque(false);
		sldrCan.setBounds(177, 32, 43, 200);
		panel.add(sldrCan);
		sldrCan.setValue(5);
		sldrCan.setSnapToTicks(true);
		sldrCan.setPaintTicks(true);
		sldrCan.setPaintLabels(true);
		sldrCan.setOrientation(SwingConstants.VERTICAL);
		sldrCan.setMaximum(10);
		sldrCan.setMajorTickSpacing(1);

		lblmust = new JLabel("#Must");
		lblmust.setBounds(273, 7, 44, 14);
		panel.add(lblmust);

		sldrMust = new JSlider();
		sldrMust.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				mustComeWeight=(double)sldrMust.getValue();
			}
		});
		sldrMust.setOpaque(false);
		sldrMust.setBounds(261, 32, 43, 200);
		panel.add(sldrMust);
		sldrMust.setValue(5);
		sldrMust.setSnapToTicks(true);
		sldrMust.setPaintTicks(true);
		sldrMust.setPaintLabels(true);
		sldrMust.setOrientation(SwingConstants.VERTICAL);
		sldrMust.setMaximum(10);
		sldrMust.setMajorTickSpacing(1);

		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(370, 37, 11, 613);
		frame.getContentPane().add(separator);

//		 JPanel panel_3 = new JPanel();
//		 panel_3.setOpaque(false);
//		 panel_3.setBounds(7, 238, 418, 412);
//		 frame.getContentPane().add(panel_3);
//		 GridBagLayout gbl_panel_3 = new GridBagLayout();
//		 gbl_panel_3.columnWidths = new int[]{383, 0, 0};
//		 gbl_panel_3.rowHeights = new int[]{0, 0, 0, 0, 19, 0, 0, 0, 0, 0, 0, 0};
//		 gbl_panel_3.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
//		 gbl_panel_3.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
//		 panel_3.setLayout(gbl_panel_3);

		 // table here for selecting times/days
		 //table.getColumnModel().setColumnSelectionAllowed(true);
		 //table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//		 String[] weekdays={"Monday","Tuesday","Wednesday","Thursday","Friday"};
//		 Integer[][] staffSchedule = new Integer[19][5];
//		 String[][] strTimes=new String[20][1];
//		 int k=0;
//		 for (Time slotTime:possibleTimes){
//			 strTimes[k][0]=slotTime.toString();
//			 k++;
//		 }
//		 for (int i=0;i<5;i++){
//			 for (int j=0;j<19;j++){
//				 staffSchedule[j][i] = 1;
//			 }
//		 }
//		 JTable tblWeek = new JTable(staffSchedule,weekdays);
//		 JTable tblTimes = new JTable(strTimes,new String[]{""});
//		 JScrollPane jscrlp = new JScrollPane(tblWeek);
//		 jscrlp.setBounds(64, 238, 418, 327);
//		 tblTimes.setBounds(7, 250, 57, 400);
//		 //tblWeek.setValueAt(0, 0, 0);
//		 tblWeek.getColumnModel().setColumnSelectionAllowed(true);
//		 tblWeek.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//		 tblWeek.setPreferredScrollableViewportSize(new Dimension(400,175));
//		 tblTimes.setBackground(bgColor);
//		 tblTimes.setGridColor(bgColor);
//		 tblTimes.setEnabled(false);
//		 frame.getContentPane().add(jscrlp);
//		 frame.add(tblTimes);
//		 ListSelectionModel lsmRow = tblWeek.getSelectionModel();
//		 JLabel jlab = new JLabel();
//		 jlab.setBounds(7, 500, 100, 30);
//		 jlab.setText("test");
//		 lsmRow.addListSelectionListener(new ListSelectionListener()
//		 {
//			 public void valueChanged(ListSelectionEvent le){
//				 //tblWeek.changeSelection(rowIndex, columnIndex, toggle, extend);
//				 int[] str = tblWeek.getSelectedRows();
//				 int selCol=tblWeek.getSelectedColumn();
//				 String theString="";
//				 for (int i=0; i<str.length;i++){
//					 theString += str[i]+" ";
//					 if (!lsmRow.getValueIsAdjusting()){
//					 //tblWeek.changeSelection(str[i], selCol, false, false);
//					 if (staffSchedule[(str[i])][selCol]==0) staffSchedule[(str[i])][selCol]=1;
//					 else if (staffSchedule[(str[i])][selCol]==1)staffSchedule[(str[i])][selCol]=0;}
//					 tblWeek.setSelectionBackground(Color.RED);
//				 }
//
//				 jlab.setText(theString);
//			 }
//		 });
//		 frame.add(jlab);

		 //TODO add Done button

		// *********Results in table form ***********
		lblResultsTbl = new JLabel("Results:");
		lblResultsTbl.setBounds(440, 12, 94, 14);
		frame.getContentPane().add(lblResultsTbl);

		scrollPane2 = new JScrollPane();
		scrollPane2.setBounds(440, 42, 350, 579);
		solutionTable= new JTable();
		frame.getContentPane().add(scrollPane2);

		lblVarTbl = new JLabel("Variations:");
		lblVarTbl.setBounds(800, 12, 94, 14);
		frame.getContentPane().add(lblVarTbl);

		variationScrollPane= new JScrollPane();
		variationScrollPane.setBounds(800,42,330,250);
		variationTable = new JTable();
		frame.getContentPane().add(variationScrollPane);


		lblResults = new JLabel("Students for this solution:");
		lblResults.setBounds(800, 312, 150, 14);
		frame.getContentPane().add(lblResults);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(800, 342, 330, 279);
		frame.getContentPane().add(scrollPane);

		textSolutionOutput = new JTextArea();
		scrollPane.setViewportView(textSolutionOutput);
		textSolutionOutput.setAlignmentX(Component.RIGHT_ALIGNMENT);
		textSolutionOutput.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));


	}


	// ******* handle button events ********************

	public void actionPerformed(ActionEvent ae){
		// ******************  Run button ***************
		//TODO move reading of schedules to choose file button and error check so no crash when format wrong
		if (ae.getActionCommand().equals("Run")){

			//empty arrayLists for next run
			solutions.clear();
			textSolutionOutput.setText("");
			variationTable=null;
			variationScrollPane.setViewportView(variationTable);
			solutionTable=null;
			scrollPane2.setViewportView(solutionTable);

			// read schedules in from file
			schedules=readSchedulesFromFile(dataFile);
			if (schedules.size()==0){
				System.out.println("schedules array is empty");
				return;}
			Schedule s=schedules.get(0);
			int i=0;
			for (int slot:s.getSchedule()){if (slot==2)i++;}
			int[] numSlotsPerDay=new int[i+1];
			int j=0;
			i=0;
			for (int slot:s.getSchedule()){
				if (slot!=2) j++;
				else {
					numSlotsPerDay[i]=j;
					j=0;
					i++;
				}
			}
			numSlotsPerDay[i]=j;  //final entry in array

			Tree solutionTree=new Tree(null, new Session(-1));
			schedulesCopy=(ArrayList<Schedule>)schedules.clone();
			Scheduler.sortSchedules(schedulesCopy,(int)comboBlockSize.getSelectedItem());
			//textSolutionOutput.setText(schedulesCopy.toString());
			//String printString="Mask:\n";
			//for (int k=0;k<preferredMask.length;k++){printString+="\n"+preferredMask[k]+" "+times[k];}
			//textSolutionOutput.append(printString);
			int[] possibleMask=new int[baseMask.length];
			do {
				Scheduler.createTree(schedulesCopy, solutionTree,possibleMask,minStudents,schedSize,blockSize,maxStudents);
				if (!solutionTree.hasLeaves()) {
					JOptionPane.showMessageDialog(null, "Impossible schedule removed. \nThere are no possible solutions containing \nthis schedule for given parameters.\n> "+schedulesCopy.get(0).getName(),"Impossible Schedule", JOptionPane.WARNING_MESSAGE);
					//textSolutionOutput.append("Impossible schedule, "+schedulesCopy.get(0).getName()+" removed");
					schedulesCopy.remove(0);
				}
			}		while (!solutionTree.hasLeaves());
			//Tree.printSolutionToFile(solutionTree, 0,"solution.txt");
			if (!(solutionTree==null)) solutionTree.pruneTree();
//TODO null pointer exception in create solutions..
			createSolutions(solutionTree, new ArrayList<Session>());
			///Scheduler.createSolutions(solutionTree, new ArrayList<Session>());
			//textSolutionOutput.append(fewestSessionsWeight+" "+preferredTimesWeight+" "+canComeWeight+" "+mustComeWeight);
			for (Solution sol:solutions){
				sol.calculateNormalizedRank(fewestSessionsWeight, preferredTimesWeight, canComeWeight, mustComeWeight,solutions);
				//textSolutionOutput.append(sol.getCanBalance()+"");
			}
			Collections.sort(solutions);
			//printSolutionsToOutputWindow(solutions);
			distinctSolutions = new ArrayList<Solution>();
			// find similar solutions
			for (Solution baseSolution:solutions){
				for (Solution testSolution:solutions){
					if (!baseSolution.isSame(testSolution)){
						if (baseSolution.isSimilar(testSolution)){
							baseSolution.addSimilarSolution(testSolution);
							//distinctSolutions.remove(testSolution);
						}
					}
				}
			}

			ArrayList<Solution> solutionsCopy = (ArrayList<Solution>)solutions.clone();
			int index=0;
			while (solutionsCopy.size()>0){
				Solution sol=solutionsCopy.get(index);
				//for (Solution s:sol.getSimilarSolutions()){
				solutionsCopy.removeAll(sol.getSimilarSolutions());
				solutionsCopy.remove(sol);
				distinctSolutions.add(sol);
				//index++;
				//}
			}
			//printSolutionsToOutputWindow(distinctSolutions);
			//if (distinctSolutions.size()!=0) printSingleSolutionToOutput(distinctSolutions.get(0));
			printSolutionsToTable(distinctSolutions);

		}
		else if (ae.getActionCommand().equals("Reset Form")){
//			chckbxMonday.setSelected(false);
//			chckbxTuesday.setSelected(false);
//			chckbxWednesday.setSelected(false);
//			chckbxThursday.setSelected(false);
//			chckbxFriday.setSelected(false);
			sldrCan.setValue(5);
			sldrMust.setValue(5);
			sldrPrefTime.setValue(5);
			sldrSessions.setValue(5);
			comboBlockSize.setSelectedIndex(2);
			comboMaxStudents.setSelectedIndex(3);
			comboMaxSessions.setSelectedIndex(4);
			comboMinStudents.setSelectedIndex(3);
			//textSolutionOutput.setText("number of days checked: 0");
			lblFileChosen.setText("");
			btnRun.setEnabled(false);
			btnSave.setEnabled(false);
			textSolutionOutput.setText("");
			variationTable=null;
			variationScrollPane.setViewportView(variationTable);
			solutionTable=null;
			scrollPane2.setViewportView(solutionTable);


		}

		else if (ae.getActionCommand().equals("Choose file")){

			boolean okFile=false;
			int option = 0;
			while (option != JFileChooser.CANCEL_OPTION && !okFile){
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
				        "CSV files", "csv");
				chooser.setFileFilter(filter); // filter to show only .csv files
				option = chooser.showOpenDialog(frame);
				if(option == JFileChooser.APPROVE_OPTION){
					//String folder = (String)chooser.getSelectedFile( ).getAbsolutePath();
					dataFile = chooser.getSelectedFile();
					//make sure user chooses a .csv file since they can change the filter
					if (dataFile.getName().endsWith(".csv")){
						btnRun.setEnabled(true);
						frame.getContentPane().add(lblFileChosen);
						lblFileChosen.setBounds(110, 630, 275, 23);
						lblFileChosen.setText("File: "+dataFile.getName());
						//fileChosen.setVisible(true);
						okFile =true;
					}
					else
					{
						//open dialog box that tells user to choose a .csv file
						JOptionPane.showMessageDialog(null, "Please choose a .csv file");
						okFile =false;
					}
				}
			}
		}
		else if (ae.getActionCommand().equals("Save")){
			int option=0;
			boolean saved=false;
			File saveFile;
			JFileChooser saveChooser =new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "CSV files", "csv");
			saveChooser.setFileFilter(filter);
			while (option != JFileChooser.CANCEL_OPTION && !saved){
				option = saveChooser.showSaveDialog(frame);
				if (option == JFileChooser.APPROVE_OPTION){
					saveFile = saveChooser.getSelectedFile();

					if (!saveFile.getName().endsWith(".csv")) saveFile=new File(saveFile.getPath()+".csv");
					if (!saveFile.exists()){
						printSolutionToFile(saveFile.getPath(),selectedSolution);
						saved=true;
						}
					else {
						int action = JOptionPane.showConfirmDialog(null, "File "+saveFile.getName()+" already exists. Overwrite?");
						if (action==JOptionPane.OK_OPTION){
							printSolutionToFile(saveFile.getPath(),selectedSolution);
							saved=true;
						}
						else saved=false;
					}
				}
			}
			//printSolutionToFile("solution.csv",selectedSolution);
		}
	}

	public void itemStateChanged(ItemEvent ie){

	}
	//Code for checkboxes for starting and ending times for each day
	@Override
	public void stateChanged(ChangeEvent ce) {

	}

	public void createSolutions(Tree t,ArrayList<Session> sessionList){
		if (t.isEnd) {
			boolean goodSolution=true;
			Solution solution=new Solution();
			ArrayList<Session> sessionListClone = new ArrayList<Session>();
			//ArrayList<Session> sessionListClone = (ArrayList<Session>) sessionList.clone();
			for (Session s:sessionList){
				sessionListClone.add(new Session(s.time,s.preferred));
			}
			solution.setSessions(sessionListClone);
			solution.findAllMembers(schedulesCopy, blockSize);
			for (Session s :solution.getSessions()){ //check parameters to make sure solution satisfies
				if (s.members.size()<minStudents)goodSolution=false;
				else if (solution.getSessions().size()>maxSessions)goodSolution=false;
			}
			if (goodSolution) solutions.add(solution);
			}
		else for (Tree leaf:t.leaves){
			int time=leaf.session.time;
			boolean preferred=true;
			for (int i=0;i<blockSize;i++){
				if (preferredMask[time+i]==1) preferred=false;
			}
			Session s=new Session(time,preferred);
			ArrayList<Session> sessionListCopy=(ArrayList<Session>)sessionList.clone();
			sessionListCopy.add(s);
			createSolutions(leaf, sessionListCopy);

		}
	}

	public void printSingleSolutionToOutput(Solution sol){
		//sol.findAllMembers(schedules, blockSize);
		textSolutionOutput.setText("");
		ArrayList<Session> sessions= sol.getSessions();
		for (Session s:sessions){
			textSolutionOutput.append(times[s.time]+":\n");
			textSolutionOutput.append(s.toString());
		}
	}

	public String getCsvSingleSolution(Solution sol){
		ArrayList<Session> sessions = sol.getSessions();
		String result = "";
		for (Session sess:sessions){
			ArrayList<Schedule> membersCanAttend = sess.members;
			result+=times[sess.time]+":\n";
			for (Schedule s:membersCanAttend){
				if (sess.membersMustAttend.contains(s)) result+="*";
				result+=s.getName()+","+s.getEmail()+"\n";
			}
			result+="\n";

		}
		return result;
	}

	public void printSolutionToFile(String filename, Solution sol){
		//TODO use dialog box to choose file to save to
		PrintWriter writer;
		try {
			writer = new PrintWriter(filename, "UTF-8");
			writer.println(getCsvSingleSolution(sol));
			writer.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File not found. Could not print to file.","Error",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			JOptionPane.showMessageDialog(null, "Unsupported encoding exception. Could not print to file.","Error",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	public void printSolutionsToOutputWindow(ArrayList<Solution> solutions){
		//limit number of solutions to print by maxSolutionsToPrint or all if smaller

		for (int i=0;i<maxSolutionsToPrint && i<solutions.size();i++){
				textSolutionOutput.append("\n");
				Solution sol=solutions.get(i);
				textSolutionOutput.append("*******Solution "+i+"********\n");
				textSolutionOutput.append(""+sol.getRank()+"\n");
				Collections.sort(sol.getSessions());
				for (Session session:sol.getSessions()){
					int index=session.time;
					textSolutionOutput.append("\t"+times[index]+"\n");;
				}

				ArrayList<Solution> similar = sol.getSimilarSolutions();
				for (int j=0;j<similar.size();j++){
					Solution similarSol=similar.get(j);
					textSolutionOutput.append("Variation "+j+"\n");
					Collections.sort(similarSol.getSessions());
					for (Session session:similarSol.getSessions()){
						int index=session.time;
						textSolutionOutput.append(times[index]+"\n");
					}
					textSolutionOutput.append("\n");
				}
		}
	}



	public void printSolutionsToTable(ArrayList<Solution> solutions){
		if (solutions.size()==0) {
			textSolutionOutput.append("No solutions found");
			//TODO make this a dialog box for no solutions found
			return;
		}
		String printString="";
		String[][] solutionArray;
		if (solutions.size()<maxSolutionsToPrint) solutionArray= new String[solutions.size()][2];
		else solutionArray=new String[maxSolutionsToPrint][2];
		//DefaultListModel listModel;
	    MultiLineTableCellRenderer renderer = new MultiLineTableCellRenderer();

		//JList<String> list;
		for (int i=0;i<maxSolutionsToPrint && i<solutions.size();i++){
			printString="";
			//listModel= new DefaultListModel();
			Solution sol=solutions.get(i);
			solutionArray[i][0]="Solution "+i;
			//textSolutionOutput.append(""+sol.getRank()+"\n");
			Collections.sort(sol.getSessions());
			for (Session session:sol.getSessions()){
				int index=session.time;
				printString+=times[index]+",";
				//listModel.addElement("times[index]");
			}
			solutionArray[i][1]=printString;
			//list = new JList(listModel);
			//solutionTable.getColumnModel().getColumn(1).
		}

		solutionTable = new JTable(solutionArray,new String[]{"",""});
	    solutionTable.getColumnModel().getColumn(1).setCellRenderer(renderer);
	    int height = (solutionTable.getFont().getSize()+8)*maxSessions;
	    solutionTable.setRowHeight(height);

	    solutionTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent le){
				//textSolutionOutput.setText("");
				int row = solutionTable.getSelectedRow();
				 if (!solutionTable.getSelectionModel().getValueIsAdjusting()){
					//printSingleSolutionToOutput(solutions.get(row));
					 ArrayList<Solution> variations = solutions.get(row).getSimilarSolutions();
					 variations.add(0, solutions.get(row));
					 printVariationsToTable(variations);
					 }
			}
		});
	    scrollPane2.setViewportView(solutionTable);
	}

	public void printVariationsToTable(ArrayList<Solution> variations){
		String printString="";
		String[][] variationArray;
		if (variations.size()<maxSolutionsToPrint) variationArray= new String[variations.size()][2];
		else variationArray=new String[maxSolutionsToPrint][2];
	    MultiLineTableCellRenderer renderer = new MultiLineTableCellRenderer();

		for (int i=0;i<maxSolutionsToPrint && i<variations.size();i++){
			printString="";
			Solution sol=variations.get(i);
			variationArray[i][0]="Variation "+i;
			//textSolutionOutput.append(""+sol.getRank()+"\n");
			Collections.sort(sol.getSessions());
			for (Session session:sol.getSessions()){
				int index=session.time;
				printString+=times[index]+",";
			}
			variationArray[i][1]=printString;
		}

		variationTable = new JTable(variationArray,new String[]{"",""});
	    variationTable.getColumnModel().getColumn(1).setCellRenderer(renderer);
	    int height = (variationTable.getFont().getSize()+8)*maxSessions;
	    variationTable.setRowHeight(height);

	    variationTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent le){
				//textSolutionOutput.setText("");
				int row = variationTable.getSelectedRow();
				 if (!variationTable.getSelectionModel().getValueIsAdjusting()){
					selectedSolution = variations.get(row);
					printSingleSolutionToOutput(selectedSolution);
					btnSave.setEnabled(true);
					 }
			}
		});
		variationScrollPane.setViewportView(variationTable);
	}


	public void openSolutionsWindow(Solution sol){
//		JButton myButton = new JButton("Open new window");
//		myButton.setBounds(10, 300, 150, 30);
//		frame.add(myButton);
//		JFrame newFrame = new JFrame("New Window");
//		newFrame.setSize(500, 500);
//		newFrame.getContentPane().setBackground(bgColor);
//		newFrame.setLayout(new BorderLayout());
////		JPanel newPanel = new JPanel();
////		newPanel.setBounds(10,10,300,300);
////		newPanel.setOpaque(false);
////		newFrame.getContentPane().add(newPanel);
//
//		JScrollPane ascrollPane = new JScrollPane();
//		ascrollPane.setBounds(10, 10, 300, 300);
//		newFrame.getContentPane().add(ascrollPane);
//		String[][] numbers= new String[][]{{"0"},{"1"},{"2"},{"3"}};
//		JTable table = new JTable(numbers,new String[]{""});
//		table.setBounds(10, 10, 300, 300);
//		//JTable tblTimes = new JTable(strTimes,new String[]{""});
//
//		JTextArea output = new JTextArea();
//		output.setBounds(10, 10, 100, 100);
//		output.setText("Test");
//		//newFrame.getContentPane().add(output);
//		ascrollPane.setViewportView(table);
//
//		//add this line of code
//		myButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				// open a new frame i.e window
//				//newFrame.pack();
//
//				newFrame.setVisible(true);
//			}
//		});
	}

	/**
	 * gets student schedules from a csv file chosen by user.
	 * uses file to determine number of days, increment, preferredMask, & times array
	 * @param csvFile file to read schedules from. Should have a header row and include name, email, days and times
	 * @return returns student schedules in an ArrayList of Schedules
	 */
	public ArrayList<Schedule> readSchedulesFromFile (File csvFile){
		//TODO make sure only read in and count columns with schedule data - stop when no more times in header
		 ArrayList<Schedule> schedules=new ArrayList<Schedule>();
		    boolean foundHeaders=false;
		    int nameColumn=-1, emailColumn=-1;
		    String[] headerRow = null;
		    int[] dayIndices = new int[5];
		 try{
			 Scanner scanner = new Scanner(csvFile);
			 while (scanner.hasNextLine()){
				 //TODO learn how to check for empty data
				String line = scanner.nextLine();
				//String dayName ="";
				//if headers found & columns known, go ahead and read data in
				if(foundHeaders && nameColumn!=-1 &&emailColumn!=-1 && dayIndices!=null){
					//System.out.println("reading in schedules "+nameColumn);
					//System.out.println(line);
				    String[] fields = line.split(",");
					//System.out.println(fields.length);
					if (fields.length==0) continue;
					//if (fields[nameColumn].isEmpty()) continue; //if no name then skip this row
					if (fields[nameColumn].isEmpty()) continue; //if no name then skip this row

					int arraySize=headerRow.length;
				    //schedSize=arraySize-dayIndices[0]+1; //size of line read in minus index where data starts
				    int[] slots=new int[schedSize];
				    int value;
				    int slot=0;
				   // System.out.println("schedule size "+schedSize);
				   // System.out.println("fields length "+fields.length);
				    //System.out.println("headers length "+headerRow.length);
				   // if (fields.length<headerRow.length||fields[dayIndices[0]+1].isEmpty()){
				    boolean badSchedule = false;
				    int j=dayIndices[0];
				    while (badSchedule==false && j<arraySize){
				    	if (fields[j].equals("")) {
				    		badSchedule=true;
				    	}
				    	j++;
				    }
				    if (fields.length<headerRow.length || badSchedule){
				    	JOptionPane.showMessageDialog(null, "Error in schedule for: "+ fields[nameColumn]+
			   					 ". Empty cells. \n Ignoring for this run.","Message",JOptionPane.WARNING_MESSAGE);
				    	//System.out.println("bad schedule for: "+fields[nameColumn]);
				    }
				    else{
				    	for (int i=dayIndices[0];i<arraySize;i++){
				    		if (fields[i].equals("")) {
				    			value=0;
				   			 	JOptionPane.showMessageDialog(null, "Error in schedule for: "+ fields[nameColumn]+
				   					 ". Empty cell at "+i+" replaced with '0'.","Message",JOptionPane.WARNING_MESSAGE);

				    		}
				    		else value=Integer.parseInt(fields[i]);
					    	if (value==0 || value==1 || value==2) slots[slot]=value;
					    	slot++;

					    }
				        //scanner.useDelimiter(",");
				        //create the schedule
				        Schedule studentSchedule=new SimpleSchedule(fields[nameColumn], fields[emailColumn], schedSize);
				        studentSchedule.setSchedule(slots);
				        studentSchedule.determineRank(BibleStudySchedulerWindow.blockSize);
				        //add schedule to list
				        schedules.add(studentSchedule);
				    }
				}
				else if (!foundHeaders){
					if (line.contains("name")&&line.contains("email")) {
						//System.out.println("reading in headers");
						foundHeaders = true;
						headerRow = line.split(",");
						for (int x=0; x<headerRow.length;x++){
							if (headerRow[x].equalsIgnoreCase("name")) nameColumn=x;
							else if (headerRow[x].equalsIgnoreCase("email")) emailColumn=x;
						}
						int i =0;
						int j=0;
						schedSize=0;
						boolean dayFound=false;
						while (i<headerRow.length){
							if (headerRow[i].contains("day")) {
								dayFound=true;
								dayIndices[j]=i;
								j++;
								schedSize++;
							}
							if (dayFound && headerRow[i].contains(":")){
								schedSize++;
							}
							i++;
						}
					}
				}
			 }
			 if (foundHeaders) {
				 int k=0;
				 //schedSize=headerRow.length-dayIndices[0]+1;
				 baseMask = new int[schedSize];
				 times = new String[schedSize];
				 String day="";

				 //for (int i=dayIndices[0];i<headerRow.length;i++){
				 for (int i=dayIndices[0];i<dayIndices[0]+schedSize;i++){
					 System.out.println(headerRow.length+" SchedSize "+schedSize+"k "+k+" i "+i);
					 times[k]=headerRow[i];
					 if (headerRow[i].contains("day")){ baseMask[k]=2; times[k]="********"; day=headerRow[i];}
					 else {baseMask[k]=0; times[k]=day+" "+headerRow[i];}
					 //System.out.println(times[k]);
					 k++;
				 }
				 Time startTime=new Time(headerRow[dayIndices[0]+1]);
				 Time nextTime = new Time(headerRow[dayIndices[0]+2]);
				 increment = startTime.timeDifference(nextTime);
				 //System.out.println("increment: "+increment);
				 initPreferredMask();
			 }
	         scanner.close();
	         return schedules;
		 }
		 catch (FileNotFoundException e){
			 JOptionPane.showMessageDialog(null, "File not found. Please choose a different file.","Error",JOptionPane.ERROR_MESSAGE);
		 }
		 return null;
	}

	public void handleBadFile(String error){
		JOptionPane.showMessageDialog(null, "Problem with file "+dataFile+":"+error+" Please correct or choose a new file.","Error",JOptionPane.ERROR_MESSAGE);
		lblFileChosen.setText("");
	}



	public void initPreferredMask(){
		preferredMask=baseMask.clone();
		int length=preferredMask.length;
		for (int i=2;i<length;i++){
			if (preferredMask[i]==2){
				preferredMask[i-2]=1;
				preferredMask[i-1]=1;
				preferredMask[i+1]=1;
				preferredMask[i+2]=1;
			}
		}
		preferredMask[length-1]=1;
		preferredMask[length-2]=1;
	}

	public class MultiLineTableCellRenderer extends JList<String> implements TableCellRenderer {

	    /**
		 *
		 */
		private static final long serialVersionUID = 1L;

		@Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	        //make multi line where the cell value is String[]
//	        if (value instanceof String[]) {
//	            setListData((String[]) value);
//	        }
			if (value instanceof String){
				String[] data = ((String)value).split(",");
				setListData(data);
			}

	        //cell backgroud color when selected
	        if (isSelected) {
	            setBackground(UIManager.getColor("Table.selectionBackground"));
	        } else {
	            setBackground(UIManager.getColor("Table.background"));
	        }

	        return this;
	    }
	}

}

