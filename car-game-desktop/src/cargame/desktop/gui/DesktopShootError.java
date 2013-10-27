package cargame.desktop.gui;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cargame.error.ShootErrorImplementation;


public class DesktopShootError implements ShootErrorImplementation {
	
	public static void shoot(final String messageHeader, final String message){
		//It makes a new Thread to not interfering with the current thread of execution 
		new Thread() {
			public void run() {
				try {
					Thread.sleep(80);
				} catch (InterruptedException e) {
					JOptionPane.showMessageDialog(new JFrame(),
			    		    "Application execution interrupted",
			    		    "Interrupted App",
			    		    JOptionPane.ERROR_MESSAGE);
					Toolkit.getDefaultToolkit().beep();
				}
				JOptionPane.showMessageDialog(new JFrame(),
		    		    message,
		    		    messageHeader,
		    		    JOptionPane.ERROR_MESSAGE);
				Toolkit.getDefaultToolkit().beep();
			}
		}.start();
		
	}

	@Override
	public void writeMessage(String messageHeader, String message) {
		DesktopShootError.shoot(messageHeader, message);
	}
}