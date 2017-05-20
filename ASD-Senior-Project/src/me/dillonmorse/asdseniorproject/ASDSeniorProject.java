package me.dillonmorse.asdseniorproject;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ASDSeniorProject extends JPanel implements Runnable, ActionListener {
	
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private JFileChooser fileChooser;
	private JMenuItem saveMenuItem, openMenuItem;
	private JTextArea textArea;
	private SecureRandom rng;
	
	public ASDSeniorProject() {
		super(new BorderLayout());
		
		// Set up the file chooser
		fileChooser = new JFileChooser();
		
		// Menu bar for saving and opening
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		
		// Menu item for saving
		saveMenuItem = new JMenuItem("Save");
		saveMenuItem.addActionListener(this);
		fileMenu.add(saveMenuItem);
		
		// Menu item for opening
		openMenuItem = new JMenuItem("Open");
		openMenuItem.addActionListener(this);
		fileMenu.add(openMenuItem);
		
		// Finish setting up the menu bar
		menuBar.add(fileMenu);
		this.add(menuBar, BorderLayout.NORTH);
		
		// Scrolling text editing field
		textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea);
		this.add(scrollPane, BorderLayout.CENTER);
	}
	
	public void run() {
		// Create the window
		frame = new JFrame("ASD Senior Project");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Add content to the window
		frame.add(new ASDSeniorProject());
		
		// Display the window
		frame.setSize(new Dimension(640, 480));
		frame.setVisible(true);
	}
	
	// Gets a password from the user
	private static char[] getPassword() {
		// Ask the user for a password
		PasswordDialog dialog = new PasswordDialog(frame);
		dialog.open();
		char[] password = dialog.passwordField.getPassword();
		dialog.destruct();
		
		return password;
	}
	
	// Concatenates two byte arrays of any length
	private static byte[] byteArrayConcat(byte[] array1, byte[] array2) {
		byte[] concatenatedArray = new byte[array1.length + array2.length];
		System.arraycopy(array1, 0, concatenatedArray, 0, array1.length);
		System.arraycopy(array2, 0, concatenatedArray, array1.length, array2.length);
		
		return concatenatedArray;
	}
	
	// Generates a secret key from a password and a salt
	private static byte[] genSecret(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// Generate a 128-bit secret key using PBKDF2-HMACSHA512
		PBEKeySpec keySpec = new PBEKeySpec(password, salt, 10000, 128);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
		
		return keyFactory.generateSecret(keySpec).getEncoded();
	}
	
	// Generates an initialization vector from a password and a salt
	private static IvParameterSpec genIV(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// Generate a 128-bit secret using salted SHA256
		byte[] secret = genSecret(password, salt);
		
		// Generate an initialization vector using the secret
		IvParameterSpec iv = new IvParameterSpec(secret);
		
		return iv;
	}

	public void actionPerformed(ActionEvent e) {
		// Saving files
		if (e.getSource() == saveMenuItem) {
			int returnVal = fileChooser.showSaveDialog(ASDSeniorProject.this);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				
				// Save the file
				try {
					// Generate two unique 256-bit salts
					rng = new SecureRandom();
					byte[] keySalt = new byte[32];
					byte[] ivSalt = new byte[32];
					do {
						rng.nextBytes(keySalt);
						rng.nextBytes(ivSalt);
					} while(Arrays.equals(keySalt, ivSalt));
					
					// Generate a secret key from a user-provided password and the key salt
					char[] password = getPassword();
					byte[] secretKey = genSecret(password, keySalt);
					SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "AES");
					
					// Generate an initialization vector from the same password the IV salt
					rng.nextBytes(ivSalt);
					IvParameterSpec iv = genIV(password, ivSalt);
					
					// Initialize a cipher for encryption
					Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
					cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
					
					// Encrypt the text
					byte[] encryptedText = cipher.doFinal(ASDSeniorProject.this.textArea.getText().getBytes());
					
					// Append both salts to the text
					byte[] finalText = byteArrayConcat(byteArrayConcat(encryptedText, keySalt), ivSalt);
					
					// Write the encrypted text to the file
					Files.write(file.toPath(), finalText);
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (NoSuchAlgorithmException e1) {
					e1.printStackTrace();
				} catch (NoSuchPaddingException e1) {
					e1.printStackTrace();
				} catch (InvalidKeyException e1) {
					e1.printStackTrace();
				} catch (IllegalBlockSizeException e1) {
					e1.printStackTrace();
				} catch (BadPaddingException e1) {
					e1.printStackTrace();
				} catch (InvalidAlgorithmParameterException e1) {
					e1.printStackTrace();
				} catch (InvalidKeySpecException e1) {
					e1.printStackTrace();
				}
			}
		// Opening files
		} else if (e.getSource() == openMenuItem) {
			int returnVal = fileChooser.showOpenDialog(ASDSeniorProject.this);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				
				// Open the file
				try {
					// Read the encrypted file
					byte[] fileBytes = Files.readAllBytes(file.toPath());
					byte[] encryptedText = Arrays.copyOfRange(fileBytes, 0, fileBytes.length - 64);
					byte[] keySalt = Arrays.copyOfRange(fileBytes, fileBytes.length - 64, fileBytes.length - 32);
					byte[] ivSalt = Arrays.copyOfRange(fileBytes, fileBytes.length - 32, fileBytes.length);
					
					// Generate a secret key from a user-provided password and the key salt
					char[] password = getPassword();
					byte[] secretKey = genSecret(password, keySalt);
					SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "AES");
					
					// Generated an initialization vector using the password and the IV salt
					IvParameterSpec iv = genIV(password, ivSalt);
					
					// Initialize a cipher for decryption
					Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
					cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
					
					// Decrypt the text
					String text = new String(cipher.doFinal(encryptedText));
					
					// Display the decrypted text
					ASDSeniorProject.this.textArea.setText(text);
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (NoSuchAlgorithmException e1) {
					e1.printStackTrace();
				} catch (NoSuchPaddingException e1) {
					e1.printStackTrace();
				} catch (InvalidKeyException e1) {
					e1.printStackTrace();
				} catch (IllegalBlockSizeException e1) {
					e1.printStackTrace();
				} catch (BadPaddingException e1) {
					JOptionPane.showMessageDialog(ASDSeniorProject.this, "Incorrect password", "Error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				} catch (InvalidAlgorithmParameterException e1) {
					e1.printStackTrace();
				} catch (InvalidKeySpecException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new ASDSeniorProject());
	}

}
