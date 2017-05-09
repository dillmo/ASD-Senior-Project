package me.dillonmorse.asdseniorproject;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ASDSeniorProject implements Runnable {
	
	public void run() {
		// Create the window
		JFrame frame = new JFrame("ASD Senior Project");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Menu bar for saving and opening
		// TODO: saving and opening functions
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem saveMenuItem = new JMenuItem("Save");
		fileMenu.add(saveMenuItem);
		JMenuItem openMenuItem = new JMenuItem("Open");
		fileMenu.add(openMenuItem);
		menuBar.add(fileMenu);
		frame.setJMenuBar(menuBar);
		
		// Scrolling text editing field
		JTextArea textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea);
		frame.getContentPane().add(scrollPane);
		
		// Display the window
		frame.setSize(new Dimension(640, 480));
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new ASDSeniorProject());
	}

}
