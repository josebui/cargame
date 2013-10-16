

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


public class CarGameUI {

	private JFrame frmCarGame;
	private JTextField textField;
	private JTextField textField_1;

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
		
		
		textField = new JTextField();
		textField.setBounds(101, 22, 107, 20);
		textField.setInputVerifier(new InputVerifier() {
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
		frmCarGame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblTrackerIp = new JLabel("Tracker IP:");
		lblTrackerIp.setBounds(10, 25, 62, 14);
		frmCarGame.getContentPane().add(lblTrackerIp);
		
		textField_1 = new JTextField();
		textField_1.setBounds(138, 53, 70, 20);
		frmCarGame.getContentPane().add(textField_1);
		textField_1.setInputVerifier(new InputVerifier() {
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
		textField_1.setColumns(10);
		
		JLabel lblTrackerPort = new JLabel(" Tracker Port: ");
		lblTrackerPort.setBounds(10, 56, 86, 14);
		frmCarGame.getContentPane().add(lblTrackerPort);
		
		JButton btnConnectPlay = new JButton("Connect & Play");
		btnConnectPlay.setBounds(37, 84, 158, 24);
		frmCarGame.getContentPane().add(btnConnectPlay);
	}
}
