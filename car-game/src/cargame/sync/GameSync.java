package cargame.sync;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import cargame.CarGame;
import cargame.core.Client;
import cargame.core.GameInfo;
import cargame.core.MovingPosition;
import cargame.core.Player;

public class GameSync extends Thread implements Client {

	private String url;
	private int port;
	
	private CarGame game;
	private boolean running;
	private boolean server;

	public GameSync(CarGame game,boolean server) {
		super();
		this.game = game;
		this.server = server;
		this.running = true; 
		this.port = 1234;
		this.url = "localhost";
//		this.url = "10.9.202.13";
	}
	
	@Override
	public void run(){
		while(running){
			//sendMyPlayerInfo(game.getMyPlayer());
			receiveData(server);
			sendData();
//			if(server){
//				//runAsServer();
//				receiveData(server);
//			}else{
//				sendData();
//				
//				//runAsClient();
//			}
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
			ServerSocket serverSocket = new ServerSocket(this.port);
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
	
	public void runAsClient(){
		
		try {
			Socket socket = new Socket(this.url,this.port);
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
	
	private void sendData() {
		try {
			DatagramSocket serverSocket = new DatagramSocket();

//			byte[] receiveData = new byte[1024];
			byte[] sendData = new byte[1024];
//			receiveData = new byte[1024];

//			DatagramPacket receivePacket = new DatagramPacket(receiveData,	receiveData.length);

//			System.out.println("Waiting for datagram packet");

//			serverSocket.receive(receivePacket);

//			String sentence = new String(receivePacket.getData());

//			InetAddress IPAddress = receivePacket.getAddress();

//			int port = receivePacket.getPort();

//			System.out.println("From: " + IPAddress + ":" + port);
//			System.out.println("Message: " + sentence);

			String valueString = Arrays.toString(game.getMyPlayer().movingPosition.getValues());
			String capitalizedSentence = valueString.substring(1, valueString.length()-1);

			sendData = capitalizedSentence.getBytes();
			InetAddress IPAddress = InetAddress.getByName(this.url); 
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,IPAddress , port);

			serverSocket.send(sendPacket);
			serverSocket.close();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void receiveData(boolean server){
		try {
			DatagramSocket serverSocket = new DatagramSocket(this.port);

			byte[] receiveData = new byte[1024];
//			byte[] sendData = new byte[1024];
//			receiveData = new byte[1024];

			DatagramPacket receivePacket = new DatagramPacket(receiveData,	receiveData.length);

//			System.out.println("Waiting for datagram packet");

			serverSocket.receive(receivePacket);

			String sentence = new String(receivePacket.getData());

			System.out.println(sentence);
			
			String[] stringValues = sentence.split(",");
			float[] values = new float[6];
			for(int i=0;i<stringValues.length;i++){
				values[i] = Float.parseFloat(stringValues[i]);
			}
			syncPlayerInfo((server)?0:1, values);
//			InetAddress IPAddress = receivePacket.getAddress();

//			int port = receivePacket.getPort();

//			System.out.println("From: " + IPAddress + ":" + port);
//			System.out.println("Message: " + sentence);

//			String capitalizedSentence = Arrays.toString(game.getMyPlayer().movingPosition.getValues());

//			sendData = capitalizedSentence.getBytes();
//			InetAddress IPAddress = InetAddress.getByName(this.url); 
//			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,IPAddress , port);

//			serverSocket.send(sendPacket);
			serverSocket.close();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void interact(Socket socket, boolean server) throws IOException, ClassNotFoundException{
		
		ObjectInputStream objectInputStream;
		ObjectOutputStream objectOutputStream;
		float[] request;
		while(true){
			if(server){
				objectInputStream =  new ObjectInputStream(socket.getInputStream());
				request = (float[])objectInputStream.readObject();
				syncPlayerInfo(0,request);
//				System.out.println("Receive:"+request.movingPosition.xPos);
				
				objectOutputStream= new ObjectOutputStream(socket.getOutputStream());
//				Map<Integer,Player> otherPlayer = new HashMap<Integer, Player>();
//				otherPlayer.put(request.id, request);
//				game.setPlayers(otherPlayer);
				
				objectOutputStream.writeObject(game.getMyPlayer().movingPosition.getValues());
				System.out.println("Send:"+game.getMyPlayer().movingPosition.xPos);
//				objectInputStream.close();
//				objectOutputStream.close();
			}else{
				objectOutputStream= new ObjectOutputStream(socket.getOutputStream());
				objectOutputStream.writeObject(game.getMyPlayer().movingPosition.getValues());
//				System.out.println("Send:"+game.getMyPlayer().movingPosition.xPos);
				
				objectInputStream =  new ObjectInputStream(socket.getInputStream());
				request = (float[])objectInputStream.readObject();
				System.out.println("Receive:"+request);
//				Map<Integer,Player> otherPlayer = new HashMap<Integer, Player>();
//				otherPlayer.put(request.id, request);
				syncPlayerInfo(1,request);
//				game.setPlayers(otherPlayer);
//				objectInputStream.close();
//				objectOutputStream.close();
			}
			
		}
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
	
	private void syncPlayerInfo(int playerId, float[] values){
		Map<Integer, Player> playerList = game.getPlayers();
		if(!playerList.containsKey(playerId)){
			Player newPlayer = new Player();
			newPlayer.id = playerId;
			newPlayer.movingPosition = new MovingPosition();
			playerList.put(playerId, newPlayer);
		}
		Player player = playerList.get(playerId);
		player.time = (new Date()).getTime();
		player.movingPosition.setValues(values);
	}

	@Override
	public void startGame() {
		// TODO Auto-generated method stub
		
	}
	
}
