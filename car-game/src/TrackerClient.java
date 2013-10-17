import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TrackerClient extends Thread {

	private int TIME_BETWEEN_TRIES = 1000;
	private String ip;
	private int port;
	private StartGameOutput guiOutput;
	public TrackerClient(String ip, int port, StartGameOutput guiOutput) throws IOException {
		super("MulticastServerThread");
		this.ip = ip;
		this.port = port;
		this.guiOutput = guiOutput;
	}

	public void run() {
		try {
			queryTheTracker();
		} catch (UnreachableTracker e) {
			// TODO Auto-generated catch block
			guiOutput.writeOutput("Tracker is Unreachable");
			JOptionPane.showMessageDialog(new JFrame(),
        		    "The tracker is unreachable",
        		    "Unreachable Tracker",
        		    JOptionPane.ERROR_MESSAGE);
            Toolkit.getDefaultToolkit().beep();
            try {
				this.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	private void queryTheTracker() throws UnreachableTracker{
		//------------Define and Initialize vars--------------
		DatagramSocket socket = null;
		DatagramPacket sendPacket = null;
		DatagramPacket recPacket = null;
		InetAddress serverAddress = null;
		InputStream inputStream = null;
		try {
			// ------Preparing the Socket----------------------
			byte[] buf = new byte[1000];
			socket = new DatagramSocket();
			socket.setSoTimeout(TIME_BETWEEN_TRIES);
			// -------Preparing the Send Packet----------------
			String dString = "Alive!____________________________________";
			buf = dString.getBytes();
			serverAddress = InetAddress.getByName(this.ip);
			sendPacket = new DatagramPacket(buf, buf.length, serverAddress,
					this.port);
			// -------Preparing the receive Packet Data-------
			recPacket = new DatagramPacket(buf, buf.length, serverAddress, this.port);
		} catch (SocketException e) {
			// Can not open new socket, check the permissions
			// To-Do the Appropriate job for Socket Exception
			e.printStackTrace();
		} catch (java.net.UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch ( Exception e){
			System.out.println("Hi Exception Happened!");
		}
		int tries = 0;
		while (tries < 5) {
			try {
				//It sends the request and wait for TIME_BETWEEN_TRIES ms
				socket.send(sendPacket);
				socket.receive(recPacket);
				
				String receivedMSG = new String(recPacket.getData(), 0,
						recPacket.getLength());
				System.out.println(receivedMSG);
				break;

			} catch (SocketTimeoutException e) {
				tries++;
				guiOutput.writeOutput("Couldn't reach the tracker("+this.ip+":"+this.port+"), try #"+tries);
				if(tries == 5)
					throw new UnreachableTracker();
			} catch (Exception e){
				guiOutput.writeOutput("System error, please contact the developers!");
			}
		}
		
	}
}