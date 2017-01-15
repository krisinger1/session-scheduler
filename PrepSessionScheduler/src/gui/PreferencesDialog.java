package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.sun.media.jfxmedia.events.NewFrameEvent;


public class PreferencesDialog extends JDialog {

	private JButton okButton;
	private JButton cancelButton;
	private JTextField userField;
	private JPasswordField passwordField;
	private JSpinner portSpinner;
	
	private PreferencesListener preferencesListener;
	

	public PreferencesDialog(JFrame parent){
		super(parent,"Preferences",true);
		setSize(400,300);

		okButton= new JButton("OK");
		cancelButton = new JButton("Cancel");
		userField = new JTextField(10);
		passwordField = new JPasswordField(10);
		
		SpinnerNumberModel portSpinnerModel = new SpinnerNumberModel(3306, 0, 9999, 1);
		portSpinner = new JSpinner(portSpinnerModel);
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		gc.gridx = 0;
		gc.gridy = 0;
		gc.fill=GridBagConstraints.NONE;
		gc.weightx = 1;
		gc.weighty = 1;

		////////// FIRST ROW /////////////
		add(new JLabel("User Name"),gc);
		gc.gridx=1;
		add(userField,gc);

		////////// NEXT ROW /////////////
		gc.gridy=1;
		gc.gridx=0;
		add(new JLabel("Password"),gc);
		gc.gridx=1;
		add(passwordField,gc);
		
		////////// NEXT ROW //////////////
		gc.gridy++;
		gc.gridx = 0;
		add(new JLabel("Port"),gc);
		gc.gridx = 1;
		add(portSpinner,gc);
		
		////////// BUTTON ROW //////////////
		gc.gridy++;
		gc.gridx = 0;
		add(okButton,gc);
		gc.gridx = 1;
		add(cancelButton,gc);

		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
//				System.out.println("OK button");
//				System.out.println(userField.getText());
//				System.out.println(new String(passwordField.getPassword()));
//				System.out.println((Integer)portSpinner.getValue());
				if (preferencesListener != null) {
					preferencesListener.preferencesSet(userField.getText(),
							new String(passwordField.getPassword()),(Integer)portSpinner.getValue());
				}
				setVisible(false);
			}
		});

		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("cancel button");
				setVisible(false);
			}
		});
	}
	
	public void setPreferencesListener(PreferencesListener listener ){
		preferencesListener= listener;
	}
	
	public void setDefaults(String user, String password, int port) {
		userField.setText(user);
		passwordField.setText(password);
		portSpinner.setValue(new Integer(port));
		
	}
}
