package cargame.desktop.gui;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class ShootError {
	public static void shoot(String messageHeader, String message){
		JOptionPane.showMessageDialog(new JFrame(),
    		    message,
    		    messageHeader,
    		    JOptionPane.ERROR_MESSAGE);
		Toolkit.getDefaultToolkit().beep();
	}
}
