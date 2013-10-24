package cargame.desktop.gui;

import java.io.IOException;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

import cargame.desktop.Main;
import cargame.desktop.listener.DesktopApplicationListener;

/*When the ip and port is entered in the main Graphical user interface
 * This Class is called
 * */
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
			System.out.println(IP+":"+port);
			trackerClient = new TrackerClient(IP, port, window);
			trackerClient.start();
			trackerClient.join();
			window.closeWindow();
			if (trackerClient.ifGotAnswerFromTracker()){
				LwjglApplication app = Main.startCarGameDesktop(trackerClient.isServer,
						trackerClient.toConnectIP, 1,
						(trackerClient.isServer ? 1 : 3));
				app.addLifecycleListener(new DesktopApplicationListener(carGameUI));
			}else
				carGameUI.toggleButton();
		} catch (InterruptedException e) {
			ShootError.shoot("Interrupted",
					"Application execution was interrupted!");
		}

	}

}
