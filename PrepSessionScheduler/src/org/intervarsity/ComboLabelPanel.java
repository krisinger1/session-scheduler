package org.intervarsity;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ComboLabelPanel<E> extends JPanel {
	private static final long serialVersionUID = 1L;
	private JComboBox<E> comboBox;
	private JLabel label;

	//ComboLabelPanel(String labelText,Integer[] items){
	ComboLabelPanel(String text, E[] e){
		super();
		//eArray=e;
		comboBox = new JComboBox<E>();
		comboBox.setModel(new DefaultComboBoxModel<E>(e));
		comboBox.setSelectedIndex(3);
		comboBox.setMaximumSize(new Dimension(45,25));
		comboBox.setPreferredSize(new Dimension(45,25));
		label=new JLabel(text);

		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		setAlignmentX(Component.RIGHT_ALIGNMENT);
		setBackground(Parameters.schemeColor1);

		add(label);
		add(Box.createRigidArea(new Dimension(10,0)));
		add(comboBox);
	}
	
	public JComboBox<E> getComboBox(){
		return comboBox;
	}
	
	public E getSelectedItem(){
		return (E)comboBox.getSelectedItem();
	}
}
