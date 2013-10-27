package cargame.sync;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import cargame.CarGame;
import cargame.communication.Client;
import cargame.communication.GameInfo;
import cargame.communication.Player;
import cargame.communication.messaging.UdpMessage;
import cargame.communication.utils.UdpMessageUtils;

public class GameSync extends Thread implements Client {

	private static final int MESSAGE_LENGTH = 700;
	private static final int PERMITED_MESSAGE_LOST = 150 ;
	private static final int WAIT_TIME = 5000 ;
	
	private static final int STATUS_RUNNING = 0;
	private static final int STATUS_WAITING = 1;
	private static final int STATUS_STOP = 2;
	
	private InetAddress peerAddress;
	private int serverPort;
	private int clientPort;
	
	private int status;
	private boolean server;
	
	private long lastReceivedPlayerTime;
	private long waitingTime;
	
	public GameSync() {
		super();
		this.serverPort = 12343;
		this.clientPort = 12353;
		this.status = STATUS_WAITING;
	}
	
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
	
	public void stopSync(){
		this.status = STATUS_STOP;
	}
	
	private InetAddress getInetAddress(String url){
		try {
			return InetAddress.getByName(url);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
	
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

	public void setSetState(int state) {
		this.status = state;
	}
	
	private void sendData() {
		UdpMessage outMessage = new UdpMessage(UdpMessage.TYPE_PLAYER_DATA, CarGame.getInstance().getMyPlayer(), System.currentTimeMillis(),CarGame.getInstance().getGameId());
		outMessage.setAddress(this.peerAddress);
		UdpMessageUtils.sendMessage(outMessage, (server)?this.clientPort:this.serverPort);

	}
	
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
	
	@Override
	public cargame.communication.ServerStatus ServerStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameInfo currentGame() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCurrentGame(int gameInfoID, Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMyPlayerInfo(Player player) {

	}
	
	private boolean checkState(int state){
		return state == this.status;
	}
	
	private void syncPlayerInfo(Player player){
		Map<Integer, Player> playerList = CarGame.getInstance().getPlayers();
		playerList.put(player.id, player);
	}
}
