package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import controller.Controller;
import model.Parameters;
import model.Solution;

public class MainFrame extends JFrame {
	///Data Entry Tab Components////
	private JPanel dataEntryTab;
	private StudentDataPanel studentDataPanel = new StudentDataPanel();
	private TablePanel tablePanel=new TablePanel();
	private JPanel buttonPanel;
	private JButton saveFileButton;
	private JButton openFileButton;
	private JButton newFileButton;

	///Scheduler Tab Components////
	private JPanel schedulerTab;

	private InputPanel inputPanel = new InputPanel();
	private ResultsPanel resultsPanel = new ResultsPanel();

	private JTabbedPane tabbedPane;

	private JFileChooser fileChooser,solutionFileChooser;
	private Controller controller;

	//TODO remove Preferences code? Or implement
	private PreferencesDialog preferencesDialog;
	private Preferences preferences;

	public MainFrame(String title){
		super(title);
		controller = new Controller();

		preferencesDialog = new PreferencesDialog(this);
		preferences=Preferences.userRoot().node("db");

		setBackground(Parameters.schemeColor1);
		setPreferredSize(new Dimension(1200,700));
		setMinimumSize(new Dimension(1000,700));
		setJMenuBar(createMenuBar());
		setLayout(new BorderLayout());
		//TODO learn how to use setImageIcon()
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new MyFileFilter());

		solutionFileChooser = new JFileChooser();
		solutionFileChooser.setFileFilter(new CsvFileFilter());

		//fileChooser.addChoosableFileFilter(new MyFileFilter());

//////////////////Button Panel/////////////////////////////////

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.setBackground(Parameters.schemeColor2);

		saveFileButton = new JButton("Save File");
		saveFileButton.setMaximumSize(new Dimension(150, 25));
		saveFileButton.setEnabled(false);
		buttonPanel.add(saveFileButton);
		openFileButton = new JButton("Open File");
		openFileButton.setMaximumSize(new Dimension(150, 25));
		buttonPanel.add(openFileButton);
		newFileButton = new JButton("New File");
		newFileButton.setMaximumSize(new Dimension(150, 25));
		buttonPanel.add(newFileButton);

		saveFileButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int choice = fileChooser.showSaveDialog(MainFrame.this);
				if (choice==JFileChooser.APPROVE_OPTION){
						try {
							controller.saveToFile(fileChooser.getSelectedFile());
							saveFileButton.setEnabled(false);

						}
						catch (IOException e1) {
							e1.printStackTrace();
						}
				}
			}
		});

		openFileButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int choice = fileChooser.showOpenDialog(MainFrame.this);
				if (choice==JFileChooser.APPROVE_OPTION){
					try {
						controller.loadFromFile(fileChooser.getSelectedFile());
						tablePanel.refresh();
						saveFileButton.setEnabled(false);

					} catch (IOException ie) {
						JOptionPane.showMessageDialog(MainFrame.this, "Could not load file", "Error", JOptionPane.ERROR_MESSAGE);
						ie.printStackTrace();
					}
				}
			}
		});

		newFileButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO implement "new file" button - make sure current database saved first
				System.out.println("new file button");
				System.out.println("changed? "+controller.databaseChanged());
				saveFileButton.setEnabled(false);
			}
		});

/////////////////////// Tabs ////////////////////
		schedulerTab=new JPanel();
		schedulerTab.setLayout(new BorderLayout());
		schedulerTab.add(inputPanel,BorderLayout.WEST);
		schedulerTab.add(resultsPanel,BorderLayout.CENTER);

		dataEntryTab = new JPanel();
		dataEntryTab.setLayout(new BorderLayout());
		dataEntryTab.add(studentDataPanel,BorderLayout.WEST);
		dataEntryTab.add(tablePanel,BorderLayout.CENTER);
		dataEntryTab.add(buttonPanel,BorderLayout.NORTH);
		dataEntryTab.setMinimumSize(this.getMinimumSize());

		tabbedPane=new JTabbedPane();
		tabbedPane.setMinimumSize(this.getMinimumSize());
		tabbedPane.addTab("Data Entry", dataEntryTab);
		tabbedPane.addTab("Scheduler", schedulerTab);
		int schedulerIndex = tabbedPane.indexOfTab("Scheduler");
		int dataEntryIndex = tabbedPane.indexOfTab("Data Entry");

		tabbedPane.setEnabledAt(schedulerIndex, false);
		tabbedPane.setSelectedIndex(dataEntryIndex);




////////////////// Preferences ///////////////////////////
		preferencesDialog.setPreferencesListener(new PreferencesListener() {

			@Override
			public void preferencesSet(String user, String password, int port) {
				System.out.println(user+" "+password+" "+port);
				preferences.put("user", user);
				preferences.put("password", password);
				preferences.putInt("port", port);
			}
		});

		String user=preferences.get("user", "");
		String password = preferences.get("password", "");
		Integer port = preferences.getInt("port", 3306);
		preferencesDialog.setDefaults(user, password, port);

//////////////// Student Data Panel /////////////////////
		studentDataPanel.setStudentFormListener(new StudentFormListener(){

			@Override
			public void StudentFormEventOccurred(StudentDataEvent e) {
				System.out.println("controller: StudentFormEventOccurred");
				if (e.getId()==-1 && !e.getFName().equals("") && !e.getLName().equals("")){
					System.out.println("StudentFormEvent: "+e.getSchedule()[0][0]+" "+e.getSchedule()[0][1]);
					controller.addStudent(e.getFName(),e.getLName(), e.getEmail(),e.getArea(),e.getSchedule());
					System.out.println("after controller: "+controller.getStudents().get(0));
					saveFileButton.setEnabled(true);
					tablePanel.refresh();
				}
				else if (!e.getFName().equals("") && !e.getLName().equals("")){
					controller.updateStudent(e.getId(), e.getFName(),e.getLName(), e.getEmail(),e.getArea(),e.getSchedule());
					System.out.println("StudentFormEvent update: "+e.getSchedule()[0][0]+" "+e.getSchedule()[0][1]);
					saveFileButton.setEnabled(true);
					tablePanel.refresh();
				}
			}

			@Override
			public void doneEventOccurred() {
				System.out.println("done event occurred");
				System.out.println("database changed? "+controller.databaseChanged());
				if (controller.getStudents().isEmpty()) {
					System.out.println("No data in database");
					JOptionPane.showMessageDialog(getParent(), "No students in database.\nAdd students or open a data file before continuing.", "Database Error", JOptionPane.WARNING_MESSAGE);
					return;
				}
				if (controller.databaseChanged()){
					int ans = JOptionPane.showConfirmDialog(getParent(), "Database file not saved. Continue anyway?", "File not saved", JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
					if (ans!=JOptionPane.OK_OPTION) return;
				}
				tabbedPane.setEnabledAt(1, true);
				tabbedPane.setSelectedIndex(1);
			}
		});

/////////////// Table Panel ///////////////////////////////////
		tablePanel.setData(controller.getStudents());
		tablePanel.setTableListener(new TableListener(){

			@Override
			public void rowDeleted(int row) {
				int choice=JOptionPane.showConfirmDialog(getParent(), "Are you sure you want to delete this student?", "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (choice==JOptionPane.YES_OPTION) {
					controller.removeStudent(row);
					saveFileButton.setEnabled(true);
					studentDataPanel.resetForm();
				}
			}

			@Override
			public boolean rowSelected(int row) {

				if (studentDataPanel.isDirty()){
					int choice=JOptionPane.showConfirmDialog(getParent(), "Current student not saved. Continue anyway? Your changes will be lost.", "Data not saved", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
					if (choice==JOptionPane.CANCEL_OPTION) {
						return false; // selection failed
					}
				}
				studentDataPanel.populateForm(controller.getStudents().get(row));
				//System.out.println("rowSelected: "+controller.getStudents().get(row).toString());
				return true;
			}
		});

////////////////// Input Panel ////////////////////////////////
		inputPanel.setInputFormListener(new InputFormListener() {

			@Override
			public void inputFormEventOccurred(InputEvent event) {
				//System.out.println("input form event");
				controller.runScheduler(event.getMaxStudents(), event.getMinStudents(),event.getMaxSessions(),event.getBlockSize(),
						event.getNumSessionsWeight(),event.getPreferredWeight(),event.getCanWeight(),event.getMustWeight());
				resultsPanel.setSolutionsData(controller.getSolutions());
				resultsPanel.refreshSolutions();
				resultsPanel.setVariationsData(new ArrayList<Solution>());
				resultsPanel.refreshMembersData();

			}
		});

////////////////// Results Panel //////////////////////////////
		resultsPanel.setSolutionsTableListener(new SolutionsTableListener() {

			@Override
			public void rowSelected(int row) {
				//System.out.println("in row selected...");
				resultsPanel.setVariationsData(controller.getVariations(row));
				resultsPanel.refreshVariations();
			}
		});

		resultsPanel.setVariationsTableListener(new VariationsTableListener() {

			@Override
			public void rowSelected(int row) {
				System.out.println(controller.getMembers(row));
				resultsPanel.setMembersData(controller.getMembers(row));
				//resultsPanel.refreshMembers();
			}
		});

		resultsPanel.setSaveEventListener(new SaveEventListener() {

			@Override
			public void saveEventOccurred(SaveSolutionEvent event) {
				int choice = solutionFileChooser.showSaveDialog(MainFrame.this);
				if (choice==JFileChooser.APPROVE_OPTION){
						controller.saveSolutionToFile(solutionFileChooser.getSelectedFile(), event.getIndex());
				}
			}
		});

////////////////// Add Components to Frame /////////////////////

		add(tabbedPane,BorderLayout.CENTER);
		setVisible(true);
	}

///////////////////// Menu Bar //////////////////////////////

	private JMenuBar createMenuBar(){
		//TODO remove unnecessary menu items and add useful ones
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		//TODO change "import" to import a csv and convert
		//TODO or import a single schedule as csv and convert?
		JMenuItem importMenuItem = new JMenuItem("Import File...");
		//TODO change "export"to export database to csv file
		JMenuItem exportMenuItem = new JMenuItem("Export file...");
		JMenuItem exitItem = new JMenuItem("Exit");
		JMenuItem preferencesMenuItem = new JMenuItem("Preferences...");
		fileMenu.add(importMenuItem);
		fileMenu.add(exportMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(preferencesMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);

		preferencesMenuItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				preferencesDialog.setVisible(true);
			}

		});

		importMenuItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int choice = fileChooser.showOpenDialog(MainFrame.this);
				if (choice==JFileChooser.APPROVE_OPTION){
					try {
						controller.loadFromFile(fileChooser.getSelectedFile());
						tablePanel.refresh();

					} catch (IOException e) {
						JOptionPane.showMessageDialog(MainFrame.this, "Could not load file", "Error", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
				}
			}

		});

		exportMenuItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean goodFileName = false;
				String extension;
				int choice;
				File file;
				fileChooser.setFileFilter(new CsvFileFilter());

				//TODO write a general extension check method to use for all save dialogs
				//TODO add JOptionPane to notify user the extension is incorrect
				//TODO check for file overwrite
				do{
					choice = fileChooser.showSaveDialog(MainFrame.this);
					file = fileChooser.getSelectedFile();
					System.out.println(file.getAbsolutePath());
					extension=Utils.getFileExtension(file.getName());
					System.out.println(Utils.getFileExtension(file.getName()));
					if (extension!=null && extension.equals("csv")) goodFileName=true;
					else {
						if (extension!=null){
							//remove current extension
							//replace with .csv
						}
						if (extension==null){
							file = new File(file.getAbsolutePath()+".csv");
							fileChooser.setSelectedFile(file);
						}
					}
				} while (!goodFileName && choice!=JFileChooser.CANCEL_OPTION);

				if (choice==JFileChooser.APPROVE_OPTION){
					try {
						//controller.saveToFile(fileChooser.getSelectedFile());
						controller.exportToCsv(file);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(MainFrame.this, "Could not save file", "Error", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
				}
			}

		});

		fileMenu.setMnemonic(KeyEvent.VK_F);
		exportMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,ActionEvent.CTRL_MASK));
		importMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,ActionEvent.CTRL_MASK));
		exitItem.setMnemonic(KeyEvent.VK_X);
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,ActionEvent.CTRL_MASK));

		exitItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent ae) {
				int option = JOptionPane.showConfirmDialog(MainFrame.this, "Are you sure you want to exit?",
						"Confirm Exit", JOptionPane.OK_CANCEL_OPTION);
				if (option==JOptionPane.OK_OPTION) System.exit(0); //Exit the program
			}

		});

		JMenu otherMenu = new JMenu("Other");
		JMenu subMenu = new JMenu("Sub Menu");
		JMenuItem subMenuItem =new JMenuItem("Item");
		subMenu.add(subMenuItem);
		otherMenu.add(subMenu);

		menuBar.add(fileMenu);
		menuBar.add(otherMenu);
		return menuBar;

	}
}

final class CsvFileFilter extends FileFilter{

	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) return true; //allow directories to be seen in fileChooser
		String name = file.getName();
		String extension = Utils.getFileExtension(name);
		if (extension == null) return false;
		else if (extension.equals("csv")) return true;
		else return false;
	}

	@Override
	public String getDescription() {
		return "Comma separated values file (*.csv)";
	}

}
