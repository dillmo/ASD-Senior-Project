package me.dillonmorse.asdseniorproject;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

public class PasswordDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	public JPasswordField passwordField;
	
	public PasswordDialog(JFrame owner) {
		super(owner, true);
		
		this.setLayout(new BorderLayout());
		
		// Create the password label
		JLabel label = new JLabel("Password: ");
		this.getContentPane().add(label, BorderLayout.WEST);
		
		// Create the password field
		passwordField = new JPasswordField(10);
		this.getContentPane().add(passwordField, BorderLayout.CENTER);
		
		// Create the Submit button
		JButton button = new JButton("Submit");
		button.addActionListener(this);
		this.getContentPane().add(button, BorderLayout.EAST);
		
		this.pack();
	}

	public void actionPerformed(ActionEvent arg0) {
		this.setVisible(false);
	}
	
	public void open() {
		this.setVisible(true);
	}
	
	public void destruct() {
		this.dispose();
	}

}
