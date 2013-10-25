package cargame.desktop.listener;

import org.lwjgl.opengl.Display;

import cargame.CarGame;
import cargame.desktop.Main;
import cargame.desktop.gui.CarGameUI;
import cargame.listeners.GameCycleListener;

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
		// TODO Auto-generated method stub
		
	}
	
}
