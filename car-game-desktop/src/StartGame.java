import java.io.IOException;

import cargame.Main;

public class StartGame extends Thread{
	private String IP;
	private int port;
	private StartGameOutput window;
	CarGameUI carGameUI;
	public StartGame(CarGameUI carGameUI,String ip, int port) throws IOException{
		this.IP = ip;
		this.port = port;
		this.carGameUI = carGameUI;
	}
	
	public void run(){
		TrackerClient trackerClient;
		try{
			window = new StartGameOutput();
			trackerClient = new TrackerClient(IP, port, window);
			trackerClient.start();
			trackerClient.join();
			if(trackerClient.getStatus())
				Main.startCarGameDesktop(trackerClient.isServer, trackerClient.toConnectIP, 1, 1);
			window.closeWindow();
			carGameUI.toggleButton();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
