package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.intervarsity.Parameters;

import model.Student;

public class StudentDataPanel extends JPanel implements ActionListener{
	private int id=-1;
	private JLabel idLabel;
	private JTextField fNameTxt;
	private JTextField lNameTxt;
	private JTextField emailTxt;
	private ScheduleInputPanel schedPanel;
	int[][] days = new int[3][17];
	private JButton addButton;
	private StudentFormListener studentFormListener;
//	private JTable scheduleTable;
//	private ScheduleTableModel scheduleTableModel;

	public StudentDataPanel(){
		super();
		setLayout(new GridBagLayout());
		GridBagConstraints gc= new GridBagConstraints();
		setPreferredSize(new Dimension(400,500));

		fNameTxt=new JTextField(15);
		lNameTxt=new JTextField(15);
		emailTxt = new JTextField(15);
		schedPanel = new ScheduleInputPanel();
		idLabel=new JLabel("ID= "+id);
//		scheduleTableModel = new ScheduleTableModel();
//		scheduleTable = new JTable(scheduleTableModel);
//		scheduleTable.setDefaultRenderer(Object.class, new ScheduleTableRenderer());

		

		//int[][] days = new int[3][17];
		for (int i=0;i<17;i++){
			for (int j=0;j<3;j++){
				days[j][i]=0;
			}
		}
		//scheduleTableModel.setData(days);

		schedPanel.setData(days);
		addButton = new JButton("Add Student");


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
		gc.weightx=1;
		gc.weighty=1;
		add(fNameTxt,gc);

		//////////////// Last Name ////////////////////////////
		gc.gridx=0;
		gc.gridy++;
		gc.insets= new Insets(0,0,0,5);

		gc.weightx=1;
		gc.weighty=1;
		gc.anchor=GridBagConstraints.FIRST_LINE_END;

		add(new JLabel("Last Name"),gc);

		gc.anchor=GridBagConstraints.FIRST_LINE_START;
		gc.gridx=1;
		gc.weightx=1;
		gc.weighty=1;
		add(lNameTxt,gc);

		/////////////////// Email /////////////////////////////

		gc.gridx=0;
		gc.gridy++;
		gc.anchor=GridBagConstraints.FIRST_LINE_END;
		gc.weightx=1;
		gc.weighty=1;
		add(new JLabel("Email"),gc);

		gc.anchor=GridBagConstraints.FIRST_LINE_START;
		gc.gridx=1;
		gc.weightx=1;
		gc.weighty=1;
		add(emailTxt,gc);

		/////////////////// Schedule /////////////////////////////

		gc.gridx=0;
		gc.gridy++;
		gc.anchor=GridBagConstraints.FIRST_LINE_END;
		gc.weightx=1;
		gc.weighty=1;
		add(new JLabel("Schedule"),gc);
		//gc.gridwidth=2;
		gc.gridx=1;
		gc.gridy++;
		gc.anchor=GridBagConstraints.FIRST_LINE_START;
		gc.weightx=10;
		gc.weighty=10;
		add(schedPanel,gc);
		//add(new JScrollPane(scheduleTable),gc);

		///////////////////// Button ///////////////////////////
		gc.gridx=0;
		gc.gridy++;	
		add(idLabel,gc);
		
		gc.gridx=1;
		//gc.gridy++;
		gc.weighty=5;
		add(addButton,gc);

		addButton.addActionListener(this);
		//TODO how to get ENTER key to trigger button event
	}

	public void setStudentFormListener(StudentFormListener listener) {
		this.studentFormListener=listener;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
			StudentDataEvent event = new StudentDataEvent(this, id, fNameTxt.getText(), lNameTxt.getText(), emailTxt.getText(),schedPanel.getData() );
			System.out.println("actionPerformed "+days[0][0]+" "+days[0][1]);
			if (studentFormListener != null){
				studentFormListener.StudentFormEventOccurred(event);
			}
			resetForm();
	}

	public void populateForm(Student student){
		id=student.getId();
		idLabel.setText("ID= "+id);
		fNameTxt.setText(student.getFName());
		lNameTxt.setText(student.getLName());
		emailTxt.setText(student.getEmail());
		days=student.getSchedule();
		schedPanel.setData(days);
		System.out.println("populateForm: "+days[0][0]+" "+days[0][1]);
		schedPanel.refresh();
	}

	public void resetForm(){
		id=-1;
		idLabel.setText("ID= "+id);
		fNameTxt.setText("");
		lNameTxt.setText("");
		emailTxt.setText("");
//		for (int i=0;i<17;i++){
//			for (int j=0;j<3;j++){
//				//days[j][i]=0;
//			}
//		}
		schedPanel.setData(new int[3][17]);
	}
}
