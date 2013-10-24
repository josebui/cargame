package cargame.desktop.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;

import javax.swing.Action;

public class CarGameUI {

	private JFrame frmCarGame;
	private JTextField ipTextField;
	private JTextField portTextField;
	private final Action startProcess = new SwingAction(this);
	private StartGame startGame = null;
	private JButton btnConnectPlay;
	private boolean isIPValid;
	private boolean isPortValid;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CarGameUI window = new CarGameUI();
					window.frmCarGame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws IOException
	 * @throws HeadlessException
	 */
	public CarGameUI() throws HeadlessException, IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws IOException
	 * @throws HeadlessException
	 */
	private void initialize() throws HeadlessException, IOException {
		frmCarGame = new JFrame();
		frmCarGame.getContentPane().setBackground(Color.BLUE);

		frmCarGame.setTitle("Car Game");
		frmCarGame.setResizable(false);
		frmCarGame.setBounds(100, 100, 224, 148);
		frmCarGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCarGame.getContentPane().setLayout(null);
		
		//Initializing IP input with Verifier
		ipTextField = new JTextField();
		ipTextField.setBounds(101, 22, 107, 20);
		ipTextField.setInputVerifier(new InputVerifier() {
			Pattern pat = Pattern
					.compile("\\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."
							+ "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."
							+ "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."
							+ "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b");

			public boolean shouldYieldFocus(JComponent input) {
				boolean inputOK = verify(input);
				if (inputOK) {
					isIPValid = true;
					return true;
				} else {
					((JTextField) input).setText("");
					isIPValid = false;
					ShootError.shoot("Invalid IP","Please Enter a valid IP address");
					return false;
				}
			}

			public boolean verify(JComponent input) {
				JTextField field = (JTextField) input;
				Matcher m = pat.matcher(field.getText());
				return m.matches();
			}
		});
		frmCarGame.getContentPane().add(ipTextField);
		ipTextField.setColumns(10);

		JLabel lblTrackerIp = new JLabel("Tracker IP:");
		lblTrackerIp.setBounds(10, 25, 62, 14);
		frmCarGame.getContentPane().add(lblTrackerIp);
		
		//initializing Port input with verifier
		portTextField = new JTextField();
		portTextField.setBounds(138, 53, 70, 20);
		frmCarGame.getContentPane().add(portTextField);
		portTextField.setInputVerifier(new InputVerifier() {
			Pattern pat = Pattern.compile("\\b([0-9]+)");

			public boolean shouldYieldFocus(JComponent input) {
				boolean inputOK = verify(input);
				if (inputOK) {
					isPortValid = true;
					return true;
				} else {
					isPortValid = false;
					((JTextField) input).setText("");
					ShootError.shoot("Invalid Port",
							"Please Enter a valid port number: 0 - 65535");
					return false;
				}
			}

			public boolean verify(JComponent input) {
				JTextField field = (JTextField) input;
				if(field.getText().length()>7)
					return false;
				Matcher m = pat.matcher(field.getText());
				if (m.matches()) {
					int portNumber = Integer.parseInt(field.getText());
					if (portNumber >= 0 && portNumber <= 65535)
						return true;
				}
				return false;
			}
		});
		portTextField.setColumns(6);

		JLabel lblTrackerPort = new JLabel(" Tracker Port: ");
		lblTrackerPort.setBounds(10, 56, 86, 14);
		frmCarGame.getContentPane().add(lblTrackerPort);

		btnConnectPlay = new JButton("Connect & Play");
		btnConnectPlay.setAction(startProcess);
		btnConnectPlay.setBounds(37, 84, 158, 24);
		frmCarGame.getContentPane().add(btnConnectPlay);
	}

	public void toggleButton() {
		if (btnConnectPlay.isEnabled())
			btnConnectPlay.setEnabled(false);
		else
			btnConnectPlay.setEnabled(true);
	}

	private class SwingAction extends AbstractAction {
		CarGameUI carGameUI;

		public SwingAction(CarGameUI carGameUI) {
			putValue(NAME, "Connect & Play");
			putValue(SHORT_DESCRIPTION, "Click to Start Playing");
			this.carGameUI = carGameUI;
		}

		public void actionPerformed(ActionEvent e) {
			
			if(isIPValid && isPortValid)
			if (startGame == null || !startGame.isAlive()) {
				startGame = new StartGame(carGameUI, ipTextField.getText(),
						Integer.parseInt(portTextField.getText()));
				toggleButton(); //Making start button false
				startGame.start();
			}
		}
	}

}
