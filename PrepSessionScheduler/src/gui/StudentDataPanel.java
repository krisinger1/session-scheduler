package gui;

import java.awt.Dimension;
import java.awt.Event;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.intervarsity.Parameters;

import model.Student;

public class StudentDataPanel extends JPanel implements ActionListener{
	private int id=-1;
	private JLabel idLabel;
	private JTextField fNameTxt;
	private JTextField lNameTxt;
	private JTextField emailTxt;
	private JTextField areaTxt;
	private ScheduleInputPanel schedPanel;
	int[][] days = new int[3][17];
	private JButton saveButton;
	private JButton newButton;
	private JButton doneButton;
	private StudentFormListener studentFormListener;
	private boolean dirty = false;

	public StudentDataPanel(){
		super();
		setLayout(new GridBagLayout());
		GridBagConstraints gc= new GridBagConstraints();
		setPreferredSize(new Dimension(400,500));

		fNameTxt=new JTextField(15);
		lNameTxt=new JTextField(15);
		emailTxt = new JTextField(15);
		areaTxt = new JTextField(15);
		schedPanel = new ScheduleInputPanel();
		idLabel=new JLabel("ID= "+id);

		fNameTxt.getDocument().addDocumentListener(new MyDocumentListener());
		lNameTxt.getDocument().addDocumentListener(new MyDocumentListener());
		emailTxt.getDocument().addDocumentListener(new MyDocumentListener());
		schedPanel.addScheduleChangeListener(new ScheduleChangeListener() {

			@Override
			public void scheduleChanged() {
				dirty=true;
			    System.out.println("schedule dirty: "+ dirty);
			    saveButton.setEnabled(true);
			}
		});

		for (int i=0;i<17;i++){
			for (int j=0;j<3;j++){
				days[j][i]=0;
			}
		}

		schedPanel.setData(days);
		saveButton = new JButton("Save Student");
		saveButton.setEnabled(false);
		newButton = new JButton("New");
		doneButton = new JButton("Done");

		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setBackground(Parameters.schemeColor1);

		//////////////// First Name ////////////////////////////
		gc.gridx=0;
		gc.gridy=0;
		gc.insets= new Insets(0,0,0,5);

		gc.weightx=1;
		gc.weighty=1;
		gc.anchor=GridBagConstraints.FIRST_LINE_END;

		add(new JLabel("First Name"),gc);

		gc.anchor=GridBagConstraints.FIRST_LINE_START;

		gc.gridx=1;
		gc.gridy=0;
		gc.gridwidth=2;
		gc.weightx=1;
		gc.weighty=1;
		add(fNameTxt,gc);

		//////////////// Last Name ////////////////////////////
		gc.gridx=0;
		gc.gridy++;
		gc.gridwidth=1;
		gc.insets= new Insets(0,0,0,5);

		gc.weightx=1;
		gc.weighty=1;
		gc.anchor=GridBagConstraints.FIRST_LINE_END;

		add(new JLabel("Last Name"),gc);

		gc.anchor=GridBagConstraints.FIRST_LINE_START;
		gc.gridx=1;
		gc.gridwidth=2;
		gc.weightx=1;
		gc.weighty=1;
		add(lNameTxt,gc);

		/////////////////// Email /////////////////////////////

		gc.gridx=0;
		gc.gridy++;
		gc.anchor=GridBagConstraints.FIRST_LINE_END;
		gc.gridwidth=1;
		gc.weightx=1;
		gc.weighty=1;
		add(new JLabel("Email"),gc);

		gc.anchor=GridBagConstraints.FIRST_LINE_START;
		gc.gridx=1;
		gc.gridwidth=2;
		gc.weightx=1;
		gc.weighty=1;
		add(emailTxt,gc);

		/////////////////// Area /////////////////////////////

		gc.gridx=0;
		gc.gridy++;
		gc.anchor=GridBagConstraints.FIRST_LINE_END;
		gc.gridwidth=1;
		gc.weightx=1;
		gc.weighty=1;
		add(new JLabel("Area"),gc);

		gc.anchor=GridBagConstraints.FIRST_LINE_START;
		gc.gridx=1;
		gc.gridwidth=2;
		gc.weightx=1;
		gc.weighty=1;
		add(areaTxt,gc);
		/////////////////// Schedule /////////////////////////////

		gc.gridx=0;
		gc.gridy++;
		gc.anchor=GridBagConstraints.FIRST_LINE_END;
		gc.gridwidth=1;
		gc.weightx=1;
		gc.weighty=1;
		add(new JLabel("Schedule"),gc);


		gc.gridx=1;
		gc.gridy++;
		gc.gridwidth=2;
		gc.anchor=GridBagConstraints.FIRST_LINE_START;
		gc.weightx=5;
		gc.weighty=3;
		add(schedPanel,gc);
		//add(new JScrollPane(scheduleTable),gc);

		///////////////////// Button ///////////////////////////
		gc.gridx=0;
		gc.gridy++;
		gc.weighty=1;
		gc.gridwidth=1;
		//add(idLabel,gc);
		add(saveButton,gc);

		gc.gridx=1;
		//gc.gridy++;
		gc.weighty=1;
		gc.gridwidth=1;
		gc.anchor=GridBagConstraints.NORTH;
		add(newButton,gc);

		gc.gridx=2;
		//gc.gridy++;
		gc.weighty=1;
		gc.gridwidth=1;
		gc.anchor=GridBagConstraints.FIRST_LINE_START;
		//add(idLabel,gc);
		add(doneButton,gc);

		////////////////
		gc.gridx=0;
		gc.gridy++;
		add(idLabel,gc);

		saveButton.addActionListener(this);
		newButton.addActionListener(this);
		doneButton.addActionListener(this);
		//TODO how to get ENTER key to trigger button event - not sure this is possible here. needs to be from jframe
	}

	public void setStudentFormListener(StudentFormListener listener) {
		this.studentFormListener=listener;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand()=="New"){
			if (!dirty){
				resetForm();
			}
			else {
				int ans = JOptionPane.showConfirmDialog(this, "Data not saved. Continue anyway?", "Data not saved", JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
				if (ans==JOptionPane.OK_OPTION) resetForm();
			}
		}
		else if (e.getActionCommand()=="Save Student"){
			if (!fNameTxt.getText().equals("") && !lNameTxt.getText().equals("")){
				StudentDataEvent event = new StudentDataEvent(this, id, fNameTxt.getText(), lNameTxt.getText(), emailTxt.getText(),areaTxt.getText(),schedPanel.getData() );
				//System.out.println("actionPerformed "+days[0][0]+" "+days[0][1]);
				if (studentFormListener != null){
					studentFormListener.StudentFormEventOccurred(event);
				}
				resetForm();
			}
			else {
				JOptionPane.showMessageDialog(this, "Please enter a first and last name.", "Warning", JOptionPane.OK_OPTION);
			}
		}
		else if (e.getActionCommand()=="Done"){
			//TODO implement Done button
			System.out.println("Done button pressed");
		}
	}

	public void populateForm(Student student){
		id=student.getId();
		idLabel.setText("ID= "+id);
		fNameTxt.setText(student.getFName());
		lNameTxt.setText(student.getLName());
		emailTxt.setText(student.getEmail());
		areaTxt.setText(student.getArea());
		days=student.getSchedule();
		schedPanel.setData(days);
		//System.out.println("populateForm: "+days[0][0]+" "+days[0][1]);
		schedPanel.clear();
		dirty=false;
		saveButton.setEnabled(false);
	}

	public void resetForm(){
		id=-1;
		idLabel.setText("ID= "+id);
		fNameTxt.setText("");
		lNameTxt.setText("");
		emailTxt.setText("");
		areaTxt.setText("");
		schedPanel.setData(new int[3][17]);
		schedPanel.clear();
		dirty=false;
		saveButton.setEnabled(false);
	}

	private class MyDocumentListener implements DocumentListener{

		@Override
		public void changedUpdate(DocumentEvent e) {
			dirty=true;
		    //System.out.println("document listener changeupdate dirty: "+ dirty);
		    saveButton.setEnabled(true);
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			dirty=true;
		    //System.out.println("document listener insertupdate dirty: "+ dirty);
		    saveButton.setEnabled(true);
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			dirty=true;
		    //System.out.println("documentlistener removeupdate dirty: "+ dirty);
		    saveButton.setEnabled(true);
		}

	}
}
