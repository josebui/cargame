package cargame.desktop.listener;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import cargame.desktop.Main;
import cargame.desktop.gui.CarGameUI;
import cargame.gamelogic.listeners.GameCycleListener;

/**
 * The listener interface for receiving desktopGame events.
 * The class that is interested in processing a desktopGame
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addDesktopGameListener<code> method. When
 * the desktopGame event occurs, that object's appropriate
 * method is invoked.
 *
 * @see DesktopGameEvent
 */
public class DesktopGameListener implements GameCycleListener {

	/** The car game ui. */
	private CarGameUI carGameUI;
	
	/**
	 * Instantiates a new desktop game listener.
	 *
	 * @param carGameUI the car game ui
	 */
	public DesktopGameListener(CarGameUI carGameUI) {
		super();
		this.carGameUI = carGameUI;
	}

	/* (non-Javadoc)
	 * @see cargame.gamelogic.listeners.GameCycleListener#endGame()
	 */
	@Override
	public void endGame() {
		if(carGameUI == null){
			Main.restartGame();
		}else{
			carGameUI.bringToFront();
			carGameUI.toggleButton();
			System.out.println("Ended");
		}
	}

	/* (non-Javadoc)
	 * @see cargame.gamelogic.listeners.GameCycleListener#startGame()
	 */
	@Override
	public void startGame() {
		try {
			// Gain focus
			Display.setDisplayMode(Display.getDisplayMode());
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
}
