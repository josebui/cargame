import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import java.awt.Color;
import java.awt.Font;

import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;

import javax.swing.Action;


public class GUI {

	private JFrame frmTrackerCar;
	private JTextField textField;
	private JTextField textField_1;
	private JLabel lblIp;
	private JLabel lblPort;
	private JTextPane textPane;
	private JLabel lblCarGameTracker;
	private final Action startAction = new SwingAction(this);
	private final Action stopAction = new SwingAction_1();
	private JButton btnStart;
	private JButton btnStop;
	private Tracker tracker;
	private JTextArea textArea;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmTrackerCar.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTrackerCar = new JFrame();
		frmTrackerCar.setTitle("Tracker - Car Game");
		frmTrackerCar.setResizable(false);
		frmTrackerCar.setAlwaysOnTop(true);
		frmTrackerCar.getContentPane().setForeground(Color.WHITE);
		frmTrackerCar.getContentPane().setFont(new Font("Tahoma", Font.BOLD, 11));
		frmTrackerCar.getContentPane().setBackground(Color.ORANGE);
		frmTrackerCar.setBackground(new Color(51, 153, 255));
		frmTrackerCar.setBounds(100, 100, 313, 300);
		frmTrackerCar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTrackerCar.getContentPane().setLayout(null);
		
		btnStart = new JButton("START");
		btnStart.setAction(startAction);
		btnStart.setBounds(77, 75, 78, 23);
		frmTrackerCar.getContentPane().add(btnStart);
		
		btnStop = new JButton("STOP");
		btnStop.setAction(stopAction);
		btnStop.setEnabled(false);
		btnStop.setBounds(179, 75, 78, 23);
		frmTrackerCar.getContentPane().add(btnStop);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(77, 11, 180, 20);
		frmTrackerCar.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setText("4445");
		textField_1.setBounds(77, 42, 180, 23);
		frmTrackerCar.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		lblIp = new JLabel("IP :");
		lblIp.setBounds(29, 14, 46, 14);
		frmTrackerCar.getContentPane().add(lblIp);
		
		lblPort = new JLabel("PORT :");
		lblPort.setBounds(29, 46, 46, 14);
		frmTrackerCar.getContentPane().add(lblPort);
		
		textArea = new JTextArea();
	    JScrollPane scrollPane = new JScrollPane( textArea );
		//textPane = new JTextPane();
	    textArea.setEditable(false);
	    scrollPane.setBounds(10, 104, 287, 120);
	    //Console.redirectOutput( textArea );
		frmTrackerCar.getContentPane().add(scrollPane);
		
		lblCarGameTracker = new JLabel("Car Game Tracker - Team 1");
		lblCarGameTracker.setBounds(77, 235, 180, 14);
		frmTrackerCar.getContentPane().add(lblCarGameTracker);
	}

	private class SwingAction extends AbstractAction {
		GUI guiObject;
		public SwingAction(GUI guiObject) {
			this.guiObject = guiObject;
			putValue(NAME, "Start");
			putValue(SHORT_DESCRIPTION, "Starting the tracker");
		}
		public void actionPerformed(ActionEvent e) {
			toggleButtons();
			System.out.println("Started...");
			tracker = new Tracker(guiObject);
			tracker.setPort(Integer.parseInt(textField_1.getText()));
			tracker.start();
		}
	}
	private class SwingAction_1 extends AbstractAction {

		public SwingAction_1() {
			putValue(NAME, "Stop");
			putValue(SHORT_DESCRIPTION, "Stopping the tracker");
		}
		public void actionPerformed(ActionEvent e) {
			tracker.stopTracker();
			//Console.redirectOutput( textArea );
			System.out.println("Stopped...");
		}
	}
	
	public void toggleButtons(){
		if(btnStart.isEnabled()){
			btnStop.setEnabled(true);
			btnStart.setEnabled(false);
		}else{
			btnStart.setEnabled(true);
			btnStop.setEnabled(false);
		}
	}
	
	public void outputLog(String message){
		textArea.append(message+"\n");
	}
}
