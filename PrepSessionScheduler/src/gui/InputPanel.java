package gui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;

import org.intervarsity.ParameterListener;
import org.intervarsity.Parameters;

import unused.InputEvent;
import unused.InputFormListener;

public class InputPanel extends JPanel implements ActionListener{
//public class InputPanel extends JPanel implements ActionListener, ItemListener{
	private JButton testButton = new JButton("test");
	private JButton runButton = new JButton("Run");
	private JSpinner maxStudentsSpinner,minStudentsSpinner,maxSessionsSpinner,blockSizeSpinner;

	private ParameterListener parameterListener;
	private InputFormListener inputFormListener;

	public InputPanel(){
		super();
		maxStudentsSpinner=new JSpinner(new SpinnerNumberModel(30,10,50,5));
		minStudentsSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 10, 1));
		maxSessionsSpinner = new JSpinner(new SpinnerNumberModel(5, 2, 10, 1));
		blockSizeSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 10, 1));

		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		setBackground(Parameters.schemeColor1);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setPreferredSize(new Dimension(400,500));

		////////// Max Students  /////////////////////////////////
		gc.gridx=0;
		gc.gridy=0;
		gc.insets= new Insets(0,0,0,5);
		gc.weightx=1;
		gc.weighty=1;
		gc.anchor=GridBagConstraints.FIRST_LINE_END;

		add(new JLabel("Maximum number of students in a session"),gc);

		gc.gridx=1;
		gc.insets= new Insets(0,0,0,0);
		gc.anchor=GridBagConstraints.FIRST_LINE_START;

		add(maxStudentsSpinner,gc);

		////////// Min Students  /////////////////////////////////
		gc.gridx=0;
		gc.gridy++;
		gc.insets= new Insets(0,0,0,5);
		gc.weightx=1;
		gc.weighty=1;
		gc.anchor=GridBagConstraints.FIRST_LINE_END;

		add(new JLabel("Minimum number of students in a session"),gc);

		gc.gridx=1;
		gc.insets= new Insets(0,0,0,0);
		gc.anchor=GridBagConstraints.FIRST_LINE_START;

		add(minStudentsSpinner,gc);

		////////// Max Sessions  /////////////////////////////////
		gc.gridx=0;
		gc.gridy++;
		gc.insets= new Insets(0,0,0,5);
		gc.weightx=1;
		gc.weighty=1;
		gc.anchor=GridBagConstraints.FIRST_LINE_END;

		add(new JLabel("Maximum number of sessions per week"),gc);

		gc.gridx=1;
		gc.insets= new Insets(0,0,0,0);
		gc.anchor=GridBagConstraints.FIRST_LINE_START;

		add(maxSessionsSpinner,gc);

		////////// Block Size  /////////////////////////////////
		gc.gridx=0;
		gc.gridy++;
		gc.insets= new Insets(0,0,0,5);
		gc.weightx=1;
		gc.weighty=1;
		gc.anchor=GridBagConstraints.FIRST_LINE_END;

		add(new JLabel("Number of schedule blocks needed per session"),gc);

		gc.gridx=1;
		gc.insets= new Insets(0,0,0,0);
		gc.anchor=GridBagConstraints.FIRST_LINE_START;

		add(blockSizeSpinner,gc);

		////////// Button //////////////////////
		gc.gridx=0;
		gc.gridy++;

		gc.insets= new Insets(0,0,0,5);
		gc.weightx=1;
		gc.weighty=1;
		gc.anchor=GridBagConstraints.FIRST_LINE_END;
		add(runButton,gc);

		gc.gridx=1;
		gc.insets= new Insets(0,0,0,0);
		gc.weightx=1;
		gc.weighty=25;
		gc.anchor=GridBagConstraints.FIRST_LINE_START;

		add(testButton,gc);

		testButton.addActionListener(this);
		runButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand()=="test"){
			InputEvent event = new InputEvent(this, (int) maxStudentsSpinner.getValue());
			if (inputFormListener != null){
				inputFormListener.inputFormEventOccurred(event);
			}
			int maxStudents = (int) maxStudentsSpinner.getValue();
			System.out.println(maxStudents);
		}
		else if (ae.getActionCommand()=="Run"){
			System.out.println("Run Button pressed");
		}
	}

//	@Override
//	public void itemStateChanged(ItemEvent ie) {
//		System.out.println("item selected: "+ie.getItem().toString());
//		if (parameterListener != null) parameterListener.parameterChanged((int)ie.getItem(),(ie.getSource()).toString());
//	}

	public void setParameterListener(ParameterListener listener){
		this.parameterListener = listener;
	}

	public void setInputFormListener(InputFormListener listener){
		this.inputFormListener=listener;
	}
}
