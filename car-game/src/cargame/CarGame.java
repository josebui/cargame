package cargame;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cargame.communication.Player;
import cargame.communication.sync.GameSync;
import cargame.gamelogic.listeners.GameCycleListener;
import cargame.gamelogic.screens.GameScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class CarGame extends Game{
	
	private static CarGame instance;
	
	public static CarGame getInstance(){
		return instance;
	}
	
	public static CarGame createInstance(String gameId,boolean server,String serverIp, int lapsNumber,int carType){
		if(instance != null){
			instance.dispose();
		}
		instance = new CarGame(gameId,server,serverIp,lapsNumber,carType);
		return instance;
	}
	
	public static final int LAPS_NUMBER = 2;
	
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
	
	public static final int ACTION_FORWARD = 0;
	public static final int ACTION_REVERSE = 1;
	public static final int ACTION_LEFT = 2;
	public static final int ACTION_RIGHT = 3;
	
	public static final int ACTION_TAB = 4;
	public static final int ACTION_SPACE = 5;
	
	private Set<Integer> currentActions;
	
	private GameCycleListener cycleListener;
	
	private GameScreen gameScreen;
	
	private Map<Integer,Player> players;
	private Player myPlayer;
	
	private GameSync gameSync;
	
	private int status;
	private boolean connectionLost;
	private boolean waitStop;
	
	// Game attributes
	private boolean server;
	private String serverIp;
	private int lapsNumber;
	private int carType;
	
	private String gameId;
	
	private CarGame(String gameId,boolean server,String serverIp, int lapsNumber,int carType) {
		super();
		this.server = server;
		this.serverIp = serverIp;
		this.lapsNumber = lapsNumber;
		this.carType = carType;
		this.gameId = gameId;
	}
	
	@Override
	public void create() {
		newGame(gameId,server,serverIp,lapsNumber,carType);
	}
	
	public void newGame(String gameId,boolean server,String serverIp, int lapsNumber,int carType){
		this.server = server;
		this.serverIp = serverIp;
		this.lapsNumber = lapsNumber;
		this.carType = carType;
		this.connectionLost = false;
		this.waitStop = false;
		this.gameId = gameId;
		this.currentActions = new HashSet<Integer>();
		this.setStatus(STATUS_NEW_GAME);
	}
	
	public void startNewGame(){
		System.out.println("New game");
		if(this.gameSync == null){
			this.gameSync = new GameSync();
		}
		
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
		
		if(cycleListener != null)
			cycleListener.startGame();
	}
	
	public void render(){
		if(this.checkStatus(STATUS_NEW_GAME)){
			startNewGame();
		}
		if(this.checkStatus(STATUS_PLAYING)){
			for(int action : currentActions){
				this.executeAction(action);
			}
		}
		super.render();
	}
	
	public void addAction(int action){
		this.currentActions.add(action);
		switch(action){
			case ACTION_SPACE:
				if(checkStatus(CarGame.STATUS_PLAYING) && isConnectionLost()){
					endGame();
				}else if(checkStatus(CarGame.STATUS_GAME_OVER)){
					endGame();
				}else if(checkStatus(CarGame.STATUS_WAITING) && isWaitStop()){
					endGame();
				}
			break;
		}
		
	}
	
	public void removeAction(int action){
		this.currentActions.remove(action);
	}
	
	private void executeAction(int action){
		if(this.gameScreen == null) return;
		this.gameScreen.executeAction(action);
	}
	
	public boolean isActionActive(int action){
		return this.currentActions.contains(action);
	}
	
	public void switchScreen(int screen){
		Screen nextScreen = null;
		switch(screen){
			case GAME_SCREEN:
				nextScreen = gameScreen;
			break;
		}
		if(!this.getScreen().equals(nextScreen)){
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
	
	public boolean isWaitStop() {
		return waitStop;
	}

	public void setWaitStop(boolean waitStop) {
		this.waitStop = waitStop;
	}

	public GameCycleListener getCycleListener() {
		return cycleListener;
	}

	public void setGameCycleListener(GameCycleListener cycleListener) {
		this.cycleListener = cycleListener;
	}

	public void endGame(){
		this.setStatus(CarGame.STATUS_ENDED);
//		this.gameSync.stopSync();
//		gameScreen = new GameScreen(myPlayer,3);
//		this.setScreen(gameScreen);
		if(cycleListener != null)
			cycleListener.endGame();
//		CarGame.getInstance().newGame(server, serverIp, lapsNumber, carType);
//		newGame(server,serverIp,lapsNumber,carType);
	}

	@Override
	public void dispose(){
		super.dispose();
		this.gameSync.stopSync();
	}
	
	public boolean checkStatus(int status){
		return this.status == status;
	}

	public void restartGame() {
		CarGame.getInstance().newGame(gameId,server, serverIp, lapsNumber, carType);
	}

	public String getGameId() {
		return this.gameId;
	}
	
}
