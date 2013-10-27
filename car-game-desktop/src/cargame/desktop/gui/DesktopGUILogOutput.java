package cargame.desktop.gui;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import cargame.communication.tracker.client.GUILogOutput;

//This class opens a small text area, served as log and user info area

public class DesktopGUILogOutput implements GUILogOutput {

	private JFrame frame;
	private JTextArea textArea;
	StartGame startGame;

	/**
	 * Create the application.
	 */
	public DesktopGUILogOutput(final StartGame startGame) {
		this.startGame = startGame;
		initialize();
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    		startGame.windowClosed();
		       }
		});
		frame.setDefaultCloseOperation(0);
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 446, 235);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane( textArea );
		textArea.setEditable(false);
		scrollPane.setBounds(10, 11, 420, 184);
		frame.getContentPane().add(scrollPane);
	}
	
	public void writeOutput(String msg){
		textArea.append(msg+"\n");
	}
	

	public void closeWindow(){
		frame.setVisible(false);
	}
}
