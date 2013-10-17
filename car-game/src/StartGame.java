import java.awt.EventQueue;
import java.io.IOException;


public class StartGame extends Thread{
	private String IP;
	private int port;
	private StartGameOutput window;
	public StartGame(String ip, int port) throws IOException{
		this.IP = ip;
		this.port = port;
	}
	
	public void run(){
		TrackerClient trackerClient;
		try{
			window = new StartGameOutput();
			trackerClient = new TrackerClient(IP, port, window);
			trackerClient.start();
			trackerClient.join();
			window.closeWindow();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
