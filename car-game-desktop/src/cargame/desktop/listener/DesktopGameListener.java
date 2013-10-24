package cargame.desktop.listener;

import com.badlogic.gdx.Gdx;

import cargame.listeners.GameCycleListener;

public class DesktopGameListener implements GameCycleListener {

	public DesktopGameListener() {
		super();
	}

	@Override
	public void endGame() {
		Gdx.app.exit();
	}
	
}
