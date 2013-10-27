package cargame.desktop.gui;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

import cargame.communication.tracker.client.TrackerClient;
import cargame.desktop.Main;
import cargame.error.ShootError;

/*When the ip and port is entered in the main Graphical user interface
 * This Class is called
 * */
public class StartGame extends Thread {
	private String IP;
	private int port;
	private DesktopGUILogOutput window;
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
			window = new DesktopGUILogOutput(this);
			System.out.println(IP+":"+port);
			trackerClient = new TrackerClient(IP, port, window);
			trackerClient.start();
			trackerClient.join();
			window.closeWindow();
			if (trackerClient.ifGotAnswerFromTracker()){
				LwjglApplication app = Main.startCarGameDesktop(carGameUI,trackerClient.gameId,trackerClient.isServer,
						trackerClient.toConnectIP, 1,
						(trackerClient.isServer ? 1 : 3));
			}else
				carGameUI.toggleButton();
		} catch (InterruptedException e) {
			ShootError.shoot("Interrupted",
					"Application execution was interrupted!");
		}

	}

}
