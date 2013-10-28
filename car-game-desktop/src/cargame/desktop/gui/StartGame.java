package cargame.desktop.gui;

import cargame.CarGame;
import cargame.communication.tracker.client.TrackerClient;
import cargame.desktop.Main;
import cargame.error.ShootError;

/**
 * The Class StartGame.
 * When the ip and port is entered in the main Graphical user interface
 * this Class is called
 */
public class StartGame extends Thread {
	
	/** The ip. */
	private String IP;
	
	/** The port. */
	private int port;
	
	/** The window. */
	private DesktopGUILogOutput window;
	
	/** The car game ui. */
	private CarGameUI carGameUI;
	
	/** The tracker client. */
	TrackerClient trackerClient;

	/**
	 * Instantiates a new start game.
	 *
	 * @param carGameUI the car game ui
	 * @param ip the ip
	 * @param port the port
	 */
	public StartGame(CarGameUI carGameUI, String ip, int port) {
		this.IP = ip;
		this.port = port;
		this.carGameUI = carGameUI;
	}
	
	/**
	 * Window closed.
	 */
	public void windowClosed() {
		//If window is closed it sends a signal to client thread to stop
		if (trackerClient.isAlive())
			trackerClient.stopClient = true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {

		try {
			window = new DesktopGUILogOutput(this);
			System.out.println(IP+":"+port);
			trackerClient = new TrackerClient(IP, port, window);
			trackerClient.start();
			trackerClient.join();
			window.closeWindow();
			if (trackerClient.ifGotAnswerFromTracker()){
				Main.startCarGameDesktop(carGameUI,trackerClient.gameId,trackerClient.isServer,
						trackerClient.toConnectIP, CarGame.LAPS_NUMBER,
						(trackerClient.isServer ? 1 : 3));
			}else
				carGameUI.toggleButton();
		} catch (InterruptedException e) {
			ShootError.shoot("Interrupted",
					"Application execution was interrupted!");
		}

	}

}
