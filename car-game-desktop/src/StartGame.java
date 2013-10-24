import java.io.IOException;

import cargame.desktop.Main;

public class StartGame extends Thread{
	private String IP;
	private int port;
	private StartGameOutput window;
	private CarGameUI carGameUI;
	TrackerClient trackerClient;
	public StartGame(CarGameUI carGameUI,String ip, int port){
		this.IP = ip;
		this.port = port;
		this.carGameUI = carGameUI;
	}
	
	public void windowClosed(){
		if(trackerClient.isAlive())
			trackerClient.stopClient = true;
	}
	
	public void run(){
		
		try{
			window = new StartGameOutput(this);
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
