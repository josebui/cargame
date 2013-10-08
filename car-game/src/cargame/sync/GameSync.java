package cargame.sync;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import cargame.CarGame;
import cargame.core.Client;
import cargame.core.GameInfo;
import cargame.core.Player;

public class GameSync extends Thread implements Client {

	private CarGame game;
	private boolean running;
	private boolean server;

	public GameSync(CarGame game,boolean server) {
		super();
		this.game = game;
		this.server = server;
		this.running = true; 
	}
	
	@Override
	public void run(){
		while(running){
			//sendMyPlayerInfo(game.getMyPlayer());
			if(server){
				runAsServer();
			}else{
				runAsClient();
			}
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	@SuppressWarnings("resource")
	public void runAsServer(){
		
		try {
			ServerSocket serverSocket = new ServerSocket(1236);
			System.out.println("Server started...");
			Socket socket = serverSocket.accept();
			interact(socket,true);
			
			//objectOutputStream.close();
//			objectInputStream.close();
//			socket.close();
//			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void interact(Socket socket, boolean server) throws IOException, ClassNotFoundException{
		
		ObjectInputStream objectInputStream;
		ObjectOutputStream objectOutputStream;
		if(server){
			objectInputStream =  new ObjectInputStream(socket.getInputStream());
			objectOutputStream= new ObjectOutputStream(socket.getOutputStream());
		}else{
			objectOutputStream= new ObjectOutputStream(socket.getOutputStream());
			objectInputStream =  new ObjectInputStream(socket.getInputStream());
//			objectOutputStream.writeObject(game.getMyPlayer());
		}
		
		
		Player request;
//		Player response = new Player();
		while(true){
			if(server){
				request = (Player)objectInputStream.readObject();
				System.out.println("Receive:"+request.movingPosition.xPos);
				
				Map<Integer,Player> otherPlayer = new HashMap<Integer, Player>();
				otherPlayer.put(request.id, request);
				game.setPlayers(otherPlayer);
				objectOutputStream.writeObject(game.getMyPlayer());
				System.out.println("Send:"+game.getMyPlayer().movingPosition.xPos);
				
			}else{
				objectOutputStream.writeObject(game.getMyPlayer());
				System.out.println("Send:"+game.getMyPlayer().movingPosition.xPos);
				
				request = (Player)objectInputStream.readObject();
				System.out.println("Receive:"+request.movingPosition.xPos);
				Map<Integer,Player> otherPlayer = new HashMap<Integer, Player>();
				otherPlayer.put(request.id, request);
				game.setPlayers(otherPlayer);
				
			}
			
		}
	}
	
	public void runAsClient(){
		
		try {
			Socket socket = new Socket("localhost",1236);
			System.out.println("Client started...");
			
			interact(socket,false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
//	public Integer getPlayerId(){
//		try {
//			Socket socket = new Socket("localhost",1235);
//			
//			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//			objectOutputStream.writeObject("ID");
//			
//			ObjectInputStream objectInputStream = new ObjectInputStream( socket.getInputStream());
//			Integer playerId = (Integer) objectInputStream.readObject();
//			
//			objectInputStream.close();
//			objectOutputStream.close();
//			socket.close();
//			return playerId;
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}

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
