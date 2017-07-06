package gui;

import java.awt.Dimension;
import java.awt.Event;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

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
	int[][] days;
	private JButton saveButton;
	private JButton newButton;
	private JButton doneButton;
	private StudentFormListener studentFormListener;
	private boolean dirty = false;

	//TODO add Undo button to student data entry panel
	public StudentDataPanel(String[] timesStrings,String[] dayStrings, int[][] mask){
		super();
		setLayout(new GridBagLayout());
		GridBagConstraints gc= new GridBagConstraints();
		setPreferredSize(new Dimension(400,500));

		fNameTxt=new JTextField(15);
		lNameTxt=new JTextField(15);
		emailTxt = new JTextField(15);
		areaTxt = new JTextField(15);
		schedPanel = new ScheduleInputPanel(timesStrings,dayStrings);
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
		days=new int[dayStrings.length][timesStrings.length];
//		for (int i=0;i<timesStrings.length;i++){
//			for (int j=0;j<dayStrings.length;j++){
//				days[j][i]=0;
//			}
//		}

		/// temporary code for mask
		//TODO implement better method of setting mask
		//int[][] mask =new int[dayStrings.length][timesStrings.length];

//		for (int i=0;i<timesStrings.length;i++){
//			for (int j=0;j<dayStrings.length;j++){
//				mask[j][i]=0;
//			}
//		}
//		mask[0][0]=1;
//		mask[0][1]=1;
//		mask[0][2]=1;
//		mask[0][3]=1;
//		mask[0][4]=1;
//		mask[0][5]=1;
//		mask[0][6]=1;
//		mask[0][7]=1;

		schedPanel.setMask(mask);

		schedPanel.setData(days);

		saveButton = new JButton("Save Student");
		saveButton.setEnabled(false);
		newButton = new JButton("Add New Student");
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
		add(saveButton,gc);

		gc.gridx=1;
		gc.weighty=1;
		gc.gridwidth=1;
		gc.anchor=GridBagConstraints.NORTH;
		add(newButton,gc);

		gc.gridx=2;
		gc.weighty=1;
		gc.gridwidth=1;
		gc.anchor=GridBagConstraints.FIRST_LINE_START;
		add(doneButton,gc);

		////////////////
		gc.gridx=0;
		gc.gridy++;
		//add(saveFileButton,gc);

		saveButton.addActionListener(this);
		newButton.addActionListener(this);
		doneButton.addActionListener(this);
		//TODO how to get ENTER key to trigger button event - not sure this is possible here. needs to be from jframe
	}

	public void setMask(int[][] mask){
		schedPanel.setMask(mask);
	}

	public void setStudentFormListener(StudentFormListener listener) {
		this.studentFormListener=listener;
	}

	public boolean isDirty(){
		return dirty;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//TODO if not saving data reset schedule to what it was
		if (e.getActionCommand()=="Add New Student"){
			if (!dirty){
				resetForm();
			}
			else {
				int ans = JOptionPane.showConfirmDialog(this, "Student not saved. Continue anyway?", "Data not saved", JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
				if (ans==JOptionPane.OK_OPTION) resetForm();
			}
		}
		else if (e.getActionCommand()=="Save Student"){
			if (!fNameTxt.getText().equals("") && !lNameTxt.getText().equals("")){
				StudentDataEvent event = new StudentDataEvent(this, id, fNameTxt.getText(), lNameTxt.getText(), emailTxt.getText(),areaTxt.getText(),schedPanel.getData() );
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

			if (studentFormListener !=null){
				if (dirty) {
					int ans = JOptionPane.showConfirmDialog(this, "Student not saved. Continue anyway?", "Data not saved", JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
					if (ans!=JOptionPane.OK_OPTION) return;
				}
				studentFormListener.doneEventOccurred();
			}
		}

	}

	public void populateForm(Student student){
		id=student.getId();
		idLabel.setText("ID= "+id);
		fNameTxt.setText(student.getFName());
		lNameTxt.setText(student.getLName());
		emailTxt.setText(student.getEmail());
		areaTxt.setText(student.getArea());
		//days=student.getSchedule();
		days=Utils.copyOf(student.getSchedule());
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
