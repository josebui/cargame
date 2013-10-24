package cargame.desktop.listener;

import cargame.desktop.gui.CarGameUI;

import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class DesktopApplicationListener implements LifecycleListener {

	private CarGameUI carGameUI;
	
	public DesktopApplicationListener(CarGameUI carGameUI) {
		super();
		this.carGameUI = carGameUI;
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {
		carGameUI.toggleButton();
	}

}
