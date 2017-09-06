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
import java.util.Arrays;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import controller.Controller;
import model.Parameters;
import model.Solution;
import model.Student;

public class MainFrame extends JFrame {
	///Set Up Tab Components////////
	private JPanel setUpTab;
	private JPanel setUpButtonPanel;
	private ScheduleInputPanel maskInputPanel = new ScheduleInputPanel(Parameters.timeSlotStrings,Parameters.dayNames);
	private JButton setPrefMask;
	private JButton setAllowedMask;

	///Data Entry Tab Components////
	private JPanel dataEntryTab;
	private StudentDataPanel studentDataPanel;
	private TablePanel tablePanel=new TablePanel();
	private JPanel buttonPanel;
	private JButton saveFileButton;
	private JButton openFileButton;
	private JButton newFileButton;
	private JButton importScheduleButton;
	private JLabel currentFileLabel;

	///Scheduler Tab Components////
	private JPanel schedulerTab;

	private InputPanel inputPanel = new InputPanel();
	private ResultsPanel resultsPanel = new ResultsPanel();

	private JTabbedPane tabbedPane;
	private int schedulerIndex;
	private int dataEntryIndex;
	private int setUpIndex;

	private JFileChooser fileChooser,solutionFileChooser,importFileChooser;
	private File currentFile = new File("Untitled.stu");
	private Controller controller;

	//IDEA implement preferences- default save location? default mask?
	private PreferencesDialog preferencesDialog;
	private Preferences preferences;

///////////////////// MainFrame ////////////////////////////////////////
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new MyFileFilter());

		solutionFileChooser = new JFileChooser();
		solutionFileChooser.setFileFilter(new ExcelFileFilter());

		importFileChooser = new JFileChooser();
		importFileChooser.setFileFilter(new ExcelFileFilter());

		//solutionFileChooser.setFileFilter(new CsvFileFilter());

		//fileChooser.addChoosableFileFilter(new MyFileFilter());

		//////////////////Set Up Tab Button Panel ////////////////////////
		setUpButtonPanel = new JPanel();
		setUpButtonPanel.setMaximumSize(new Dimension(200, this.getHeight()));
		setUpButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		setUpButtonPanel.setBackground(Parameters.schemeColor2);

		setAllowedMask = new JButton("Set As Allowed Times");
		setAllowedMask.setMaximumSize(new Dimension(150, 25));
		setUpButtonPanel.add(setAllowedMask);

		setPrefMask = new JButton("Set As Preferred Times");
		setPrefMask.setMaximumSize(new Dimension(150, 25));
		setUpButtonPanel.add(setPrefMask);

		setAllowedMask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("set allowed times button");
				controller.setAllowedTimesMask(Utils.copyOf(maskInputPanel.getData()));
				controller.setPreferredTimesMask(Utils.copyOf(maskInputPanel.getData()));
				studentDataPanel.setMask(controller.getAllowedTimesMask());
				tabbedPane.setEnabledAt(dataEntryIndex,true);
			}
		});

		setPrefMask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("set preferred times button");
				controller.setPreferredTimesMask(Utils.copyOf(maskInputPanel.getData()));
			}
		});



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

		importScheduleButton = new JButton("Import Schedule");
		importScheduleButton.setMaximumSize(new Dimension(150, 25));
		buttonPanel.add(importScheduleButton);

		currentFileLabel = new JLabel(currentFile.getName());
		currentFileLabel.setMaximumSize(new Dimension(150, 25));
		buttonPanel.add(currentFileLabel);

		saveFileButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					saveFile(currentFile);
					currentFileLabel.setText(currentFile.getName());
					//saveFileButton.setEnabled(false);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
//				int choice = fileChooser.showSaveDialog(MainFrame.this);
//				if (choice==JFileChooser.APPROVE_OPTION){
//						try {
//							if (!Utils.extensionOK(fileChooser.getSelectedFile(), "stu")){
//								fileChooser.setSelectedFile(Utils.changeExtension(fileChooser.getSelectedFile(), "stu"));
//							}
//							controller.saveToFile(fileChooser.getSelectedFile());
//							saveFileButton.setEnabled(false);
//							currentFile=fileChooser.getSelectedFile();
//
//						}
//						catch (IOException e1) {
//							e1.printStackTrace();
//						}
//				}
			}
		});

		//IDEA add "Save As" button option
		//TODO label that displays current filename. also save current filename in variable


		openFileButton.addActionListener(new ActionListener() {
			//FIXME if file opened is not current version, prompt for save
			//FIXME if nothing in database yet, don't prompt for save
			@Override
			public void actionPerformed(ActionEvent e) {
				if (studentDataPanel.isDirty()){
					int choice=JOptionPane.showConfirmDialog(getParent(), "Current student not saved. Continue anyway? Your changes will be lost.", "Data not saved", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
					if (choice==JOptionPane.CANCEL_OPTION) {
						return; // selection failed
					}
				}

				System.out.println("changed? "+controller.databaseChanged());
				if (controller.databaseChanged()){
					int choice = JOptionPane.showConfirmDialog(MainFrame.this,"File changed. Save first?");
					if (choice==JOptionPane.YES_OPTION){
						try {
							saveFile(currentFile);
							currentFileLabel.setText(currentFile.getName());
							//saveFileButton.setEnabled(false);
						}
						catch (IOException e1) {
							e1.printStackTrace();
						}

//						try {
//							if (fileChooser.getSelectedFile()!=null){
//								controller.saveToFile(fileChooser.getSelectedFile());
//							}
//							else{
//								int choice2 = fileChooser.showSaveDialog(MainFrame.this);
//								if (choice2==JFileChooser.APPROVE_OPTION){
//										try {
//											controller.saveToFile(fileChooser.getSelectedFile());
//											saveFileButton.setEnabled(false);
//
//										}
//										catch (IOException e1) {
//											e1.printStackTrace();
//										}
//								}
//							}
//							saveFileButton.setEnabled(false);
//
//						} catch (IOException e1) {
//							e1.printStackTrace();
//						}
					}
					else if (choice==JOptionPane.CANCEL_OPTION) return;
				}


				fileChooser.setFileFilter(new MyFileFilter());
				fileChooser.setSelectedFile(null);
				int choice = fileChooser.showOpenDialog(MainFrame.this);
				if (choice==JFileChooser.APPROVE_OPTION){
					try {
						currentFile=fileChooser.getSelectedFile();
						controller.loadFromFile(currentFile);
						tablePanel.refresh();
						saveFileButton.setEnabled(false);
						currentFileLabel.setText(currentFile.getName());

					}
					catch (IOException ie) {
						JOptionPane.showMessageDialog(MainFrame.this, "Could not load file.\n"+ie.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

						ie.printStackTrace();
					}
				}
			}
		});

		newFileButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				//System.out.println("new file button");

				if (studentDataPanel.isDirty()){
					int choice=JOptionPane.showConfirmDialog(getParent(), "Current student not saved. Continue anyway? Your changes will be lost.", "Data not saved", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
					if (choice==JOptionPane.CANCEL_OPTION) {
						return; // selection failed
					}
				}

				//System.out.println("changed? "+controller.databaseChanged());
				if (controller.databaseChanged()){
					int choice = JOptionPane.showConfirmDialog(MainFrame.this,"File changed. Save first?");
					if (choice==JOptionPane.YES_OPTION){
						try {
							saveFile(currentFile);
							currentFileLabel.setText(currentFile.getName());
							//saveFileButton.setEnabled(false);
						}
						catch (IOException e1) {
							e1.printStackTrace();
						}



//						try {
//							if (fileChooser.getSelectedFile()!=null){
//								controller.saveToFile(fileChooser.getSelectedFile());
//							}
//							else{
//								int choice2 = fileChooser.showSaveDialog(MainFrame.this);
//								if (choice2==JFileChooser.APPROVE_OPTION){
//										try {
//											controller.saveToFile(fileChooser.getSelectedFile());
//											saveFileButton.setEnabled(false);
//
//										}
//										catch (IOException e1) {
//											e1.printStackTrace();
//										}
//								}
//							}
//							saveFileButton.setEnabled(false);
//
//						} catch (IOException e1) {
//							e1.printStackTrace();
//						}
					}
					else if (choice==JOptionPane.CANCEL_OPTION) return;
				}
				//new file
				controller.clearDatabase();
				saveFileButton.setEnabled(true);
				tablePanel.refresh();
				studentDataPanel.resetForm();
				fileChooser.setSelectedFile(new File("Untitled.stu"));
				currentFile=fileChooser.getSelectedFile();
				currentFileLabel.setText(currentFile.getName());
			}
		});

		importScheduleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				importFileChooser.setFileFilter(new ExcelFileFilter());
				importFileChooser.setMultiSelectionEnabled(true);
				boolean fileOK=true;
				int choice;
				do{
					choice=importFileChooser.showOpenDialog(MainFrame.this);
					if (choice==JFileChooser.APPROVE_OPTION){
						//Student student;
						ArrayList<Student> students = new ArrayList<Student>();
						try {
							//TODO maybe send students arraylist into importschedules and return error info?
							students = controller.importSchedule(importFileChooser.getSelectedFiles());
							//studentDataPanel.populateForm(student);
							if (students!=null){
								for (Student student:students) {
									controller.addStudent(student);
									tablePanel.refresh();

								}
								saveFileButton.setEnabled(true);
								return;
							}
							else {
								System.out.println("Bad import file");
								fileOK=false;
								JOptionPane.showMessageDialog(MainFrame.this, "There is a problem with the file you chose. Please choose another file");
							}
						}
						catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					else if (choice==JFileChooser.CANCEL_OPTION) return;

				}
				while(!fileOK||choice!=JFileChooser.CANCEL_OPTION);
			}
		});

		/////////////////////// Tabs ////////////////////
		setUpTab = new JPanel();
		setUpTab.setLayout(new FlowLayout(FlowLayout.CENTER));
		setUpTab.setBackground(Parameters.schemeColor2);
		setUpTab.add(setUpButtonPanel);
		maskInputPanel.init();
		maskInputPanel.setPreferredSize(new Dimension(200, 500));
		setUpTab.add(maskInputPanel);

		schedulerTab=new JPanel();
		schedulerTab.setLayout(new BorderLayout());
		schedulerTab.add(inputPanel,BorderLayout.WEST);
		schedulerTab.add(resultsPanel,BorderLayout.CENTER);

		dataEntryTab = new JPanel();
		studentDataPanel=new StudentDataPanel(Parameters.timeSlotStrings,Parameters.dayNames,new int[Parameters.dayNames.length][Parameters.timeSlotStrings.length]);
		dataEntryTab.setLayout(new BorderLayout());
		dataEntryTab.add(studentDataPanel,BorderLayout.WEST);
		dataEntryTab.add(tablePanel,BorderLayout.CENTER);
		dataEntryTab.add(buttonPanel,BorderLayout.NORTH);
		dataEntryTab.setMinimumSize(this.getMinimumSize());

		tabbedPane=new JTabbedPane();
		tabbedPane.setMinimumSize(this.getMinimumSize());
		tabbedPane.add("Set Up",setUpTab);
		tabbedPane.addTab("Data Entry", dataEntryTab);
		tabbedPane.addTab("Scheduler", schedulerTab);
		schedulerIndex = tabbedPane.indexOfTab("Scheduler");
		dataEntryIndex = tabbedPane.indexOfTab("Data Entry");
		setUpIndex = tabbedPane.indexOfTab("Set Up");

		tabbedPane.setEnabledAt(schedulerIndex, false);
		tabbedPane.setEnabledAt(dataEntryIndex,false);
		tabbedPane.setSelectedIndex(setUpIndex);




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
				tabbedPane.setEnabledAt(schedulerIndex, true);
				tabbedPane.setSelectedIndex(schedulerIndex);
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
						//controller.saveSolutionToFile(solutionFileChooser.getSelectedFile(), event.getIndex());
						try {
							//FIXME save solution to excel should automatically put .xlsx extension
							controller.saveSolutionToExcel(solutionFileChooser.getSelectedFile(), event.getIndex());
						} catch (IOException e) {
							e.printStackTrace();
						}

				}
			}
		});

		////////////////// Add Components to Frame /////////////////////

		add(tabbedPane,BorderLayout.CENTER);
		setVisible(true);
	}

	private void saveFile(File file) throws IOException{
		if (file!=null && !file.getName().equals("Untitled.stu")){
			//TODO need to check for correct extension?
			controller.saveToFile(file);
			JOptionPane.showMessageDialog(MainFrame.this, "File saved successfully.", "File Saved", JOptionPane.PLAIN_MESSAGE);
			saveFileButton.setEnabled(false);
			currentFile=file;
		}
		else{
			fileChooser.setSelectedFile(file);
			int choice = fileChooser.showSaveDialog(MainFrame.this);

			if (choice==JFileChooser.APPROVE_OPTION){
				if (!Utils.extensionOK(fileChooser.getSelectedFile(), "stu")){
					fileChooser.setSelectedFile(Utils.changeExtension(fileChooser.getSelectedFile(), "stu"));
				}
				currentFile=fileChooser.getSelectedFile();
				controller.saveToFile(currentFile);
				saveFileButton.setEnabled(false);
				JOptionPane.showMessageDialog(MainFrame.this, "File saved successfully.", "File Saved", JOptionPane.PLAIN_MESSAGE);


			}
		}

	}

///////////////////// Menu Bar //////////////////////////////

	private JMenuBar createMenuBar(){
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu exportMenu = new JMenu("Export ");
		//TODO check to see if there is any data to export to a file - students in database
		//TODO change "import" to import a csv and convert
		JMenuItem importMenuItem = new JMenuItem("Import File...");
		JMenuItem exportMenuItemExcel = new JMenuItem("Export data to Excel...");
		JMenuItem exportMenuItemCsv = new JMenuItem("Export data as CSV...");
		JMenuItem exitItem = new JMenuItem("Exit");
		JMenuItem preferencesMenuItem = new JMenuItem("Preferences...");
		exportMenu.add(exportMenuItemExcel);
		exportMenu.add(exportMenuItemCsv);
		fileMenu.add(importMenuItem);
		//fileMenu.add(exportMenuItem);
		fileMenu.add(exportMenu);
		fileMenu.addSeparator();
		//fileMenu.add(preferencesMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);

		preferencesMenuItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//uncomment below if preferences implemented
				//preferencesDialog.setVisible(true);
			}

		});

		importMenuItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				fileChooser.setFileFilter(new MyFileFilter());
				int choice = fileChooser.showOpenDialog(MainFrame.this);
				if (choice==JFileChooser.APPROVE_OPTION){
					try {
						controller.loadFromFile(fileChooser.getSelectedFile());
						tablePanel.refresh();

					} catch (IOException e) {
						JOptionPane.showMessageDialog(MainFrame.this, "Could not load file."+e.getMessage()+" "+e.getCause().toString(), "Error", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
				}
			}

		});

		exportMenuItemExcel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean goodFileName = false;
				String extension;
				int choice;
				File file;
				if (controller.getNumStudents()==0){
					JOptionPane.showMessageDialog(MainFrame.this, "There is no data to export", "No Data", JOptionPane.WARNING_MESSAGE);
					return;
				}
				fileChooser.setFileFilter(new ExcelFileFilter());

				choice = fileChooser.showSaveDialog(MainFrame.this);
				file = fileChooser.getSelectedFile();
				file = Utils.changeExtension(file, "xlsx");
				fileChooser.setSelectedFile(file);

				while (!goodFileName && choice!=JFileChooser.CANCEL_OPTION){

					extension=Utils.getFileExtension(file.getName());
					if (extension!=null && extension.equals("xlsx")) goodFileName=true;
					else {
						if (extension!=null){
							file = new File(Utils.removeExtension(file.getAbsolutePath())+".xlsx");
							System.out.println(file.getName());
							fileChooser.setSelectedFile(file);
						}
						if (extension==null){
							file = new File(file.getAbsolutePath()+".xlsx");

							fileChooser.setSelectedFile(file);
						}
					}
					if (file.exists() && !file.isDirectory()){
						int confirm=JOptionPane.showConfirmDialog(MainFrame.this, "File already exists. Overwrite?","File exists",JOptionPane.YES_NO_OPTION);
						if (confirm==JOptionPane.NO_OPTION) goodFileName=false;
					}
					choice = fileChooser.showSaveDialog(MainFrame.this);
					file = fileChooser.getSelectedFile();
				}
				if (choice==JFileChooser.CANCEL_OPTION) return;
				else if (choice==JFileChooser.APPROVE_OPTION){
					try {
						controller.exportToExcel(file);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(MainFrame.this, "Could not save file", "Error", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
				}
			}

		});

		exportMenuItemCsv.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean goodFileName = false;
				String extension;
				int choice;
				File file;

				if (controller.getNumStudents()==0){
					JOptionPane.showMessageDialog(MainFrame.this, "There is no data to export", "No Data", JOptionPane.WARNING_MESSAGE);
					return;
				}

				fileChooser.setFileFilter(new CsvFileFilter());

				choice = fileChooser.showSaveDialog(MainFrame.this);
				file = fileChooser.getSelectedFile();


				 while (!goodFileName && choice!=JFileChooser.CANCEL_OPTION){
					extension=Utils.getFileExtension(file.getName());
					if (extension!=null && extension.equals("csv")) goodFileName=true;
					else {
						if (extension!=null){
							file = new File(Utils.removeExtension(file.getAbsolutePath())+".csv");
							System.out.println(file.getName());
							fileChooser.setSelectedFile(file);
						}
						if (extension==null){
							file = new File(file.getAbsolutePath()+".csv");

							fileChooser.setSelectedFile(file);
						}
					}
					if (file.exists() && !file.isDirectory()){
						int confirm=JOptionPane.showConfirmDialog(MainFrame.this, "File already exists. Overwrite?","File exists",JOptionPane.YES_NO_OPTION);
						if (confirm==JOptionPane.NO_OPTION) goodFileName=false;
					}
					choice = fileChooser.showSaveDialog(MainFrame.this);
					file = fileChooser.getSelectedFile();
				}

				if (choice==JFileChooser.CANCEL_OPTION) return;
				else if (choice==JFileChooser.APPROVE_OPTION){
					try {
						controller.exportToCsv(file);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(MainFrame.this, "Could not save file", "Error", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
				}
			}

		});


		fileMenu.setMnemonic(KeyEvent.VK_F);
		exportMenuItemExcel.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,ActionEvent.CTRL_MASK));
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

//		JMenu otherMenu = new JMenu("Other");
//		JMenu subMenu = new JMenu("Sub Menu");
//		JMenuItem subMenuItem =new JMenuItem("Item");
//		subMenu.add(subMenuItem);
//		otherMenu.add(subMenu);

		menuBar.add(fileMenu);
//		menuBar.add(otherMenu);
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

final class ExcelFileFilter extends FileFilter{

	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) return true; //allow directories to be seen in fileChooser
		String name = file.getName();
		String extension = Utils.getFileExtension(name);
		if (extension == null) return false;
		else if (extension.equals("xlsx")) return true;
		else return false;
	}

	@Override
	public String getDescription() {
		return "Excel file (*.xlsx)";
	}

}

