import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Tracker extends Thread {
	private static List<WaitingPeer> serverPeers = new ArrayList<WaitingPeer>();
	private static int port;
	private static DatagramSocket socket;
	private static byte[] buf = new byte[1000];
	private static DatagramPacket packet = new DatagramPacket(buf, buf.length);
	
	public Tracker(int port){
		this.port = port;
	}
	
	private static boolean isInTheList(WaitingPeer waitingP){
		String address = waitingP.address;
		for(WaitingPeer wpe : serverPeers){
			if(wpe.address.equalsIgnoreCase(address))
				return true;
		}
		return false;
	}
	
	private static void sendBack(boolean isServer, WaitingPeer wp) throws IOException{
		String dString = isServer+"!"+wp.address;
        buf = dString.getBytes();
        
		packet = new DatagramPacket(buf, buf.length, packet.getAddress(), packet.getPort());
        socket.send(packet);
        String receivedMSG = new String(packet.getData(), 0,
				packet.getLength());
        System.out.println(receivedMSG);
	}
	
	public void run() {
		System.out.println("Start listenning on port "+port+"...");
		System.err.println("This is an error");
		try {
			socket = new DatagramSocket(port);
		

		while(true) {
			socket.receive(packet);
			String receivedMSG = new String(packet.getData(), 0,
					packet.getLength());
		
			WaitingPeer wp = new WaitingPeer();
			wp.address = packet.getAddress().getHostAddress();
			System.out.println("Recieved from "+wp.address+" The message is: " + receivedMSG );
			
			if(serverPeers.size() != 0){
				if(isInTheList(wp))
					sendBack(true, wp);
				else
					sendBack(false, serverPeers.remove(0));
			}else if(serverPeers.size()==0){
				serverPeers.add(wp);
				sendBack(true, wp);
			}
		}
		} catch (SocketException e) {
			System.err.println("ERROR: Exception happened while trying to listen on the port "+port+". Probably another program is listening on the same port or you don't have enough permissions");
		} catch (IOException e) {
			System.err.println("ERROR: Exception happened trying to send and recieve data, check your connection please.");
		}

	}

}
