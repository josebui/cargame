package cargame.sync;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import cargame.CarGame;
import cargame.core.Client;
import cargame.core.GameInfo;
import cargame.core.Player;

public class GameSync extends Thread implements Client {

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
			Socket socket = new Socket("localhost",1235);
			
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectOutputStream.writeObject("ID");
			
			ObjectInputStream objectInputStream = new ObjectInputStream( socket.getInputStream());
			Integer playerId = (Integer) objectInputStream.readObject();
			
			objectInputStream.close();
			objectOutputStream.close();
			socket.close();
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
			Socket socket = new Socket("localhost",1235);
			
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectOutputStream.writeObject(player);
			
			ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
			game.setPlayers((Map<Integer, Player>) objectInputStream.readObject());
//			
			objectInputStream.close();
			objectOutputStream.close();
			socket.close();
			
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
	
	
	
}
