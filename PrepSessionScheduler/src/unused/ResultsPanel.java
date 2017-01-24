package gui;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.intervarsity.Parameters;

public class ResultsPanel extends JPanel {
	private JButton testButton = new JButton("test");
	public ResultsPanel(){
		super();
		
		setLayout(new FlowLayout());
		setBackground(Parameters.schemeColor2);
		add(testButton);
	}
}
