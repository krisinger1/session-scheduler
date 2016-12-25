package gui;

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
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import org.intervarsity.ParameterListener;
import org.intervarsity.Parameters;

public class InputPanel extends JPanel implements ActionListener, ItemListener{
	private JButton testButton = new JButton("test");
	private JComboBox<Integer> comboMaxStudents,comboMinStudents,comboBlockSize,comboMaxSessions;
	private JLabel lblMaxStudents,lblMinStudents,lblMaxSessions,lblBlockSize;
	//TODO change these variable names
//	private ComboLabelPanel<Integer> maxStudents=new ComboLabelPanel<Integer>("max students",new Integer[] {15,20,25,30,35});
//	private ComboLabelPanel<Integer> minStudents=new ComboLabelPanel<Integer>("minimum students",new Integer[] {1,2,3,4,5,6,7,8,9,10});
//	private ComboLabelPanel<Integer> maxSessions=new ComboLabelPanel<Integer>("maximum sessions to schedule per week",new Integer[] {1,2,3,4,5,6,7});
//	private ComboLabelPanel<Integer> blockSize=new ComboLabelPanel<Integer>("how many blocks a session needs",new Integer[] {1,2,3,4,5});

	private ParameterListener parameterListener;
	private InputFormListener inputFormListener;

	InputPanel(){
		super();

		lblMaxStudents =new JLabel("Maximum number of students in a session");
		comboMaxStudents = new JComboBox<Integer>(new Integer[] {15,20,25,30,35});
		lblMinStudents =new JLabel("Minimum number of students in a session");
		comboMinStudents = new JComboBox<Integer>(new Integer[] {1,2,3,4,5,6,7,8,9,10});
		lblMaxSessions =new JLabel("Maximum number of sessions per week");
		comboMaxSessions = new JComboBox<Integer>(new Integer[] {1,2,3,4,5,6,7});
		lblBlockSize =new JLabel("Number of schedule blocks needed per session");
		comboBlockSize = new JComboBox<Integer>(new Integer[] {1,2,3,4,5});

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

		add(lblMaxStudents,gc);

		gc.gridx=1;
		gc.gridy=0;
		gc.insets= new Insets(0,0,0,0);
		gc.anchor=GridBagConstraints.FIRST_LINE_START;

		comboMaxStudents.setSelectedIndex(3);
		add(comboMaxStudents,gc);

		////////// Min Students  /////////////////////////////////
		gc.gridx=0;
		gc.gridy=1;
		gc.insets= new Insets(0,0,0,5);
		gc.weightx=1;
		gc.weighty=1;
		gc.anchor=GridBagConstraints.FIRST_LINE_END;

		add(lblMinStudents,gc);

		gc.gridx=1;
		gc.gridy=1;
		gc.insets= new Insets(0,0,0,0);
		gc.anchor=GridBagConstraints.FIRST_LINE_START;

		comboMinStudents.setSelectedIndex(3);
		add(comboMinStudents,gc);

		////////// Max Sessions  /////////////////////////////////
		gc.gridx=0;
		gc.gridy=2;
		gc.insets= new Insets(0,0,0,5);
		gc.weightx=1;
		gc.weighty=1;
		gc.anchor=GridBagConstraints.FIRST_LINE_END;

		add(lblMaxSessions,gc);

		gc.gridx=1;
		gc.gridy=2;
		gc.insets= new Insets(0,0,0,0);
		gc.anchor=GridBagConstraints.FIRST_LINE_START;

		comboMaxSessions.setSelectedIndex(4);
		add(comboMaxSessions,gc);

		////////// Block Size  /////////////////////////////////
		gc.gridx=0;
		gc.gridy=3;
		gc.insets= new Insets(0,0,0,5);
		gc.weightx=1;
		gc.weighty=1;
		gc.anchor=GridBagConstraints.FIRST_LINE_END;

		add(lblBlockSize,gc);

		gc.gridx=1;
		gc.gridy=3;
		gc.insets= new Insets(0,0,0,0);
		gc.anchor=GridBagConstraints.FIRST_LINE_START;

		comboBlockSize.setSelectedIndex(2);
		add(comboBlockSize,gc);

		////////// Button //////////////////////
		gc.gridx=1;
		gc.gridy=4;
		gc.insets= new Insets(0,0,0,0);
		gc.weightx=1;
		gc.weighty=25;
		gc.anchor=GridBagConstraints.FIRST_LINE_START;

		add(testButton,gc);

		testButton.addActionListener(this);


//		maxStudents.getComboBox().addItemListener(this);

//		lblMaxStudents = new JLabel("Max students per session:");
//		lblMinStudents = new JLabel("Min students per session:");
//		lblMaxSessions = new JLabel("Max sessions to schedule per week:");
//		lblBlockSize = new JLabel("Number of blocks needed per session:");
//
//		comboMaxStudents = new JComboBox<Integer>();
//		comboMaxStudents.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {15,20,25,30,35}));
//		comboMaxStudents.setSelectedIndex(3);
//		comboMaxStudents.setMaximumSize(new Dimension(75,75));
//
//		comboMinStudents = new JComboBox<Integer>();
//		comboMinStudents.setModel(new DefaultComboBoxModel(new Integer[] {1,2,3,4,5,6,7,8,9,10}));
//		comboMinStudents.setSelectedIndex(3);
//
//		comboMaxSessions = new JComboBox<Integer>();
//		comboMaxSessions.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {1,2,3,4,5,6,7}));
//		comboMaxSessions.setSelectedIndex(4);
//
//		comboBlockSize = new JComboBox<Integer>();
//		comboBlockSize.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {1,2,3,4,5}));
//		comboBlockSize.setSelectedIndex(2);

//		  comboMaxStudents.addItemListener(new ItemListener() {
//			public void itemStateChanged(ItemEvent ie) {
//				//maxStudents=(int)comboMaxStudents.getSelectedItem();
//				//formChanged=true;
//			}
//		});


//		add(Box.createRigidArea(new Dimension(0,10)));
//
//		add(Box.createRigidArea(new Dimension(0,5)));
//		add(minStudents);
//		add(Box.createRigidArea(new Dimension(0,5)));
//		add(maxSessions);
//		add(Box.createRigidArea(new Dimension(0,5)));
//		add(blockSize);
//		blockSize.setVisible(false);
//		add(testButton);

	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		InputEvent event = new InputEvent(this, (int) comboMaxStudents.getSelectedItem());
		if (inputFormListener != null){
			inputFormListener.inputFormEventOccurred(event);
		}
		int maxStudents = (int) comboMaxStudents.getSelectedItem();
		System.out.println(maxStudents);
	}

	@Override
	public void itemStateChanged(ItemEvent ie) {
		System.out.println("item selected: "+ie.getItem().toString());
		if (parameterListener != null) parameterListener.parameterChanged((int)ie.getItem(),(ie.getSource()).toString());
	}

	public void setParameterListener(ParameterListener listener){
		this.parameterListener = listener;
	}

	public void setInputFormListener(InputFormListener listener){
		this.inputFormListener=listener;
	}
}
