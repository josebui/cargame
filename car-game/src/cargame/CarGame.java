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

/**
 * The Class CarGame.
 */
public class CarGame extends Game{
	
	/** The instance. */
	private static CarGame instance;
	
	/**
	 * Gets the single instance of CarGame.
	 *
	 * @return single instance of CarGame
	 */
	public static CarGame getInstance(){
		return instance;
	}
	
	/**
	 * Creates the instance.
	 *
	 * @param gameId the game id
	 * @param server the server
	 * @param serverIp the server ip
	 * @param lapsNumber the laps number
	 * @param carType the car type
	 * @return the car game
	 */
	public static CarGame createInstance(String gameId,boolean server,String serverIp, int lapsNumber,int carType){
		if(instance != null){
			instance.dispose();
		}
		instance = new CarGame(gameId,server,serverIp,lapsNumber,carType);
		return instance;
	}
	
	/** The Constant LAPS_NUMBER. */
	public static final int LAPS_NUMBER = 2;
	
	/** The Constant GAME_SCREEN. */
	public static final int GAME_SCREEN = 0;
	
	/** The Constant SPLASH_SCREEN. */
	public static final int SPLASH_SCREEN = 1;
	
	/** The Constant INTRO_SCREEN. */
	public static final int INTRO_SCREEN = 2;
	
	/** The Constant GAMEOVER_SCREEN. */
	public static final int GAMEOVER_SCREEN = 3;
	
	/** The Constant STATUS_WAITING. */
	public static final int STATUS_WAITING = 0;
	
	/** The Constant STATUS_PLAYING. */
	public static final int STATUS_PLAYING = 1;
	
	/** The Constant STATUS_ENDED. */
	public static final int STATUS_ENDED = 2;
	
	/** The Constant STATUS_NEW_GAME. */
	public static final int STATUS_NEW_GAME = 3;
	
	/** The Constant STATUS_GAME_OVER. */
	public static final int STATUS_GAME_OVER = 4;
	
	/** The Constant STATUS_CONNECTION_LOST. */
	public static final int STATUS_CONNECTION_LOST = 5;
	
	/** The Constant ACTION_FORWARD. */
	public static final int ACTION_FORWARD = 0;
	
	/** The Constant ACTION_REVERSE. */
	public static final int ACTION_REVERSE = 1;
	
	/** The Constant ACTION_LEFT. */
	public static final int ACTION_LEFT = 2;
	
	/** The Constant ACTION_RIGHT. */
	public static final int ACTION_RIGHT = 3;
	
	/** The Constant ACTION_TAB. */
	public static final int ACTION_TAB = 4;
	
	/** The Constant ACTION_SPACE. */
	public static final int ACTION_SPACE = 5;
	
	/** The current actions. */
	private Set<Integer> currentActions;
	
	/** The cycle listener. */
	private GameCycleListener cycleListener;
	
	/** The game screen. */
	private GameScreen gameScreen;
	
	/** The players. */
	private Map<Integer,Player> players;
	
	/** The my player. */
	private Player myPlayer;
	
	/** The game sync. */
	private GameSync gameSync;
	
	/** The status. */
	private int status;
	
	/** The connection lost. */
	private boolean connectionLost;
	
	/** The wait stop. */
	private boolean waitStop;
	
	// Game attributes
	/** The server. */
	private boolean server;
	
	/** The server ip. */
	private String serverIp;
	
	/** The laps number. */
	private int lapsNumber;
	
	/** The car type. */
	private int carType;
	
	/** The game id. */
	private String gameId;
	
	/**
	 * Instantiates a new car game.
	 *
	 * @param gameId the game id
	 * @param server the server
	 * @param serverIp the server ip
	 * @param lapsNumber the laps number
	 * @param carType the car type
	 */
	private CarGame(String gameId,boolean server,String serverIp, int lapsNumber,int carType) {
		super();
		this.server = server;
		this.serverIp = serverIp;
		this.lapsNumber = lapsNumber;
		this.carType = carType;
		this.gameId = gameId;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	@Override
	public void create() {
		newGame(gameId,server,serverIp,lapsNumber,carType);
	}
	
	/**
	 * New game.
	 *
	 * @param gameId the game id
	 * @param server the server
	 * @param serverIp the server ip
	 * @param lapsNumber the laps number
	 * @param carType the car type
	 */
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
	
	/**
	 * Start new game.
	 */
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
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Game#render()
	 */
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
	
	/**
	 * Adds the action.
	 *
	 * @param action the action
	 */
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
	
	/**
	 * Removes the action.
	 *
	 * @param action the action
	 */
	public void removeAction(int action){
		this.currentActions.remove(action);
	}
	
	/**
	 * Execute action.
	 *
	 * @param action the action
	 */
	private void executeAction(int action){
		if(this.gameScreen == null) return;
		this.gameScreen.executeAction(action);
	}
	
	/**
	 * Checks if is action active.
	 *
	 * @param action the action
	 * @return true, if is action active
	 */
	public boolean isActionActive(int action){
		return this.currentActions.contains(action);
	}
	
	/**
	 * Switch screen.
	 *
	 * @param screen the screen
	 */
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

	/**
	 * Gets the players.
	 *
	 * @return the players
	 */
	public Map<Integer, Player> getPlayers() {
		if(players == null){
			players = new HashMap<Integer, Player>();
		}
		return players;
	}
	
	/**
	 * Sets the players.
	 *
	 * @param players the players
	 */
	public void setPlayers(Map<Integer, Player> players){
		this.players = players;
	}

	/**
	 * Gets the my player.
	 *
	 * @return the my player
	 */
	public Player getMyPlayer() {
		return myPlayer;
	}

	/**
	 * Sets the my player.
	 *
	 * @param myPlayer the new my player
	 */
	public void setMyPlayer(Player myPlayer) {
		this.myPlayer = myPlayer;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Checks if is connection lost.
	 *
	 * @return true, if is connection lost
	 */
	public boolean isConnectionLost() {
		return connectionLost; 
	}

	/**
	 * Sets the connection lost.
	 *
	 * @param connectionLost the new connection lost
	 */
	public void setConnectionLost(boolean connectionLost) {
		this.connectionLost = connectionLost;
	}
	
	/**
	 * Checks if is wait stop.
	 *
	 * @return true, if is wait stop
	 */
	public boolean isWaitStop() {
		return waitStop;
	}

	/**
	 * Sets the wait stop.
	 *
	 * @param waitStop the new wait stop
	 */
	public void setWaitStop(boolean waitStop) {
		this.waitStop = waitStop;
	}

	/**
	 * Gets the cycle listener.
	 *
	 * @return the cycle listener
	 */
	public GameCycleListener getCycleListener() {
		return cycleListener;
	}

	/**
	 * Sets the game cycle listener.
	 *
	 * @param cycleListener the new game cycle listener
	 */
	public void setGameCycleListener(GameCycleListener cycleListener) {
		this.cycleListener = cycleListener;
	}

	/**
	 * End game.
	 */
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

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Game#dispose()
	 */
	@Override
	public void dispose(){
		super.dispose();
		this.gameSync.stopSync();
	}
	
	/**
	 * Check status.
	 *
	 * @param status the status
	 * @return true, if successful
	 */
	public boolean checkStatus(int status){
		return this.status == status;
	}

	/**
	 * Restart game.
	 */
	public void restartGame() {
		CarGame.getInstance().newGame(gameId,server, serverIp, lapsNumber, carType);
	}

	/**
	 * Gets the game id.
	 *
	 * @return the game id
	 */
	public String getGameId() {
		return this.gameId;
	}
	
}
