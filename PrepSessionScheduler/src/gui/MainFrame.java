package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;

import controller.Controller;
import unused.InputEvent;
import unused.InputFormListener;
import unused.InputPanel;
import unused.ResultsPanel;

public class MainFrame extends JFrame {
	//private InputPanel inputPanel = new InputPanel();
	private ResultsPanel resultsPanel = new ResultsPanel();
	private StudentDataPanel studentDataPanel = new StudentDataPanel();
	private TablePanel tablePanel=new TablePanel();
	private ScheduleInputPanel scheduleInputPanel=new ScheduleInputPanel();
	private PreferencesDialog preferencesDialog;
	private Preferences preferences;
	private int maxStudents;
	private JFileChooser fileChooser;
	private Controller controller;


	public MainFrame(String title){
		super(title);
		controller = new Controller();
		preferencesDialog = new PreferencesDialog(this);
		preferences=Preferences.userRoot().node("db");

		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new MyFileFilter());
		//fileChooser.addChoosableFileFilter(new MyFileFilter());
		setJMenuBar(createMenuBar());
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(1500,700));
		setMinimumSize(new Dimension(1000,700));
		//TODO learn how to use setImageIcon()
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

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

		studentDataPanel.setStudentFormListener(new StudentFormListener(){

			@Override
			public void StudentFormEventOccurred(StudentDataEvent e) {
				if (e.getId()==-1 && !e.getFName().equals("") && !e.getLName().equals("")){
					System.out.println("StudentFormEvent: "+e.getSchedule()[0][0]+" "+e.getSchedule()[0][1]);
					controller.addStudent(e.getFName(),e.getLName(), e.getEmail(),e.getSchedule());
					System.out.println("after controller: "+controller.getStudents().get(0));

					tablePanel.refresh();
				}
				else if (!e.getFName().equals("") && !e.getLName().equals("")){
					controller.updateStudent(e.getId(), e.getFName(),e.getLName(), e.getEmail(),e.getSchedule());
					System.out.println("StudentFormEvent: "+e.getSchedule()[0][0]+" "+e.getSchedule()[0][1]);
					tablePanel.refresh();
				}
			}

		});

		tablePanel.setData(controller.getStudents());

		tablePanel.setTableListener(new TableListener(){

			@Override
			public void rowDeleted(int row) {
				controller.removeStudent(row);
			}

			@Override
			public void rowSelected(int row) {
				studentDataPanel.populateForm(controller.getStudents().get(row));
				System.out.println("rowSelected: "+controller.getStudents().get(row).toString());
			}

		});

//		inputPanel.setVisible(false);
//		add(inputPanel,BorderLayout.WEST);
		add(studentDataPanel,BorderLayout.WEST);
		//add(resultsPanel,BorderLayout.EAST);
		add(tablePanel,BorderLayout.CENTER);
		//add(scheduleInputPanel,BorderLayout.EAST);
	}

	private JMenuBar createMenuBar(){
		JMenuBar menuBar = new JMenuBar();


		JMenu fileMenu = new JMenu("File");
		JMenuItem importMenuItem = new JMenuItem("Import File...");
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		});

		exportMenuItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int choice = fileChooser.showSaveDialog(MainFrame.this);
				if (choice==JFileChooser.APPROVE_OPTION){
					try {
						controller.saveToFile(fileChooser.getSelectedFile());
					} catch (IOException e) {
						JOptionPane.showMessageDialog(MainFrame.this, "Could not save file", "Error", JOptionPane.ERROR_MESSAGE);
						// TODO Auto-generated catch block
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
