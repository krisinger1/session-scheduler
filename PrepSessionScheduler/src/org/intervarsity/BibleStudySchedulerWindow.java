package org.intervarsity;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.JSlider;
import java.awt.Cursor;
import java.awt.Component;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JSeparator;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JScrollPane;
import java.awt.Dimension;
//TODO print sorted schedules to a csv file?
//TODO print solutions to file
//TODO how to use Git with eclipse?
public class BibleStudySchedulerWindow implements ActionListener,ItemListener,ChangeListener{
	private ArrayList<Solution> solutions=new ArrayList<Solution>();
	private String[] times;
	private ArrayList<Schedule> schedules;
	public static int schedSize;
	private ArrayList<Time> possibleTimes=new ArrayList<Time>();
	private int[] preferredMask;
	private JFrame frame;
	private JCheckBox chckbxMonday,chckbxTuesday,chckbxWednesday,chckbxThursday,chckbxFriday;
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
	private JPanel panel,panelTuesdayTimes,panelMonTimes,panelWedTimes,panelThursTimes,panelFriTimes;
	private JComboBox<Integer> comboMinStudents, comboMaxSessions,comboIncrement,comboBlockSize;
	private JSpinner spnrMonStartHr,spnrMonPrefStartHr,spnrMonPrefEndHr,
			spnrTueStartHr,spnrTuePrefStartHr,spnrTuePrefEndHr,
			spnrWedStartHr,spnrWedPrefStartHr,spnrWedPrefEndHr,
			spnrThursStartHr,spnrThursPrefStartHr,spnrThursPrefEndHr,
			spnrFriStartHr,spnrFriPrefStartHr,spnrFriPrefEndHr;
	private JScrollPane scrollPane;
	public static int blockSize;
	private int maxSessions;
	//TODO why is minStudents never used?
	private int minStudents;
	//TODO fix code to not allow more than maxStudents per session to force more solutions - 
	// don't want 30+ students in a session
	private int maxStudents = 20;
	//TODO cap off number of solutions to print - especially if printing students names
	private int maxSolutionsToPrint; 
	private int increment;
	private boolean formChanged=false;

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
		Time t=new Time(8, 0);
		for (int i=1;i<21;i++){
			possibleTimes.add(t);
			t=t.nextTime(30);
		}
		
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(176, 196, 222));
		frame.setBounds(25, 25, 1200, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblWhichDaysDo = new JLabel("Enter days and start times for student schedules:");
		lblWhichDaysDo.setVerticalAlignment(SwingConstants.TOP);
		lblWhichDaysDo.setBounds(10, 211, 359, 26);
		frame.getContentPane().add(lblWhichDaysDo);
		
		JButton btnDone = new JButton("Run");
		btnDone.setBounds(723, 331, 133, 23);
		btnDone.addActionListener(this);
		frame.getContentPane().add(btnDone);
		
		JButton btnResetForm = new JButton("Reset Form");
		btnResetForm.setBounds(723, 365, 133, 23);
		frame.getContentPane().add(btnResetForm);
		btnResetForm.addActionListener(this);
		
		lblResults = new JLabel("Results:");
		lblResults.setBounds(894, 12, 94, 14);
		frame.getContentPane().add(lblResults);
		
		panel = new JPanel();
		panel.setOpaque(false);
		panel.setBounds(529, 54, 327, 244);
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
		separator.setBounds(521, 37, 11, 613);
		frame.getContentPane().add(separator);
		 
		 JPanel panel_3 = new JPanel();
		 panel_3.setOpaque(false);
		 panel_3.setBounds(7, 238, 418, 412);
		 frame.getContentPane().add(panel_3);
		 GridBagLayout gbl_panel_3 = new GridBagLayout();
		 gbl_panel_3.columnWidths = new int[]{383, 0, 0};
		 gbl_panel_3.rowHeights = new int[]{0, 0, 0, 0, 19, 0, 0, 0, 0, 0, 0, 0};
		 gbl_panel_3.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		 gbl_panel_3.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		 panel_3.setLayout(gbl_panel_3);

//		MONDAY
		 
		 chckbxMonday = new JCheckBox("Monday");
		 chckbxMonday.setOpaque(false);
		 GridBagConstraints gbc_chckbxMonday = new GridBagConstraints();
		 gbc_chckbxMonday.anchor = GridBagConstraints.WEST;
		 gbc_chckbxMonday.insets = new Insets(0, 0, 5, 5);
		 gbc_chckbxMonday.gridx = 0;
		 gbc_chckbxMonday.gridy = 0;
		 panel_3.add(chckbxMonday, gbc_chckbxMonday);
		 chckbxMonday.addItemListener(this);
		 
		 panelMonTimes = new JPanel();
		 panelMonTimes.setOpaque(false);
		  GridBagConstraints gbc_panelMonTimes = new GridBagConstraints();
		  gbc_panelMonTimes.gridwidth = 2;
		  gbc_panelMonTimes.insets = new Insets(0, 0, 5, 0);
		  gbc_panelMonTimes.gridx = 0;
		  gbc_panelMonTimes.gridy = 1;
		  panel_3.add(panelMonTimes, gbc_panelMonTimes);
		  GridBagLayout gbl_panelMonTimes = new GridBagLayout();
		  gbl_panelMonTimes.columnWidths = new int[]{43, 90, 23, 0, 87, 0, 0};
		  gbl_panelMonTimes.rowHeights = new int[]{0, 0, 0};
		  gbl_panelMonTimes.columnWeights = new double[]{0.0, 64.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		  gbl_panelMonTimes.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		  panelMonTimes.setLayout(gbl_panelMonTimes);
		  
		  JLabel label = new JLabel("Start time");
		  GridBagConstraints gbc_label = new GridBagConstraints();
		  gbc_label.anchor = GridBagConstraints.EAST;
		  gbc_label.insets = new Insets(0, 0, 5, 5);
		  gbc_label.gridx = 0;
		  gbc_label.gridy = 0;
		  panelMonTimes.add(label, gbc_label);
		  
		  spnrMonStartHr = new JSpinner();
		  spnrMonStartHr.setPreferredSize(new Dimension(75, 20));
		  spnrMonStartHr.setModel(new SpinnerListModel(possibleTimes));
		  spnrMonStartHr.setValue(possibleTimes.get(2));
		  //spnrMonStartHr.setModel(new SpinnerNumberModel(9, 1, 12, 1));
		  GridBagConstraints gbc_spnrMonStartHr = new GridBagConstraints();
		  gbc_spnrMonStartHr.insets = new Insets(0, 0, 5, 5);
		  gbc_spnrMonStartHr.gridx = 1;
		  gbc_spnrMonStartHr.gridy = 0;
		  panelMonTimes.add(spnrMonStartHr, gbc_spnrMonStartHr);
		  spnrMonStartHr.addChangeListener(this);
		  
		  JLabel lblPrefStartTime = new JLabel("Pref Start Time");
		  GridBagConstraints gbc_lblPrefStartTime = new GridBagConstraints();
		  gbc_lblPrefStartTime.anchor = GridBagConstraints.EAST;
		  gbc_lblPrefStartTime.gridwidth = 2;
		  gbc_lblPrefStartTime.insets = new Insets(0, 0, 5, 5);
		  gbc_lblPrefStartTime.gridx = 2;
		  gbc_lblPrefStartTime.gridy = 0;
		  panelMonTimes.add(lblPrefStartTime, gbc_lblPrefStartTime);
		  
		  spnrMonPrefStartHr = new JSpinner();
		  spnrMonPrefStartHr.setPreferredSize(new Dimension(75, 20));
		  spnrMonPrefStartHr.setModel(new SpinnerListModel(possibleTimes));
		  spnrMonPrefStartHr.setValue(possibleTimes.get(2));
		  //spnrMonPrefStartHr.setModel(new SpinnerNumberModel(9, 1, 12, 1));
		  GridBagConstraints gbc_spnrMonPrefStartHr = new GridBagConstraints();
		  gbc_spnrMonPrefStartHr.insets = new Insets(0, 0, 5, 5);
		  gbc_spnrMonPrefStartHr.gridx = 4;
		  gbc_spnrMonPrefStartHr.gridy = 0;
		  panelMonTimes.add(spnrMonPrefStartHr, gbc_spnrMonPrefStartHr);
		  spnrMonPrefStartHr.addChangeListener(this);
		 	  
		  JLabel lblPrefEndTime = new JLabel("Pref End Time");
		  GridBagConstraints gbc_lblPrefEndTime = new GridBagConstraints();
		  gbc_lblPrefEndTime.anchor = GridBagConstraints.EAST;
		  gbc_lblPrefEndTime.insets = new Insets(0, 0, 0, 5);
		  gbc_lblPrefEndTime.gridx = 3;
		  gbc_lblPrefEndTime.gridy = 1;
		  panelMonTimes.add(lblPrefEndTime, gbc_lblPrefEndTime);
		  
		  spnrMonPrefEndHr = new JSpinner();
		  spnrMonPrefEndHr.setPreferredSize(new Dimension(75, 20));
		  //spnrMonPrefEndHr.setModel(new SpinnerNumberModel(9, 1, 12, 1));
		  spnrMonPrefEndHr.setModel(new SpinnerListModel(possibleTimes));
		  spnrMonPrefEndHr.setValue(possibleTimes.get(possibleTimes.size()-1));
		  GridBagConstraints gbc_spnrMonPrefEndHr = new GridBagConstraints();
		  gbc_spnrMonPrefEndHr.insets = new Insets(0, 0, 0, 5);
		  gbc_spnrMonPrefEndHr.gridx = 4;
		  gbc_spnrMonPrefEndHr.gridy = 1;
		  panelMonTimes.add(spnrMonPrefEndHr, gbc_spnrMonPrefEndHr);
		  //spnrMonPrefEndHr.setValue(5);
		  spnrMonPrefEndHr.addChangeListener(this);
	
		  panelMonTimes.setVisible(false);
//		  
//		TUESDAY		
		 
		  chckbxTuesday = new JCheckBox("Tuesday");
		  chckbxTuesday.setOpaque(false);
		  GridBagConstraints gbc_chckbxTuesday = new GridBagConstraints();
		  gbc_chckbxTuesday.anchor = GridBagConstraints.WEST;
		  gbc_chckbxTuesday.insets = new Insets(0, 0, 5, 5);
		  gbc_chckbxTuesday.gridx = 0;
		  gbc_chckbxTuesday.gridy = 2;
		  panel_3.add(chckbxTuesday, gbc_chckbxTuesday);
		  chckbxTuesday.addItemListener(this);
		  
		  panelTuesdayTimes = new JPanel();
		  panelTuesdayTimes.setOpaque(false);
		  GridBagConstraints gbc_panelTuesdayTimes = new GridBagConstraints();
		  gbc_panelTuesdayTimes.gridwidth = 2;
		  gbc_panelTuesdayTimes.insets = new Insets(0, 0, 5, 0);
		  gbc_panelTuesdayTimes.gridx = 0;
		  gbc_panelTuesdayTimes.gridy = 3;
		  panel_3.add(panelTuesdayTimes, gbc_panelTuesdayTimes);
		  GridBagLayout gbl_panelTuesdayTimes = new GridBagLayout();
		  gbl_panelTuesdayTimes.columnWidths = new int[]{28, 0, 0, 0, 23, 0, 0, 0, 0, 0};
		  gbl_panelTuesdayTimes.rowHeights = new int[]{0, 0, 0};
		  gbl_panelTuesdayTimes.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		  gbl_panelTuesdayTimes.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		  panelTuesdayTimes.setLayout(gbl_panelTuesdayTimes);
		  
		  JLabel lblTueStart = new JLabel("Start time");
		  GridBagConstraints gbc_lblTueStart = new GridBagConstraints();
		  gbc_lblTueStart.anchor = GridBagConstraints.EAST;
		  gbc_lblTueStart.insets = new Insets(0, 0, 5, 5);
		  gbc_lblTueStart.gridx = 0;
		  gbc_lblTueStart.gridy = 0;
		  panelTuesdayTimes.add(lblTueStart, gbc_lblTueStart);
		  
		  spnrTueStartHr = new JSpinner();
		  spnrTueStartHr.setPreferredSize(new Dimension(75, 20));
		  //spnrTueStartHr.setModel(new SpinnerNumberModel(9, 1, 12, 1));
		  spnrTueStartHr.setModel(new SpinnerListModel(possibleTimes));
		  spnrTueStartHr.setValue(possibleTimes.get(2));
		  GridBagConstraints gbc_spnrTueStartHr = new GridBagConstraints();
		  gbc_spnrTueStartHr.insets = new Insets(0, 0, 5, 5);
		  gbc_spnrTueStartHr.gridx = 1;
		  gbc_spnrTueStartHr.gridy = 0;
		  panelTuesdayTimes.add(spnrTueStartHr, gbc_spnrTueStartHr);
		  spnrTueStartHr.addChangeListener(this);
		  
		  JLabel lblTuePrefStartTime = new JLabel("Pref Start Time");
		  GridBagConstraints gbc_lblTuePrefStartTime = new GridBagConstraints();
		  gbc_lblTuePrefStartTime.anchor = GridBagConstraints.EAST;
		  gbc_lblTuePrefStartTime.gridwidth = 2;
		  gbc_lblTuePrefStartTime.insets = new Insets(0, 0, 5, 5);
		  gbc_lblTuePrefStartTime.gridx = 4;
		  gbc_lblTuePrefStartTime.gridy = 0;
		  panelTuesdayTimes.add(lblTuePrefStartTime, gbc_lblTuePrefStartTime);
		  
		  spnrTuePrefStartHr = new JSpinner();
		  spnrTuePrefStartHr.setPreferredSize(new Dimension(75, 20));
		  spnrTuePrefStartHr.setModel(new SpinnerListModel(possibleTimes));
		  spnrTuePrefStartHr.setValue(possibleTimes.get(2));
		  //spnrTuePrefStartHr.setModel(new SpinnerNumberModel(9, 1, 12, 1));
		  GridBagConstraints gbc_spnrTuePrefStartHr = new GridBagConstraints();
		  gbc_spnrTuePrefStartHr.insets = new Insets(0, 0, 5, 5);
		  gbc_spnrTuePrefStartHr.gridx = 6;
		  gbc_spnrTuePrefStartHr.gridy = 0;
		  panelTuesdayTimes.add(spnrTuePrefStartHr, gbc_spnrTuePrefStartHr);
		  spnrTuePrefStartHr.addChangeListener(this);
		  
		  JLabel lblTuePrefEndTime = new JLabel("Pref End Time");
		  GridBagConstraints gbc_lblTuePrefEndTime = new GridBagConstraints();
		  gbc_lblTuePrefEndTime.anchor = GridBagConstraints.EAST;
		  gbc_lblTuePrefEndTime.insets = new Insets(0, 0, 0, 5);
		  gbc_lblTuePrefEndTime.gridx = 5;
		  gbc_lblTuePrefEndTime.gridy = 1;
		  panelTuesdayTimes.add(lblTuePrefEndTime, gbc_lblTuePrefEndTime);
		  
		  spnrTuePrefEndHr = new JSpinner();
		  spnrTuePrefEndHr.setPreferredSize(new Dimension(75, 20));
		  spnrTuePrefEndHr.setModel(new SpinnerListModel(possibleTimes));
		  spnrTuePrefEndHr.setValue(possibleTimes.get(possibleTimes.size()-1));
		  //spnrTuePrefEndHr.setModel(new SpinnerNumberModel(9, 1, 12, 1));
		  GridBagConstraints gbc_spnrTuePrefEndHr = new GridBagConstraints();
		  gbc_spnrTuePrefEndHr.insets = new Insets(0, 0, 0, 5);
		  gbc_spnrTuePrefEndHr.gridx = 6;
		  gbc_spnrTuePrefEndHr.gridy = 1;
		  panelTuesdayTimes.add(spnrTuePrefEndHr, gbc_spnrTuePrefEndHr);
		  spnrTuePrefEndHr.addChangeListener(this);
		  
		  panelTuesdayTimes.setVisible(false);

//		WEDNESDAY
		  
		  chckbxWednesday = new JCheckBox("Wednesday");
		  chckbxWednesday.setOpaque(false);
		  GridBagConstraints gbc_chckbxWednesday = new GridBagConstraints();
		  gbc_chckbxWednesday.anchor = GridBagConstraints.WEST;
		  gbc_chckbxWednesday.insets = new Insets(0, 0, 5, 5);
		  gbc_chckbxWednesday.gridx = 0;
		  gbc_chckbxWednesday.gridy = 4;
		  panel_3.add(chckbxWednesday, gbc_chckbxWednesday);
		  chckbxWednesday.addItemListener(this);
		  
		  panelWedTimes = new JPanel();
		  panelWedTimes.setOpaque(false);
		  GridBagConstraints gbc_panelWedTimes = new GridBagConstraints();
		  gbc_panelWedTimes.gridwidth = 2;
		  gbc_panelWedTimes.insets = new Insets(0, 0, 5, 0);
		  gbc_panelWedTimes.gridx = 0;
		  gbc_panelWedTimes.gridy = 5;
		  panel_3.add(panelWedTimes, gbc_panelWedTimes);
		  GridBagLayout gbl_panelWedTimes = new GridBagLayout();
		  gbl_panelWedTimes.columnWidths = new int[]{28, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		  gbl_panelWedTimes.rowHeights = new int[]{0, 0, 0};
		  gbl_panelWedTimes.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		  gbl_panelWedTimes.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		  panelWedTimes.setLayout(gbl_panelWedTimes);
		  panelWedTimes.setVisible(false);
		  
		  JLabel label_1 = new JLabel("Start time");
		  GridBagConstraints gbc_label_1 = new GridBagConstraints();
		  gbc_label_1.anchor = GridBagConstraints.WEST;
		  gbc_label_1.insets = new Insets(0, 0, 5, 5);
		  gbc_label_1.gridx = 0;
		  gbc_label_1.gridy = 0;
		  panelWedTimes.add(label_1, gbc_label_1);
		  
		  spnrWedStartHr = new JSpinner();
		  spnrWedStartHr.setPreferredSize(new Dimension(75, 20));
		  spnrWedStartHr.setModel(new SpinnerListModel(possibleTimes));
		  spnrWedStartHr.setValue(possibleTimes.get(2));
		  //spnrWedStartHr.setModel(new SpinnerNumberModel(9, 1, 12, 1));
		  GridBagConstraints gbc_spnrWedStartHr = new GridBagConstraints();
		  gbc_spnrWedStartHr.insets = new Insets(0, 0, 5, 5);
		  gbc_spnrWedStartHr.gridx = 1;
		  gbc_spnrWedStartHr.gridy = 0;
		  panelWedTimes.add(spnrWedStartHr, gbc_spnrWedStartHr);
		  spnrWedStartHr.addChangeListener(this);
		  
		  JLabel label_2 = new JLabel("Pref Start Time");
		  GridBagConstraints gbc_label_2 = new GridBagConstraints();
		  gbc_label_2.anchor = GridBagConstraints.EAST;
		  gbc_label_2.gridwidth = 2;
		  gbc_label_2.insets = new Insets(0, 0, 5, 5);
		  gbc_label_2.gridx = 4;
		  gbc_label_2.gridy = 0;
		  panelWedTimes.add(label_2, gbc_label_2);
		  
		  spnrWedPrefStartHr = new JSpinner();
		  spnrWedPrefStartHr.setPreferredSize(new Dimension(75, 20));
		  spnrWedPrefStartHr.setModel(new SpinnerListModel(possibleTimes));
		  spnrWedPrefStartHr.setValue(possibleTimes.get(2));
		  //spnrWedPrefStartHr.setModel(new SpinnerNumberModel(9, 1, 12, 1));
		  GridBagConstraints gbc_spnrWedPrefStartHr = new GridBagConstraints();
		  gbc_spnrWedPrefStartHr.insets = new Insets(0, 0, 5, 5);
		  gbc_spnrWedPrefStartHr.gridx = 6;
		  gbc_spnrWedPrefStartHr.gridy = 0;
		  panelWedTimes.add(spnrWedPrefStartHr, gbc_spnrWedPrefStartHr);
		  spnrWedPrefStartHr.addChangeListener(this);
		  
		  JLabel label_4 = new JLabel("Pref End Time");
		  GridBagConstraints gbc_label_4 = new GridBagConstraints();
		  gbc_label_4.anchor = GridBagConstraints.EAST;
		  gbc_label_4.insets = new Insets(0, 0, 0, 5);
		  gbc_label_4.gridx = 5;
		  gbc_label_4.gridy = 1;
		  panelWedTimes.add(label_4, gbc_label_4);
		  
		  spnrWedPrefEndHr = new JSpinner();
		  spnrWedPrefEndHr.setPreferredSize(new Dimension(75, 20));
		  spnrWedPrefEndHr.setModel(new SpinnerListModel(possibleTimes));
		  spnrWedPrefEndHr.setValue(possibleTimes.get(possibleTimes.size()-1));
		  //spnrWedPrefEndHr.setModel(new SpinnerNumberModel(9, 1, 12, 1));
		  GridBagConstraints gbc_spnrWedPrefEndHr = new GridBagConstraints();
		  gbc_spnrWedPrefEndHr.insets = new Insets(0, 0, 0, 5);
		  gbc_spnrWedPrefEndHr.gridx = 6;
		  gbc_spnrWedPrefEndHr.gridy = 1;
		  panelWedTimes.add(spnrWedPrefEndHr, gbc_spnrWedPrefEndHr);
		  spnrWedPrefEndHr.addChangeListener(this);
		  
//		THURSDAY 
		  
		  chckbxThursday = new JCheckBox("Thursday");
		  chckbxThursday.setOpaque(false);
		  GridBagConstraints gbc_chckbxThursday = new GridBagConstraints();
		  gbc_chckbxThursday.anchor = GridBagConstraints.WEST;
		  gbc_chckbxThursday.insets = new Insets(0, 0, 5, 5);
		  gbc_chckbxThursday.gridx = 0;
		  gbc_chckbxThursday.gridy = 6;
		  panel_3.add(chckbxThursday, gbc_chckbxThursday);
		  chckbxThursday.addItemListener(this);
		  
		  panelThursTimes = new JPanel();
		  panelThursTimes.setOpaque(false);
		  GridBagConstraints gbc_panelThursTimes = new GridBagConstraints();
		  gbc_panelThursTimes.gridwidth = 2;
		  gbc_panelThursTimes.insets = new Insets(0, 0, 5, 0);
		  gbc_panelThursTimes.gridx = 0;
		  gbc_panelThursTimes.gridy = 7;
		  panel_3.add(panelThursTimes, gbc_panelThursTimes);
		  GridBagLayout gbl_panelThursTimes = new GridBagLayout();
		  gbl_panelThursTimes.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		  gbl_panelThursTimes.rowHeights = new int[]{0, 0, 0};
		  gbl_panelThursTimes.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		  gbl_panelThursTimes.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		  panelThursTimes.setLayout(gbl_panelThursTimes);
		  panelThursTimes.setVisible(false);
		  
		  JLabel label_3 = new JLabel("Start time");
		  GridBagConstraints gbc_label_3 = new GridBagConstraints();
		  gbc_label_3.anchor = GridBagConstraints.EAST;
		  gbc_label_3.insets = new Insets(0, 0, 5, 5);
		  gbc_label_3.gridx = 0;
		  gbc_label_3.gridy = 0;
		  panelThursTimes.add(label_3, gbc_label_3);
		  
		  spnrThursStartHr = new JSpinner();
		  spnrThursStartHr.setPreferredSize(new Dimension(75, 20));
		  spnrThursStartHr.setModel(new SpinnerListModel(possibleTimes));
		  spnrThursStartHr.setValue(possibleTimes.get(2));
		  //spnrThursStartHr.setModel(new SpinnerNumberModel(9, 1, 12, 1));
		  GridBagConstraints gbc_spnrThursStartHr = new GridBagConstraints();
		  gbc_spnrThursStartHr.insets = new Insets(0, 0, 5, 5);
		  gbc_spnrThursStartHr.gridx = 1;
		  gbc_spnrThursStartHr.gridy = 0;
		  panelThursTimes.add(spnrThursStartHr, gbc_spnrThursStartHr);
		  spnrThursStartHr.addChangeListener(this);
		  
		  JLabel label_5 = new JLabel("Pref Start Time");
		  GridBagConstraints gbc_label_5 = new GridBagConstraints();
		  gbc_label_5.anchor = GridBagConstraints.EAST;
		  gbc_label_5.gridwidth = 2;
		  gbc_label_5.insets = new Insets(0, 0, 5, 5);
		  gbc_label_5.gridx = 4;
		  gbc_label_5.gridy = 0;
		  panelThursTimes.add(label_5, gbc_label_5);
		  
		  spnrThursPrefStartHr = new JSpinner();
		  spnrThursPrefStartHr.setPreferredSize(new Dimension(75, 20));
		  spnrThursPrefStartHr.setModel(new SpinnerListModel(possibleTimes));
		  spnrThursPrefStartHr.setValue(possibleTimes.get(2));
		  //spnrThursPrefStartHr.setModel(new SpinnerNumberModel(9, 1, 12, 1));
		  GridBagConstraints gbc_spnrThursPrefStartHr = new GridBagConstraints();
		  gbc_spnrThursPrefStartHr.insets = new Insets(0, 0, 5, 5);
		  gbc_spnrThursPrefStartHr.gridx = 6;
		  gbc_spnrThursPrefStartHr.gridy = 0;
		  panelThursTimes.add(spnrThursPrefStartHr, gbc_spnrThursPrefStartHr);
		  spnrThursPrefStartHr.addChangeListener(this);
		  
		  JLabel label_6 = new JLabel("Pref End Time");
		  GridBagConstraints gbc_label_6 = new GridBagConstraints();
		  gbc_label_6.anchor = GridBagConstraints.EAST;
		  gbc_label_6.insets = new Insets(0, 0, 0, 5);
		  gbc_label_6.gridx = 5;
		  gbc_label_6.gridy = 1;
		  panelThursTimes.add(label_6, gbc_label_6);
		  
		  spnrThursPrefEndHr = new JSpinner();
		  spnrThursPrefEndHr.setPreferredSize(new Dimension(75, 20));
		  spnrThursPrefEndHr.setModel(new SpinnerListModel(possibleTimes));
		  spnrThursPrefEndHr.setValue(possibleTimes.get(possibleTimes.size()-1));
		  //spnrThursPrefEndHr.setModel(new SpinnerNumberModel(9, 1, 12, 1));
		  GridBagConstraints gbc_spnrThursPrefEndHr = new GridBagConstraints();
		  gbc_spnrThursPrefEndHr.insets = new Insets(0, 0, 0, 5);
		  gbc_spnrThursPrefEndHr.gridx = 6;
		  gbc_spnrThursPrefEndHr.gridy = 1;
		  panelThursTimes.add(spnrThursPrefEndHr, gbc_spnrThursPrefEndHr);
		  spnrThursPrefEndHr.addChangeListener(this);
		  
//		FRIDAY
		  
		  chckbxFriday = new JCheckBox("Friday");
		  chckbxFriday.setOpaque(false);
		  GridBagConstraints gbc_chckbxFriday = new GridBagConstraints();
		  gbc_chckbxFriday.anchor = GridBagConstraints.WEST;
		  gbc_chckbxFriday.insets = new Insets(0, 0, 5, 5);
		  gbc_chckbxFriday.gridx = 0;
		  gbc_chckbxFriday.gridy = 8;
		  panel_3.add(chckbxFriday, gbc_chckbxFriday);
		  chckbxFriday.addItemListener(this);
		  
		  panelFriTimes = new JPanel();
		  panelFriTimes.setOpaque(false);
		  GridBagConstraints gbc_panelFriTimes = new GridBagConstraints();
		  gbc_panelFriTimes.insets = new Insets(0, 0, 5, 0);
		  gbc_panelFriTimes.gridwidth = 2;
		  gbc_panelFriTimes.gridx = 0;
		  gbc_panelFriTimes.gridy = 9;
		  panel_3.add(panelFriTimes, gbc_panelFriTimes);
		  GridBagLayout gbl_panelFriTimes = new GridBagLayout();
		  gbl_panelFriTimes.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		  gbl_panelFriTimes.rowHeights = new int[]{0, 0, 0};
		  gbl_panelFriTimes.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		  gbl_panelFriTimes.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		  panelFriTimes.setLayout(gbl_panelFriTimes);
		  panelFriTimes.setVisible(false);
		  
		  JLabel label_10 = new JLabel("Start time");
		  GridBagConstraints gbc_label_10 = new GridBagConstraints();
		  gbc_label_10.anchor = GridBagConstraints.EAST;
		  gbc_label_10.insets = new Insets(0, 0, 5, 5);
		  gbc_label_10.gridx = 0;
		  gbc_label_10.gridy = 0;
		  panelFriTimes.add(label_10, gbc_label_10);
		  
		  spnrFriStartHr = new JSpinner();
		  spnrFriStartHr.setPreferredSize(new Dimension(75, 20));
		  spnrFriStartHr.setModel(new SpinnerListModel(possibleTimes));
		  spnrFriStartHr.setValue(possibleTimes.get(2));
		  //spnrFriStartHr.setModel(new SpinnerNumberModel(9, 1, 12, 1));
		  GridBagConstraints gbc_spnrFriStartHr = new GridBagConstraints();
		  gbc_spnrFriStartHr.insets = new Insets(0, 0, 5, 5);
		  gbc_spnrFriStartHr.gridx = 1;
		  gbc_spnrFriStartHr.gridy = 0;
		  panelFriTimes.add(spnrFriStartHr, gbc_spnrFriStartHr);
		  spnrFriStartHr.addChangeListener(this);
		  
		  JLabel label_11 = new JLabel("Pref Start Time");
		  GridBagConstraints gbc_label_11 = new GridBagConstraints();
		  gbc_label_11.anchor = GridBagConstraints.EAST;
		  gbc_label_11.gridwidth = 2;
		  gbc_label_11.insets = new Insets(0, 0, 5, 5);
		  gbc_label_11.gridx = 4;
		  gbc_label_11.gridy = 0;
		  panelFriTimes.add(label_11, gbc_label_11);
		  
		  spnrFriPrefStartHr = new JSpinner();
		  spnrFriPrefStartHr.setPreferredSize(new Dimension(75, 20));
		  spnrFriPrefStartHr.setModel(new SpinnerListModel(possibleTimes));
		  spnrFriPrefStartHr.setValue(possibleTimes.get(2));
		  //spnrFriPrefStartHr.setModel(new SpinnerNumberModel(9, 1, 12, 1));
		  GridBagConstraints gbc_spnrFriPrefStartHr = new GridBagConstraints();
		  gbc_spnrFriPrefStartHr.insets = new Insets(0, 0, 5, 5);
		  gbc_spnrFriPrefStartHr.gridx = 6;
		  gbc_spnrFriPrefStartHr.gridy = 0;
		  panelFriTimes.add(spnrFriPrefStartHr, gbc_spnrFriPrefStartHr);
		  spnrFriPrefStartHr.addChangeListener(this);
		  
		  JLabel label_12 = new JLabel("Pref End Time");
		  GridBagConstraints gbc_label_12 = new GridBagConstraints();
		  gbc_label_12.anchor = GridBagConstraints.EAST;
		  gbc_label_12.insets = new Insets(0, 0, 0, 5);
		  gbc_label_12.gridx = 5;
		  gbc_label_12.gridy = 1;
		  panelFriTimes.add(label_12, gbc_label_12);
		  
		  spnrFriPrefEndHr = new JSpinner();
		  spnrFriPrefEndHr.setPreferredSize(new Dimension(75, 20));
		  spnrFriPrefEndHr.setModel(new SpinnerListModel(possibleTimes));
		  spnrFriPrefEndHr.setValue(possibleTimes.get(possibleTimes.size()-1));
		  //spnrFriPrefEndHr.setModel(new SpinnerNumberModel(9, 1, 12, 1));
		  GridBagConstraints gbc_spnrFriPrefEndHr = new GridBagConstraints();
		  gbc_spnrFriPrefEndHr.insets = new Insets(0, 0, 0, 5);
		  gbc_spnrFriPrefEndHr.gridx = 6;
		  gbc_spnrFriPrefEndHr.gridy = 1;
		  panelFriTimes.add(spnrFriPrefEndHr, gbc_spnrFriPrefEndHr);
		  spnrFriPrefEndHr.addChangeListener(this);
		  
		  JLabel lblSortingWeights = new JLabel("Sorting weights");
		  lblSortingWeights.setBounds(529, 12, 138, 14);
		  frame.getContentPane().add(lblSortingWeights);
		  
		  JLabel lblIntervalSessionLength = new JLabel("interval, session length, max sessions, min students...Parameters");
		  lblIntervalSessionLength.setBounds(10, 12, 406, 14);
		  frame.getContentPane().add(lblIntervalSessionLength);

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
		  
		  comboIncrement = new JComboBox<Integer>();
		  comboIncrement.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				increment=(int)comboIncrement.getSelectedItem();
				formChanged=true;
			}
		});
		  GridBagConstraints gbc_comboIncrement = new GridBagConstraints();
		  gbc_comboIncrement.insets = new Insets(0, 0, 5, 5);
		  gbc_comboIncrement.gridx = 0;
		  gbc_comboIncrement.gridy = 0;
		  panelParameterInput.add(comboIncrement, gbc_comboIncrement);
		  comboIncrement.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {15, 30, 60}));
		  comboIncrement.setSelectedIndex(1);
		  
		  JLabel lblScheduleInc = new JLabel("schedule increment (minutes)");
		  GridBagConstraints gbc_lblScheduleInc = new GridBagConstraints();
		  gbc_lblScheduleInc.anchor = GridBagConstraints.WEST;
		  gbc_lblScheduleInc.insets = new Insets(0, 0, 5, 0);
		  gbc_lblScheduleInc.gridx = 1;
		  gbc_lblScheduleInc.gridy = 0;
		  panelParameterInput.add(lblScheduleInc, gbc_lblScheduleInc);
		  
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
		  
		  
		  scrollPane = new JScrollPane();
		  scrollPane.setBounds(870, 42, 304, 579);
		  frame.getContentPane().add(scrollPane);
		  
		  textSolutionOutput = new JTextArea();
		  scrollPane.setViewportView(textSolutionOutput);
		  textSolutionOutput.setAlignmentX(Component.RIGHT_ALIGNMENT);
		  textSolutionOutput.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	public void actionPerformed(ActionEvent ae){
		if (ae.getActionCommand().equals("Run")){
			
			schedules=Scheduler.readSchedulesFromFile("bible_prep_schedule_spring_16.csv");
			if (schedules==null)return;
			Schedule s=schedules.get(0);
			schedSize=s.getSchedule().length;
			int i=0;
			for (int slot:s.getSchedule()){if (slot==2)i++;}
			int[] numslots=new int[i+1];
			int j=0;
			i=0;
			for (int slot:s.getSchedule()){
				if (slot!=2) j++;
				else {
					numslots[i]=j;
					j=0;
					i++;
				}
			}
			numslots[i]=j;  //final entry in array
//TODO create button on form to read in csv file then disable checkboxes when num days are checked
			increment=(int)comboIncrement.getSelectedItem();
			int num=0;
			String dayString= "";
			ArrayList<Day> days=new ArrayList<Day>();
			preferredMask =new int[schedSize];
			int lastIndex=0;
				if (chckbxMonday.isSelected()) {
					if (numslots.length<=num){
						textSolutionOutput.setText("Please choose "+numslots.length+"days.");
						return;
					}
					//int hour = (int)spnrMonStartHr.getValue();
					//if (spnrMonStartAM.getValue().equals("PM")) hour+=12;
					//Time start=new Time(hour, (int)spnrMonStartMin.getValue());
					Time start = (Time)spnrMonStartHr.getValue();
					Time end=start.nextTime(increment*numslots[num]);
					Day monday=new Day(increment, start, end, "Monday");
					days.add(monday);
					dayString+=monday.toString()+start+end;
					Time prefStart=(Time)spnrMonPrefStartHr.getValue();
					Time prefEnd=(Time)spnrMonPrefEndHr.getValue();
					Time t=start;
					for (int index=lastIndex;index<(numslots[num]+lastIndex);index++){
						if (t.compareTo(prefStart)<0||t.compareTo(prefEnd)>=0) preferredMask[index]=1;
						else preferredMask[index]=0;
						t=t.nextTime(increment);
					}
					lastIndex+=numslots[num];
					if (lastIndex<schedSize)preferredMask[lastIndex]=2;
					lastIndex++;
					num++;
				}
				if (chckbxTuesday.isSelected()) {
					if (numslots.length<=num){
						textSolutionOutput.setText("Please choose "+numslots.length+"days.");
						return;
					}
					//int hour = (int)spnrTueStartHr.getValue();
					//if (spnrTueStartAM.getValue().equals("PM")) hour+=12;
					Time start=(Time)spnrTueStartHr.getValue(); 
					Time end=start.nextTime(increment*numslots[num]);
					Day tuesday=new Day(increment, start, end, "Tuesday");
					days.add(tuesday);
					dayString+=tuesday.toString()+start+end;
					Time prefStart=(Time)spnrTuePrefStartHr.getValue();
					Time prefEnd=(Time)spnrTuePrefEndHr.getValue();
					Time t=start;
					for (int index=lastIndex;index<(numslots[num]+lastIndex);index++){
						if (t.compareTo(prefStart)<0||t.compareTo(prefEnd)>=0) preferredMask[index]=1;
						else preferredMask[index]=0;
						t=t.nextTime(increment);
					}
					lastIndex+=numslots[num];
					if (lastIndex<schedSize)preferredMask[lastIndex]=2;
					lastIndex++;
					num++;
				
				}
				if (chckbxWednesday.isSelected()){
					if (numslots.length<=num){
						textSolutionOutput.setText("Please choose "+numslots.length+"days.");
						return;
					}
					//int hour = (int)spnrWedStartHr.getValue();
					//if (spnrWedStartAM.getValue().equals("PM")) hour+=12;
					Time start=(Time)spnrWedStartHr.getValue();
					Time end=start.nextTime(increment*numslots[num]);
					Day wednesday=new Day(increment, start, end, "Wednesday");
					days.add(wednesday);
					dayString+=wednesday.toString()+start+end;
					Time prefStart=(Time)spnrWedPrefStartHr.getValue();
					Time prefEnd=(Time)spnrWedPrefEndHr.getValue();
					Time t=start;
					for (int index=lastIndex;index<(numslots[num]+lastIndex);index++){
						if (t.compareTo(prefStart)<0||t.compareTo(prefEnd)>=0) preferredMask[index]=1;
						else preferredMask[index]=0;
						t=t.nextTime(increment);
					}
					lastIndex+=numslots[num];
					if (lastIndex<schedSize)preferredMask[lastIndex]=2;
					lastIndex++;
					num++;
				}
				if (chckbxThursday.isSelected()){
					if (numslots.length<=num){
						textSolutionOutput.setText("Please choose "+numslots.length+"days.");
						return;
					}
					//int hour = (int)spnrThursStartHr.getValue();
					//if (spnrThursStartAM.getValue().equals("PM")) hour+=12;
					Time start=(Time)spnrThursStartHr.getValue();
					Time end=start.nextTime(increment*numslots[num]);
					Day thursday=new Day(increment, start, end, "Thursday");
					days.add(thursday);
					dayString+=thursday.toString()+start+end;
					Time prefStart=(Time)spnrThursPrefStartHr.getValue();
					Time prefEnd=(Time)spnrThursPrefEndHr.getValue();
					Time t=start;
					for (int index=lastIndex;index<(numslots[num]+lastIndex);index++){
						if (t.compareTo(prefStart)<0||t.compareTo(prefEnd)>=0) preferredMask[index]=1;
						else preferredMask[index]=0;
						t=t.nextTime(increment);
					}
					lastIndex+=numslots[num];
					if (lastIndex<schedSize)preferredMask[lastIndex]=2;
					lastIndex++;
					num++;
				}
				if (chckbxFriday.isSelected()){
					if (numslots.length<=num){
						textSolutionOutput.setText("Please choose "+numslots.length+"days.");
						return;
					}
					//int hour = (int)spnrFriStartHr.getValue();
					//if (spnrFriStartAM.getValue().equals("PM")) hour+=12;
					Time start=(Time)spnrFriStartHr.getValue();
					Time end=start.nextTime(increment*numslots[num]);
					Day friday=new Day(increment, start, end, "Friday");
					days.add(friday);
					dayString+=friday.toString()+start+end;
					Time prefStart=(Time)spnrFriPrefStartHr.getValue();
					Time prefEnd=(Time)spnrFriPrefEndHr.getValue();
					Time t=start;
					for (int index=lastIndex;index<(numslots[num]+lastIndex);index++){
						if (t.compareTo(prefStart)<0||t.compareTo(prefEnd)>=0) preferredMask[index]=1;
						else preferredMask[index]=0;
						t=t.nextTime(increment);
						
					}
					lastIndex+=numslots[num];
					if (lastIndex<schedSize)preferredMask[lastIndex]=2;
					lastIndex++;
					num++;
				}
			
			if (numslots.length!=num)textSolutionOutput.setText("Please choose "+numslots.length+"days.");
			else textSolutionOutput.append("number of day checked: "+num+" "+sldrSessions.getValue()+"  "+increment
					+"\n"+dayString+"\n"+schedules.get(0).getName());
			times=Scheduler.createTimeArray(days,schedSize);
			Tree solutionTree=new Tree(null, new Session(-1));
			ArrayList<Schedule> schedulesCopy=(ArrayList<Schedule>)schedules.clone();
			Scheduler.sortSchedules(schedulesCopy,(int)comboBlockSize.getSelectedItem());
			//textSolutionOutput.setText(schedulesCopy.toString());
			String printString="Mask:\n";
			for (int k=0;k<preferredMask.length;k++){printString+="\n"+preferredMask[k]+" "+times[k];}
			textSolutionOutput.append(printString);
			int[] possibleMask=new int[preferredMask.length];
			do {
				Scheduler.createTree(schedulesCopy, solutionTree,possibleMask,minStudents,schedSize,blockSize);
				if (!solutionTree.hasLeaves()) {
					System.out.println("Impossible schedule, "+schedulesCopy.get(0).getName()+"removed");
					schedulesCopy.remove(0);
				}
			}		while (!solutionTree.hasLeaves());
			Tree.printSolutionToFile(solutionTree, 0,"solution.txt");
			if (!(solutionTree==null)) solutionTree.pruneTree();
//TODO null pointer exception in create solutions..
			createSolutions(solutionTree, new ArrayList<Session>());
			///Scheduler.createSolutions(solutionTree, new ArrayList<Session>());
			textSolutionOutput.append(fewestSessionsWeight+" "+preferredTimesWeight+" "+canComeWeight+" "+mustComeWeight);
			for (Solution sol:solutions){
				sol.calculateNormalizedRank(fewestSessionsWeight, preferredTimesWeight, canComeWeight, mustComeWeight,solutions);
				textSolutionOutput.append(sol.getCanBalance()+"");
			}
			Collections.sort(solutions);
			printSolutionsToOutputWindow();
			//empty arraylists for next run
			//solutionTree=null;
			solutions.clear();
			
			
			
		}
		else if (ae.getActionCommand().equals("Reset Form")){
			chckbxMonday.setSelected(false);
			chckbxTuesday.setSelected(false);
			chckbxWednesday.setSelected(false);
			chckbxThursday.setSelected(false);
			chckbxFriday.setSelected(false);
			sldrCan.setValue(5);
			sldrMust.setValue(5);
			sldrPrefTime.setValue(5);
			sldrSessions.setValue(5);
			comboBlockSize.setSelectedIndex(2);
			comboIncrement.setSelectedIndex(1);
			comboMaxSessions.setSelectedIndex(4);
			comboMinStudents.setSelectedIndex(3);
			textSolutionOutput.setText("number of day checked: 0");
		}
	}
	
	public void itemStateChanged(ItemEvent ie){
		if (chckbxTuesday.isSelected())	panelTuesdayTimes.setVisible(true);
		if (!chckbxTuesday.isSelected()) panelTuesdayTimes.setVisible(false);
		if (chckbxMonday.isSelected())	panelMonTimes.setVisible(true);
		if (!chckbxMonday.isSelected()) panelMonTimes.setVisible(false);
		if (chckbxWednesday.isSelected())	panelWedTimes.setVisible(true);
		if (!chckbxWednesday.isSelected()) panelWedTimes.setVisible(false);
		if (chckbxThursday.isSelected())	panelThursTimes.setVisible(true);
		if (!chckbxThursday.isSelected()) panelThursTimes.setVisible(false);
		if (chckbxFriday.isSelected())	panelFriTimes.setVisible(true);
		if (!chckbxFriday.isSelected()) panelFriTimes.setVisible(false);
		
	}

	@Override
	public void stateChanged(ChangeEvent ce) {
		if((ce.getSource()).equals(spnrMonStartHr)){
			if (((Time)spnrMonStartHr.getValue()).compareTo((Time)spnrMonPrefStartHr.getValue())>0){
				spnrMonPrefStartHr.setValue(spnrMonStartHr.getValue());
			}
		}
		else if((ce.getSource()).equals(spnrMonPrefStartHr)){
			if (((Time)spnrMonPrefStartHr.getValue()).compareTo((Time)spnrMonStartHr.getValue())<0){
				spnrMonPrefStartHr.setValue(spnrMonStartHr.getValue());
			}
			else if (((Time)spnrMonPrefStartHr.getValue()).compareTo((Time)spnrMonPrefEndHr.getValue())>0){
				spnrMonPrefStartHr.setValue(spnrMonPrefEndHr.getValue());
			}
		}
		else if (ce.getSource().equals(spnrMonPrefEndHr)){
			if (((Time)spnrMonPrefStartHr.getValue()).compareTo((Time)spnrMonPrefEndHr.getValue())>=0){
				spnrMonPrefEndHr.setValue(spnrMonPrefStartHr.getValue());
			}
		}
		else if((ce.getSource()).equals(spnrTueStartHr)){
			if (((Time)spnrTueStartHr.getValue()).compareTo((Time)spnrTuePrefStartHr.getValue())>0){
				spnrTuePrefStartHr.setValue(spnrTueStartHr.getValue());
			}
		}
		else if((ce.getSource()).equals(spnrTuePrefStartHr)){
			if (((Time)spnrTuePrefStartHr.getValue()).compareTo((Time)spnrTueStartHr.getValue())<0){
				spnrTuePrefStartHr.setValue(spnrTueStartHr.getValue());
			}
			else if (((Time)spnrTuePrefStartHr.getValue()).compareTo((Time)spnrTuePrefEndHr.getValue())>0){
				spnrTuePrefStartHr.setValue(spnrTuePrefEndHr.getValue());
			}
		}
		else if (ce.getSource().equals(spnrTuePrefEndHr)){
			if (((Time)spnrTuePrefStartHr.getValue()).compareTo((Time)spnrTuePrefEndHr.getValue())>=0){
				spnrTuePrefEndHr.setValue(spnrTuePrefStartHr.getValue());
			}
		}
		else if((ce.getSource()).equals(spnrWedStartHr)){
			if (((Time)spnrWedStartHr.getValue()).compareTo((Time)spnrWedPrefStartHr.getValue())>0){
				spnrWedPrefStartHr.setValue(spnrWedStartHr.getValue());
			}
		}
		else if((ce.getSource()).equals(spnrWedPrefStartHr)){
			if (((Time)spnrWedPrefStartHr.getValue()).compareTo((Time)spnrWedStartHr.getValue())<0){
				spnrWedPrefStartHr.setValue(spnrWedStartHr.getValue());
			}
			else if (((Time)spnrWedPrefStartHr.getValue()).compareTo((Time)spnrWedPrefEndHr.getValue())>0){
				spnrWedPrefStartHr.setValue(spnrWedPrefEndHr.getValue());
			}
		}
		else if (ce.getSource().equals(spnrWedPrefEndHr)){
			if (((Time)spnrWedPrefStartHr.getValue()).compareTo((Time)spnrWedPrefEndHr.getValue())>=0){
				spnrWedPrefEndHr.setValue(spnrWedPrefStartHr.getValue());
			}
		}
		else if((ce.getSource()).equals(spnrThursStartHr)){
			if (((Time)spnrThursStartHr.getValue()).compareTo((Time)spnrThursPrefStartHr.getValue())>0){
				spnrThursPrefStartHr.setValue(spnrThursStartHr.getValue());
			}
		}
		else if((ce.getSource()).equals(spnrThursPrefStartHr)){
			if (((Time)spnrThursPrefStartHr.getValue()).compareTo((Time)spnrThursStartHr.getValue())<0){
				spnrThursPrefStartHr.setValue(spnrThursStartHr.getValue());
			}
			else if (((Time)spnrThursPrefStartHr.getValue()).compareTo((Time)spnrThursPrefEndHr.getValue())>0){
				spnrThursPrefStartHr.setValue(spnrThursPrefEndHr.getValue());
			}
		}
		else if (ce.getSource().equals(spnrThursPrefEndHr)){
			if (((Time)spnrThursPrefStartHr.getValue()).compareTo((Time)spnrThursPrefEndHr.getValue())>=0){
				spnrThursPrefEndHr.setValue(spnrThursPrefStartHr.getValue());
			}
		}
		else if((ce.getSource()).equals(spnrFriStartHr)){
			if (((Time)spnrFriStartHr.getValue()).compareTo((Time)spnrFriPrefStartHr.getValue())>0){
				spnrFriPrefStartHr.setValue(spnrFriStartHr.getValue());
			}
		}
		else if((ce.getSource()).equals(spnrFriPrefStartHr)){
			if (((Time)spnrFriPrefStartHr.getValue()).compareTo((Time)spnrFriStartHr.getValue())<0){
				spnrFriPrefStartHr.setValue(spnrFriStartHr.getValue());
			}
			else if (((Time)spnrFriPrefStartHr.getValue()).compareTo((Time)spnrFriPrefEndHr.getValue())>0){
				spnrFriPrefStartHr.setValue(spnrFriPrefEndHr.getValue());
			}
		}
		else if (ce.getSource().equals(spnrFriPrefEndHr)){
			if (((Time)spnrFriPrefStartHr.getValue()).compareTo((Time)spnrFriPrefEndHr.getValue())>=0){
				spnrFriPrefEndHr.setValue(spnrFriPrefStartHr.getValue());
			}
		}
	}
	
	public void createSolutions(Tree t,ArrayList<Session> sessionList){
//TODO do not include solutions if not enough students or too amny sessions
		if (t.isEnd) {
			Solution solution=new Solution();
			ArrayList<Session> sessionListClone = (ArrayList<Session>) sessionList.clone();
			solution.setSessions(sessionList);
			//solution.print();
			solution.findAllMembers(schedules, blockSize);
			solutions.add(solution);
			//solution.print();
		}
		else for (Tree leaf:t.leaves){
			int time=leaf.session.time;
			boolean preferred;
			if (preferredMask[time]==0) preferred=true;
			else preferred = false;
			Session s=new Session(time,preferred);
			ArrayList<Session> sessionListCopy=(ArrayList<Session>)sessionList.clone();
			sessionListCopy.add(s);
			createSolutions(leaf, sessionListCopy);
			
		}
	}
	
	public void printSolutionsToOutputWindow(){
		//for (Solution sol:solutions){
		for (int i=0; i<solutions.size();i++){
		//for (int i=0;i<25;i++){
			if (solutions.get(i).getNumSessions()<=maxSessions){
//				System.out.println();
				textSolutionOutput.append("\n");
				Solution sol=solutions.get(i);
				textSolutionOutput.append("*******Solution "+i+"********\n");
				textSolutionOutput.append(""+sol.getRank()+"\n");
	//			System.out.println("*******Solution "+i+"********");
	//			System.out.format("Rank: %.2f\n",sol.getRank());
				Collections.sort(sol.getSessions());
				for (Session session:sol.getSessions()){
					int index=session.time;
	//				System.out.println("\t"+times[index]);
					textSolutionOutput.append("\t"+times[index]+"\n");;
					
					//print ratio of must attend to can attend
					//System.out.println(session.membersMustAttend.size()+"/"+session.members.size());
					
					//print names of those who can attend
	//				session.print();
	//				System.out.println();
					
					//print names of those who must attend:
					//session.printMustAttend();
					//System.out.println();
				}
			}
		}
	}
}
