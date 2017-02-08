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
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;

import org.intervarsity.ParameterListener;
import org.intervarsity.Parameters;

public class InputPanel extends JPanel implements ActionListener{
//public class InputPanel extends JPanel implements ActionListener, ItemListener{
	private JButton testButton = new JButton("test");
	private JButton runButton = new JButton("Run");
	private JSpinner maxStudentsSpinner,minStudentsSpinner,maxSessionsSpinner,blockSizeSpinner;
	private JSlider sldrSessions,sldrPreferred,sldrCan,sldrMust;

	private ParameterListener parameterListener;
	private InputFormListener inputFormListener;

	public InputPanel(){
		super();
		maxStudentsSpinner=new JSpinner(new SpinnerNumberModel(30,10,50,5));
		minStudentsSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 10, 1));
		maxSessionsSpinner = new JSpinner(new SpinnerNumberModel(5, 2, 10, 1));
		blockSizeSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 10, 1));

		sldrSessions = new JSlider(SwingConstants.VERTICAL, 0, 10, 5);
		sldrSessions.setMajorTickSpacing(1);
		sldrSessions.setSnapToTicks(true);
		sldrSessions.setPaintTicks(true);
		sldrSessions.setMaximum(10);
		sldrSessions.setPaintLabels(true);
		sldrSessions.setOpaque(false);
		sldrSessions.setBounds(10, 32, 43, 200);

		sldrPreferred = new JSlider(SwingConstants.VERTICAL, 0, 10, 5);
		sldrPreferred.setMajorTickSpacing(1);
		sldrPreferred.setSnapToTicks(true);
		sldrPreferred.setPaintTicks(true);
		sldrPreferred.setMaximum(10);
		sldrPreferred.setPaintLabels(true);
		sldrPreferred.setOpaque(false);
		sldrPreferred.setBounds(10, 32, 43, 200);

		sldrCan = new JSlider(SwingConstants.VERTICAL, 0, 10, 5);
		sldrCan.setMajorTickSpacing(1);
		sldrCan.setSnapToTicks(true);
		sldrCan.setPaintTicks(true);
		sldrCan.setMaximum(10);
		sldrCan.setPaintLabels(true);
		sldrCan.setOpaque(false);
		sldrCan.setBounds(10, 32, 43, 200);

		sldrMust = new JSlider(SwingConstants.VERTICAL, 0, 10, 5);
		sldrMust.setMajorTickSpacing(1);
		sldrMust.setSnapToTicks(true);
		sldrMust.setPaintTicks(true);
		sldrMust.setMaximum(10);
		sldrMust.setPaintLabels(true);
		sldrMust.setOpaque(false);
		sldrMust.setBounds(10, 32, 43, 200);

		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		setBackground(Parameters.schemeColor1);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setPreferredSize(new Dimension(400,500));

		////////// Max Students  /////////////////////////////////
		gc.gridx=0;
		gc.gridy=0;
		gc.insets= new Insets(0,0,0,5);
		gc.weightx=3;
		gc.weighty=1;
		gc.anchor=GridBagConstraints.FIRST_LINE_END;
		gc.gridwidth=3;

		add(new JLabel("Maximum number of students in a session"),gc);

		gc.gridx=3;
		gc.weightx=1;
		gc.insets= new Insets(0,0,0,0);
		gc.anchor=GridBagConstraints.FIRST_LINE_START;
		gc.gridwidth=1;

		add(maxStudentsSpinner,gc);

		////////// Min Students  /////////////////////////////////
		gc.gridx=0;
		gc.gridy++;
		gc.insets= new Insets(0,0,0,5);
		gc.weightx=1;
		gc.weighty=1;
		gc.anchor=GridBagConstraints.FIRST_LINE_END;
		gc.gridwidth=3;

		add(new JLabel("Minimum number of students in a session"),gc);

		gc.gridx=3;
		gc.insets= new Insets(0,0,0,0);
		gc.anchor=GridBagConstraints.FIRST_LINE_START;
		gc.gridwidth=1;

		add(minStudentsSpinner,gc);

		////////// Max Sessions  /////////////////////////////////
		gc.gridx=0;
		gc.gridy++;
		gc.insets= new Insets(0,0,0,5);
		gc.weightx=1;
		gc.weighty=1;
		gc.anchor=GridBagConstraints.FIRST_LINE_END;
		gc.gridwidth=3;

		add(new JLabel("Maximum number of sessions per week"),gc);

		gc.gridx=3;
		gc.insets= new Insets(0,0,0,0);
		gc.anchor=GridBagConstraints.FIRST_LINE_START;
		gc.gridwidth=1;

		add(maxSessionsSpinner,gc);

		////////// Block Size  /////////////////////////////////
		gc.gridx=0;
		gc.gridy++;
		gc.insets= new Insets(0,0,0,5);
		gc.weightx=1;
		gc.weighty=1;
		gc.anchor=GridBagConstraints.FIRST_LINE_END;
		gc.gridwidth=3;

		add(new JLabel("Number of schedule blocks needed per session"),gc);

		gc.gridx=3;
		gc.insets= new Insets(0,0,0,0);
		gc.anchor=GridBagConstraints.FIRST_LINE_START;
		gc.gridwidth=1;

		add(blockSizeSpinner,gc);

		////////// Slider labels ////////////////
		gc.gridx = 0;
		gc.gridy++;
		gc.insets= new Insets(0,0,0,5);
		gc.weightx=1;
		gc.weighty=1;
		gc.anchor=GridBagConstraints.CENTER;

		add(new JLabel("# Sessions"),gc);

		gc.gridx = 1;
		add(new JLabel("Preferred"),gc);

		gc.gridx = 2;
		add(new JLabel("# Can"),gc);

		gc.gridx = 3;
		add(new JLabel("# Must"),gc);

		////////// Sliders //////////////////////
		gc.gridx = 0;
		gc.gridy++;
		gc.insets= new Insets(0,0,0,5);
		gc.weightx=1;
		gc.weighty=1;
		gc.anchor=GridBagConstraints.CENTER;

		add(sldrSessions,gc);

		gc.gridx=1;
		add(sldrPreferred,gc);

		gc.gridx=2;
		add(sldrCan,gc);

		gc.gridx=3;
		add(sldrMust,gc);

		////////// Button //////////////////////
		gc.gridx=2;
		gc.gridy++;

		gc.insets= new Insets(0,0,0,5);
		gc.weightx=1;
		gc.weighty=25;
		gc.anchor=GridBagConstraints.FIRST_LINE_END;
		add(runButton,gc);

		gc.gridx=3;
		gc.insets= new Insets(0,0,0,0);
		gc.weightx=1;
		gc.weighty=25;
		gc.anchor=GridBagConstraints.FIRST_LINE_START;

		//add(testButton,gc);

	//testButton.addActionListener(this);
		runButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand()=="test"){
			int maxStudents = (int) maxStudentsSpinner.getValue();
			//System.out.println("input panel: maxstudents  "+maxStudents);

//			InputEvent event = new InputEvent(this, (int) maxStudentsSpinner.getValue(), (int) minStudentsSpinner.getValue(), (int) maxSessionsSpinner.getValue(), (int) blockSizeSpinner.getValue());
//			if (inputFormListener != null){
//				inputFormListener.inputFormEventOccurred(event);
//			}
		}
		else if (ae.getActionCommand()=="Run"){
			//System.out.println("Input Panel: Run Button pressed");

			InputEvent event = new InputEvent(this, (int) maxStudentsSpinner.getValue(), (int) minStudentsSpinner.getValue(), (int) maxSessionsSpinner.getValue(), (int) blockSizeSpinner.getValue(),
								(int)sldrSessions.getValue(), (int)sldrPreferred.getValue(),(int)sldrCan.getValue(),(int)sldrMust.getValue());
			if (inputFormListener != null){
				inputFormListener.inputFormEventOccurred(event);
			}
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
