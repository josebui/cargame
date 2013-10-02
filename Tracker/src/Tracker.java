import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class Tracker {
	private static List<WaitingPeer> serverPeers = new ArrayList<WaitingPeer>();
	private static DatagramSocket socket;
	private static byte[] buf = new byte[1000];
	private static DatagramPacket packet = new DatagramPacket(buf, buf.length);
	
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
	
	
	public static void main(String[] args) throws IOException {
		System.out.println("Starting client TrackerReciever...");
		socket = new DatagramSocket(4445);

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

	}

}
