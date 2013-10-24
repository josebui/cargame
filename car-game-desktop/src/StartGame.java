
import java.io.IOException;

import cargame.desktop.Main;

public class StartGame extends Thread {
	private String IP;
	private int port;
	private GUILogOutput window;
	private CarGameUI carGameUI;
	TrackerClient trackerClient;

	public StartGame(CarGameUI carGameUI, String ip, int port) {
		this.IP = ip;
		this.port = port;
		this.carGameUI = carGameUI;
	}
	
	public void windowClosed() {
		//If window is closed it sends a signal to client thread to stop
		if (trackerClient.isAlive())
			trackerClient.stopClient = true;
	}

	public void run() {

		try {
			window = new GUILogOutput(this);
			trackerClient = new TrackerClient(IP, port, window);
			trackerClient.start();
			trackerClient.join();
			window.closeWindow();
			if (trackerClient.ifGotAnswerFromTracker())
				Main.startCarGameDesktop(trackerClient.isServer,
						trackerClient.toConnectIP, 1,
						(trackerClient.isServer ? 1 : 3));			
			carGameUI.toggleButton();
		} catch (InterruptedException e) {
			ShootError.shoot("Interrupted",
					"Application execution was interrupted!");
		}

	}

}
