import java.awt.EventQueue;
import java.io.IOException;


public class StartGame extends Thread{
	private String IP;
	private int port;
	public StartGame(String ip, int port) throws IOException{
		this.IP = ip;
		this.port = port;
	}
	
	public void run(){
		TrackerClient trackerClient;
		
		try {
			trackerClient = new TrackerClient(IP, port);
			trackerClient.start();
			trackerClient.join();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
