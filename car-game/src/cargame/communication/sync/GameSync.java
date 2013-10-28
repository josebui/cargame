package cargame.communication.sync;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import cargame.CarGame;
import cargame.communication.Client;
import cargame.communication.GameInfo;
import cargame.communication.Player;
import cargame.communication.messaging.UdpMessage;
import cargame.communication.utils.UdpMessageUtils;

/**
 * The Class GameSync.
 */
public class GameSync extends Thread implements Client {

	/** The Constant MESSAGE_LENGTH. */
	private static final int MESSAGE_LENGTH = 700;
	
	/** The Constant PERMITED_MESSAGE_LOST. */
	private static final int PERMITED_MESSAGE_LOST = 150 ;
	
	/** The Constant WAIT_TIME. */
	private static final int WAIT_TIME = 5000 ;
	
	/** The Constant STATUS_RUNNING. */
	private static final int STATUS_RUNNING = 0;
	
	/** The Constant STATUS_WAITING. */
	private static final int STATUS_WAITING = 1;
	
	/** The Constant STATUS_STOP. */
	private static final int STATUS_STOP = 2;
	
	/** The peer address. */
	private InetAddress peerAddress;
	
	/** The server port. */
	private int serverPort;
	
	/** The client port. */
	private int clientPort;
	
	/** The status. */
	private int status;
	
	/** The server. */
	private boolean server;
	
	/** The last received player time. */
	private long lastReceivedPlayerTime;
	
	/** The waiting time. */
	private long waitingTime;
	
	/**
	 * Instantiates a new game sync.
	 */
	public GameSync() {
		super("Game Sync");
		this.serverPort = 12343;
		this.clientPort = 12353;
		this.status = STATUS_WAITING;
	}
	
	/**
	 * Start.
	 *
	 * @param server the server
	 * @param serverIp the server ip
	 */
	public void start(boolean server,String serverIp){
		this.status = STATUS_RUNNING; 
		this.server = server;
		this.peerAddress = (server)?null:getInetAddress(serverIp);
		System.out.println("Received address:"+this.peerAddress);
		this.lastReceivedPlayerTime = Long.MIN_VALUE;
		this.waitingTime = System.currentTimeMillis();
		if(!this.isAlive()){
			super.start();
		}
	}
	
	/**
	 * Stop sync.
	 */
	public void stopSync(){
		this.status = STATUS_STOP;
	}
	
	/**
	 * Gets the inet address.
	 *
	 * @param url the url
	 * @return the inet address
	 */
	private InetAddress getInetAddress(String url){
		try {
			return InetAddress.getByName(url);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run(){
		int lost_packets = 0;
		while(checkState(STATUS_RUNNING) || checkState(STATUS_WAITING)){
			if(checkState(STATUS_RUNNING) && !CarGame.getInstance().checkStatus(CarGame.STATUS_ENDED)){
				if(receiveData()){
					
					lost_packets = 0;
					CarGame.getInstance().setConnectionLost(false);
				}else{
					lost_packets++;
				}
				if(CarGame.getInstance().checkStatus(CarGame.STATUS_PLAYING) && lost_packets >= PERMITED_MESSAGE_LOST){
					CarGame.getInstance().setConnectionLost(true);
				}
				if(this.peerAddress != null){
					sendData();
				}
			}
			
			if(CarGame.getInstance().checkStatus(CarGame.STATUS_WAITING) && (System.currentTimeMillis() -this.waitingTime >= WAIT_TIME )){
				CarGame.getInstance().setWaitStop(true);
			}
		}
	}

	/**
	 * Sets the sets the state.
	 *
	 * @param state the new sets the state
	 */
	public void setSetState(int state) {
		this.status = state;
	}
	
	/**
	 * Send data.
	 */
	private void sendData() {
		UdpMessage outMessage = new UdpMessage(UdpMessage.TYPE_PLAYER_DATA, CarGame.getInstance().getMyPlayer(), System.currentTimeMillis(),CarGame.getInstance().getGameId());
		outMessage.setAddress(this.peerAddress);
		UdpMessageUtils.sendMessage(outMessage, (server)?this.clientPort:this.serverPort);

	}
	
	/**
	 * Receive data.
	 *
	 * @return true, if successful
	 */
	private boolean receiveData(){
		UdpMessage inMessage = UdpMessageUtils.receiveMessage((server)?this.serverPort:this.clientPort, MESSAGE_LENGTH, 20);
		
		if(inMessage == null) return false; // No new message
		if(!CarGame.getInstance().getGameId().equals(inMessage.getGameId())) return false; // Message from other game
		if(inMessage.getTime() <= this.lastReceivedPlayerTime) return true; // Ignore old packet
		this.lastReceivedPlayerTime = inMessage.getTime(); 
		
		if(this.server && this.peerAddress == null){
			this.peerAddress = inMessage.getAddress();
			System.out.println("Client address:"+this.peerAddress.getHostAddress());
		}
		
		switch(inMessage.getType()){
			case UdpMessage.TYPE_PLAYER_DATA:
				Player receivedPlayer = (Player)inMessage.getData();
				if(receivedPlayer != null){
					syncPlayerInfo(receivedPlayer);
				}
			break;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see cargame.communication.Client#ServerStatus()
	 */
	@Override
	public cargame.communication.ServerStatus ServerStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see cargame.communication.Client#currentGame()
	 */
	@Override
	public GameInfo currentGame() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see cargame.communication.Client#setCurrentGame(int, cargame.communication.Player)
	 */
	@Override
	public void setCurrentGame(int gameInfoID, Player player) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see cargame.communication.Client#sendMyPlayerInfo(cargame.communication.Player)
	 */
	@Override
	public void sendMyPlayerInfo(Player player) {

	}
	
	/**
	 * Check state.
	 *
	 * @param state the state
	 * @return true, if successful
	 */
	private boolean checkState(int state){
		return state == this.status;
	}
	
	/**
	 * Sync player info.
	 *
	 * @param player the player
	 */
	private void syncPlayerInfo(Player player){
		Map<Integer, Player> playerList = CarGame.getInstance().getPlayers();
		playerList.put(player.id, player);
	}
}
