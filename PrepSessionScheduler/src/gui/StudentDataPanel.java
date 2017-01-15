package gui;

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
import javax.swing.JTextField;

import org.intervarsity.Parameters;

public class StudentDataPanel extends JPanel implements ActionListener{
	private JTextField fNameTxt;
	private JTextField lNameTxt;
	private JTextField emailTxt;
	private JButton addButton;
	private StudentFormListener studentFormListener;

	public StudentDataPanel(){
		super();
		setLayout(new GridBagLayout());
		GridBagConstraints gc= new GridBagConstraints();
		setPreferredSize(new Dimension(400,500));

		fNameTxt=new JTextField(15);
		lNameTxt=new JTextField(15);
		emailTxt = new JTextField(15);
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

		///////////////////// Button ///////////////////////////

		gc.gridx=1;
		gc.gridy++;
		gc.weighty=15;
		add(addButton,gc);

		addButton.addActionListener(this);
		//TODO how to get ENTER key to trigger button event
	}

	public void setStudentFormListener(StudentFormListener listener) {
		this.studentFormListener=listener;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
			StudentDataEvent event = new StudentDataEvent(this, fNameTxt.getText(), lNameTxt.getText(), emailTxt.getText());
			if (studentFormListener != null){
				studentFormListener.StudentFormEventOccurred(event);
			}
			resetForm();
	}

	public void resetForm(){
		fNameTxt.setText("");
		lNameTxt.setText("");
		emailTxt.setText("");
	}
}
