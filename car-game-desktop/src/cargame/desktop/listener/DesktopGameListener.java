package cargame.desktop.listener;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import cargame.desktop.Main;
import cargame.desktop.gui.CarGameUI;
import cargame.gamelogic.listeners.GameCycleListener;

public class DesktopGameListener implements GameCycleListener {

	private CarGameUI carGameUI;
	
	public DesktopGameListener(CarGameUI carGameUI) {
		super();
		this.carGameUI = carGameUI;
	}

	@Override
	public void endGame() {
		if(carGameUI == null){
//			System.exit(0);
//			CarGame.getInstance().restartGame();
			Main.restartGame();
		}else{
			carGameUI.bringToFront();
			carGameUI.toggleButton();
			System.out.println("Ended");
		}
	}

	@Override
	public void startGame() {
		try {
			// Gain focus
			Display.setDisplayMode(Display.getDisplayMode());
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
