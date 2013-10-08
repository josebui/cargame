package cargame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import cargame.core.Client;
import cargame.core.GameInfo;
import cargame.core.Player;
import cargame.screens.GameOver;
import cargame.screens.GameScreen;
import cargame.sync.GameSync;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class CarGame extends Game{
	
	private static CarGame instance;
	
	public static CarGame getInstance(){
		return instance;
	}
	
	public static CarGame createInstance(boolean server){
		instance = new CarGame(server);
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
	
	private Map<Integer,Player> players;
	private Player myPlayer;
	
	private GameSync gameSync;
	
	private CarGame(boolean server) {
		super();
		
		gameSync = new GameSync(this,server);
//		Integer playerId = gameSync.getPlayerId();
//		if(playerId == null){
//			return;
//		}
		myPlayer = new Player();
		myPlayer.id = (server)?1:0;
		
		gameScreen = new GameScreen(myPlayer);
		gameOverScreen = new GameOver();
		
	}
	
	@Override
	public void create() {
		this.setScreen(gameScreen);
		gameSync.start();
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

	public Map<Integer, Player> getPlayers() {
		if(players == null){
			players = new HashMap<Integer, Player>();
		}
		return players;
	}

	public void setPlayers(Map<Integer, Player> players) {
		this.players = players;
	}

	public Player getMyPlayer() {
		return myPlayer;
	}

	public void setMyPlayer(Player myPlayer) {
		this.myPlayer = myPlayer;
	}
	
	
}
