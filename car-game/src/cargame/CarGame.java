package cargame;

import cargame.screens.GameOver;
import cargame.screens.GameScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class CarGame extends Game {
	
	private static CarGame instance;
	
	public static CarGame getInstance(){
		if(instance == null){
			instance = new CarGame();
		}
		return instance;
	}
	
	public static final int GAME_SCREEN = 0;
	public static final int SPLASH_SCREEN = 1;
	public static final int INTRO_SCREEN = 2;
	public static final int GAMEOVER_SCREEN = 3;
	
	private Screen splashScreen;
	private Screen gameScreen;
	private Screen introScreen;
	private Screen gameOverScreen;
	
	private CarGame() {
		super();
		gameScreen = new GameScreen();
		gameOverScreen = new GameOver();
	}
	
	@Override
	public void create() {
		this.setScreen(gameScreen);
	}
	
	public void switchScreen(int screen){
		Screen nextScreen = null;
		switch(screen){
			case GAME_SCREEN:
				nextScreen = gameScreen;
			break;
			case GAMEOVER_SCREEN:
				nextScreen = gameOverScreen;
			break;
		}
		if(!this.getScreen().equals(nextScreen)){
			System.out.println("Next screen");
			this.setScreen(nextScreen);
		}
	}
	
}
