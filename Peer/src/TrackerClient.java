import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class TrackerClient extends Thread {

	private int TIME_BETWEEN_TRIES = 1000;

	public TrackerClient() throws IOException {
		super("MulticastServerThread");
	}

	public void run() {
		try {
			queryTheTracker();
		} catch (UnreachableTracker e) {
			// TODO Auto-generated catch block
			System.out.println("Tracker is Unreachable");
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
			serverAddress = InetAddress.getByName("127.0.0.1");
			sendPacket = new DatagramPacket(buf, buf.length, serverAddress,
					4445);
			// -------Preparing the recieve Packet Data-------
			recPacket = new DatagramPacket(buf, buf.length, serverAddress, 4445);
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
				System.out.println("Couldn't reach the tracker, try #"+tries);
				if(tries == 5)
					throw new UnreachableTracker();
			} catch (Exception e){
				System.out.println("exception happened 1");
			}
		}
		
	}
}