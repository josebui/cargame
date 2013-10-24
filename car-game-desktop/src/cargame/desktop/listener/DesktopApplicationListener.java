package cargame.desktop.listener;

import cargame.desktop.gui.CarGameUI;

import com.badlogic.gdx.LifecycleListener;

public class DesktopApplicationListener implements LifecycleListener {

	private CarGameUI carGameUI;
	
	public DesktopApplicationListener(CarGameUI carGameUI) {
		super();
		this.carGameUI = carGameUI;
	}

	@Override
	public void pause() {
		System.out.println("Pause");
	}

	@Override
	public void resume() {
		System.out.println("Resume");

	}

	@Override
	public void dispose() {
		System.out.println("Dispose");
		carGameUI.toggleButton();
	}

}
