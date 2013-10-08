package cargame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cargame.core.Player;

public class Server {

	private Map<Integer,Player> players;
	
	private ServerSocket serverSocket;

	public Server() {
		super();
		try {
			serverSocket = new ServerSocket(1235);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Map<Integer,Player> getPlayers(){
		if(players == null){
			players = new HashMap<Integer, Player>();
		}
		return players;
	}
	
	public void start(){
		try {
			System.out.println("Server started...");
			Socket socket = serverSocket.accept();
			
			ObjectInputStream objectInputStream =  new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream objectOutputStream;
			
			while(true){
				
				Object response = objectInputStream.readObject();
				if(response.getClass().equals(String.class)){
					objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
					objectOutputStream.writeObject(getUniqueId());
					
				}else{
					Player player = (Player)response;
					this.getPlayers().put(player.id, player);
					
					objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
					objectOutputStream.writeObject(players);
				}
				
				
				
			}
			//objectOutputStream.close();
			//objectInputStream.close();
			//socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Integer getUniqueId(){
		Random rand = new Random(1);
		int nextInt = Math.abs(rand.nextInt());
		while(getPlayers().keySet().contains(nextInt)){
			nextInt = rand.nextInt();
		}
		return nextInt;
	}
	
	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}
	
	
}
