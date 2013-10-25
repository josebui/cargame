package cargame;

import java.util.HashMap;
import java.util.Map;

import cargame.core.Player;
import cargame.listeners.GameCycleListener;
import cargame.screens.GameScreen;
import cargame.sync.GameSync;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class CarGame extends Game{
	
	private static CarGame instance;
	
	public static CarGame getInstance(){
		return instance;
	}
	
	public static CarGame createInstance(boolean server,String serverIp, int lapsNumber,int carType){
		instance = new CarGame(server,serverIp,lapsNumber,carType);
		return instance;
	}
	
	public static final int GAME_SCREEN = 0;
	public static final int SPLASH_SCREEN = 1;
	public static final int INTRO_SCREEN = 2;
	public static final int GAMEOVER_SCREEN = 3;
	
	public static final int STATUS_WAITING = 0;
	public static final int STATUS_PLAYING = 1;
	public static final int STATUS_ENDED = 2;
	public static final int STATUS_NEW_GAME = 3;
	public static final int STATUS_GAME_OVER = 4;
	public static final int STATUS_CONNECTION_LOST = 5;
	
	private GameCycleListener cycleListener;
	
	private GameScreen gameScreen;
	
	private Map<Integer,Player> players;
	private Player myPlayer;
	
	private GameSync gameSync;
	
	private int status;
	private boolean connectionLost;
	// Game attributes
	private boolean server;
	private String serverIp;
	private int lapsNumber;
	private int carType;
	
	private CarGame(boolean server,String serverIp, int lapsNumber,int carType) {
		super();
		this.server = server;
		this.serverIp = serverIp;
		this.lapsNumber = lapsNumber;
		this.carType = carType;
//		this.status = STATUS_PLAYING;
//		this.waiting = true;
//		this.connectionLost = false;
//		gameSync = new GameSync(this,server,serverIp);
//		Integer playerId = gameSync.getPlayerId();
//		if(playerId == null){
//			return;
//		}
//		myPlayer = new Player();
//		myPlayer.id = (server)?1:0;
//		myPlayer.car_id = carType;
//		
//		gameScreen = new GameScreen(myPlayer,lapsNumber);
	}
	
	@Override
	public void create() {
		newGame(server,serverIp,lapsNumber,carType);
//		this.setScreen(gameScreen);
//		gameSync.start();
	}
	
	public void newGame(boolean server,String serverIp, int lapsNumber,int carType){
		this.server = server;
		this.serverIp = serverIp;
		this.lapsNumber = lapsNumber;
		this.carType = carType;
		this.connectionLost = false;
		this.setStatus(STATUS_NEW_GAME);
	}
	
	public void startNewGame(){
		System.out.println("New game");
		if(this.gameSync == null){
			this.gameSync = new GameSync();
		}
		
		this.setStatus(STATUS_WAITING);
		
		// Players
		myPlayer = new Player();
		myPlayer.id = (server)?1:0;
		myPlayer.car_id = carType;
		players = new HashMap<Integer, Player>();
		
		if( this.gameScreen==null){
			this.gameScreen = new GameScreen();
			this.setScreen(gameScreen);
		}

		this.gameScreen.startGame(myPlayer,lapsNumber);
		
		gameSync.start(server,serverIp);
		this.setStatus(STATUS_WAITING);
	}
	
	public void render(){
		if(this.checkStatus(STATUS_NEW_GAME)){
			startNewGame();
		}
		super.render();
	}
	
	public void switchScreen(int screen){
		Screen nextScreen = null;
		switch(screen){
			case GAME_SCREEN:
				nextScreen = gameScreen;
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
	
	public void setPlayers(Map<Integer, Player> players){
		this.players = players;
	}

	public Player getMyPlayer() {
		return myPlayer;
	}

	public void setMyPlayer(Player myPlayer) {
		this.myPlayer = myPlayer;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isConnectionLost() {
		return connectionLost; 
	}

	public void setConnectionLost(boolean connectionLost) {
		this.connectionLost = connectionLost;
	}
	
	public GameCycleListener getCycleListener() {
		return cycleListener;
	}

	public void setCycleListener(GameCycleListener cycleListener) {
		this.cycleListener = cycleListener;
	}

	public void endGame(){
		this.status = CarGame.STATUS_ENDED;
//		this.gameSync.stopSync();
//		gameScreen = new GameScreen(myPlayer,3);
//		this.setScreen(gameScreen);
		cycleListener.endGame();
//		CarGame.getInstance().newGame(server, serverIp, lapsNumber, carType);
//		newGame(server,serverIp,lapsNumber,carType);
	}

	public boolean checkStatus(int status){
		return this.status == status;
	}

	public void restartGame() {
		CarGame.getInstance().newGame(server, serverIp, lapsNumber, carType);
	}
	
}
