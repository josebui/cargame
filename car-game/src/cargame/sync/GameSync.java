package cargame.sync;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import cargame.CarGame;
import cargame.core.Client;
import cargame.core.GameInfo;
import cargame.core.MovingPosition;
import cargame.core.Player;

public class GameSync extends Thread implements Client {

	private static final int MESSAGE_LENGTH = 500;
	
	private String url;
	private int serverPort;
	private int clientPort;
	
	private CarGame game;
	private boolean running;
	private boolean server;

	public GameSync(CarGame game,boolean server) {
		super();
		this.game = game;
		this.server = server;
		this.running = true; 
		this.serverPort = 1234;
		this.clientPort = 1235;
//		this.url = "localhost";
		this.url = "10.9.153.28";
//		this.url = "192.168.0.102";
	}
	
	@Override
	public void run(){
		while(running){
			receiveData();
			sendData();
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	private void sendData() {
		try {
			DatagramSocket serverSocket = new DatagramSocket();
//			byte[] sendData = new byte[MESSAGE_LENGTH];

//			String valueString = Arrays.toString(game.getMyPlayer().movingPosition.getValues());
//			String capitalizedSentence = valueString.substring(1, valueString.length()-1);
			
//			sendData = capitalizedSentence.getBytes();
			InetAddress IPAddress = InetAddress.getByName(this.url);
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(game.getMyPlayer());
			byte[] data = outputStream.toByteArray();
			
			//DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,IPAddress , (server)?this.clientPort:this.serverPort);
			DatagramPacket sendPacket = new DatagramPacket(data, data.length,IPAddress , (server)?this.clientPort:this.serverPort);
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
	
	private void receiveData(){
		DatagramSocket serverSocket = null;
		try {
			serverSocket = new DatagramSocket((server)?this.serverPort:this.clientPort);
			serverSocket.setSoTimeout(20);
			byte[] receiveData = new byte[MESSAGE_LENGTH];

			DatagramPacket receivePacket = new DatagramPacket(receiveData,	receiveData.length);
			serverSocket.receive(receivePacket);
			
			byte[] data = receivePacket.getData();
			
			ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			Player receivedPlayer = (Player)objectInputStream.readObject();
			syncPlayerInfo(receivedPlayer);
//			String sentence = new String(receivePacket.getData());

			
//			String[] stringValues = sentence.split(",");
//			float[] values = new float[6];
//			for(int i=0;i<stringValues.length;i++){
//				values[i] = Float.parseFloat(stringValues[i]);
//			}
//			syncPlayerInfo((server)?0:1, values);
			serverSocket.close();
		}catch(SocketTimeoutException e){
			
		}catch (Exception e) {
			e.printStackTrace();
		} 
		if(serverSocket!=null && !serverSocket.isClosed()){
			serverSocket.close();
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
	
	private void syncPlayerInfo(Player player){
		Map<Integer, Player> playerList = game.getPlayers();
		playerList.put(player.id, player);
	}

	@Override
	public void startGame() {
		// TODO Auto-generated method stub
		
	}
	
}
