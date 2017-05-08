package me.dillonmorse.asdseniorproject;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class ASDSeniorProject implements Runnable {
	
	public void run() {
		// Create the window
		JFrame frame = new JFrame("HelloWorldSwing");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Temporary "Hello, world!" label
		JLabel label = new JLabel("Hello, world!");
		frame.getContentPane().add(label);
		
		// Display the window
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new ASDSeniorProject());
	}

}
