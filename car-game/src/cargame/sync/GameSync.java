package cargame.sync;

import cargame.CarGame;

public class GameSync extends Thread {

	private CarGame game;
	private boolean running;

	public GameSync(CarGame game) {
		super();
		this.game = game;
		this.running = true; 
	}
	
	@Override
	public void run(){
		while(running){
			game.sendMyPlayerInfo(game.getMyPlayer());
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	
	
}
