package cargame.desktop.listener;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

import cargame.listeners.GameCycleListener;

public class DesktopGameListener implements GameCycleListener {

	private LwjglApplication app;
	
	public DesktopGameListener(LwjglApplication app) {
		super();
		this.app = app;
	}

	@Override
	public void endGame() {
		app.exit();
	}
	
}
