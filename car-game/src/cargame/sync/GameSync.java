package cargame.sync;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;

import cargame.CarGame;
import cargame.core.Client;
import cargame.core.GameInfo;
import cargame.core.Player;

public class GameSync extends Thread implements Client {

	private String url;
	private int port;
	
	private CarGame game;
	private boolean running;
	
	private Socket socket;

	public GameSync(CarGame game) {
		super();
		this.game = game;
		this.running = true; 
		this.port = 12345;
		this.url = "localhost";
		try {
			this.socket = new Socket(this.url,this.port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run(){
		while(running){
			sendMyPlayerInfo(game.getMyPlayer());
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public Integer getPlayerId(){
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectOutputStream.writeObject("ID");
			
			ObjectInputStream objectInputStream = new ObjectInputStream( socket.getInputStream());
			Integer playerId = (Integer) objectInputStream.readObject();
			
//			objectInputStream.close();
//			objectOutputStream.close();
//			socket.close();
			return playerId;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public cargame.core.ServerStatus ServerStatus() {
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
		try {
			
			player.time = (new Date()).getTime();
			
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectOutputStream.writeObject(player);
			
			ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
			Map<Integer, Player> newPlayerList = (Map<Integer, Player>) objectInputStream.readObject();
			syncPlayersInfo(newPlayerList);
//			
//			objectInputStream.close();
//			objectOutputStream.close();
//			socket.close();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void syncPlayersInfo(Map<Integer, Player> newPlayerList){
		Map<Integer, Player> playerList = game.getPlayers();
		for(Integer playerId : newPlayerList.keySet()){
			Player newPlayerInfo = newPlayerList.get(playerId);
			Player actualPlayerInfo = playerList.put(playerId, newPlayerInfo);
			if(actualPlayerInfo != null){
				if(newPlayerInfo.time <= actualPlayerInfo.time){
					playerList.put(playerId, actualPlayerInfo);
				}
			}
		}
	}
	
}
