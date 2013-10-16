

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
	private final Action startProcess = new SwingAction();

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
	 * @throws IOException 
	 * @throws HeadlessException 
	 */
	public CarGameUI() throws HeadlessException, IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 * @throws HeadlessException 
	 */
	private void initialize() throws HeadlessException, IOException {
//		frmCarGame = new JFrame() { 
//			  private Image backgroundImage = ImageIO.read(new File("nascar.jpg"));
//			  public void paint( Graphics g ) { 
//			    super.paint(g);
//			    g.drawImage(backgroundImage, 0, 0, null);
//			  }
//			};
		frmCarGame = new JFrame();
		frmCarGame.getContentPane().setBackground(Color.BLUE);
		
		frmCarGame.setTitle("Car Game");
		frmCarGame.setResizable(false);
		frmCarGame.setBounds(100, 100, 224, 148);
		frmCarGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCarGame.getContentPane().setLayout(null);
		
		
		ipTextField = new JTextField();
		ipTextField.setBounds(101, 22, 107, 20);
		ipTextField.setInputVerifier(new InputVerifier() {
            Pattern pat = Pattern.compile("\\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."+
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b");
            public boolean shouldYieldFocus(JComponent input) {
                boolean inputOK = verify(input);
                if (inputOK) {
                    return true;
                } else {
                	JOptionPane.showMessageDialog(new JFrame(),
                		    "Please Enter a valid IP address",
                		    "Invalid IP",
                		    JOptionPane.ERROR_MESSAGE);
                    Toolkit.getDefaultToolkit().beep();
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
		
		portTextField = new JTextField();
		portTextField.setBounds(138, 53, 70, 20);
		frmCarGame.getContentPane().add(portTextField);
		portTextField.setInputVerifier(new InputVerifier() {
            Pattern pat = Pattern.compile("\\b([0-9]+)");
            public boolean shouldYieldFocus(JComponent input) {
                boolean inputOK = verify(input);
                if (inputOK) {
                    return true;
                } else {
                	JOptionPane.showMessageDialog(new JFrame(),
                		    "Please Enter a valid port number: 0 - 65535",
                		    "Invalid PORT",
                		    JOptionPane.ERROR_MESSAGE);
                    Toolkit.getDefaultToolkit().beep();
                    return false;
                }
            }
            public boolean verify(JComponent input) {
                JTextField field = (JTextField) input;
                Matcher m = pat.matcher(field.getText());
                if( m.matches()){
                	int portNumber = Integer.parseInt(field.getText());
                	if(portNumber>=0 && portNumber <=65535)
                		return true;
                }
                	return false;
            }
        });
		portTextField.setColumns(10);
		
		JLabel lblTrackerPort = new JLabel(" Tracker Port: ");
		lblTrackerPort.setBounds(10, 56, 86, 14);
		frmCarGame.getContentPane().add(lblTrackerPort);
		
		JButton btnConnectPlay = new JButton("Connect & Play");
		btnConnectPlay.setAction(startProcess);
		btnConnectPlay.setBounds(37, 84, 158, 24);
		frmCarGame.getContentPane().add(btnConnectPlay);
	}
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "Connect & Play");
			putValue(SHORT_DESCRIPTION, "Click to Start Playing");
			
		}
		public void actionPerformed(ActionEvent e) {			
			try {
				StartGame startGame = new StartGame(ipTextField.getText(), Integer.parseInt(portTextField.getText()));
				startGame.start();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
