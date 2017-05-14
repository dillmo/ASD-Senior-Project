package me.dillonmorse.asdseniorproject;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ASDSeniorProject extends JPanel implements Runnable, ActionListener {
	
	private static final long serialVersionUID = 1L;
	private JFileChooser fileChooser;
	private JMenuItem saveMenuItem, openMenuItem;
	private JTextArea textArea;
	
	public ASDSeniorProject() {
		super(new BorderLayout());
		
		// Set up the file chooser
		fileChooser = new JFileChooser();
		
		// Menu bar for saving and opening
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		
		// Saving
		saveMenuItem = new JMenuItem("Save");
		saveMenuItem.addActionListener(this);
		fileMenu.add(saveMenuItem);
		
		// Opening
		openMenuItem = new JMenuItem("Open");
		openMenuItem.addActionListener(this);
		fileMenu.add(openMenuItem);
		
		menuBar.add(fileMenu);
		this.add(menuBar, BorderLayout.NORTH);
		
		// Scrolling text editing field
		textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea);
		this.add(scrollPane, BorderLayout.CENTER);
	}
	
	public void run() {
		// Create the window
		JFrame frame = new JFrame("ASD Senior Project");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Add content to the window
		frame.add(new ASDSeniorProject());
		
		// Display the window
		frame.setSize(new Dimension(640, 480));
		frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		// Saving files
		if (e.getSource() == saveMenuItem) {
			int returnVal = fileChooser.showSaveDialog(ASDSeniorProject.this);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				
				// Save the file
				try {
					byte[] bytes = ASDSeniorProject.this.textArea.getText().getBytes();
					Files.write(file.toPath(), bytes);
				} catch (IOException x) {}
			}
		// Opening files
		} else if (e.getSource() == openMenuItem) {
			int returnVal = fileChooser.showOpenDialog(ASDSeniorProject.this);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				
				// Open the file
				try {
					String text = new String(Files.readAllBytes(file.toPath()));
					ASDSeniorProject.this.textArea.setText(text);
				} catch (IOException x) {}
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new ASDSeniorProject());
	}

}
