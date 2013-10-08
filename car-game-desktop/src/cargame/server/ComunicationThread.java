package cargame.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import cargame.core.Player;

public class ComunicationThread extends Thread {

	private Server server;
	
	public ComunicationThread(Server server) {
		super();
		this.server = server;
	}
	
	@Override
	public void run(){
		try {
			while(true){
				synchronized (server.getOpenSockets()) {
					for(Socket socket: server.getOpenSockets()){
						ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
						Object response = objectInputStream.readObject();
						ObjectOutputStream objectOutputStream;
						if(response.getClass().equals(String.class)){
							objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
							objectOutputStream.writeObject(server.getUniqueId());
							
						}else{
							Player player = (Player)response;
							server.getPlayers().put(player.id, player);
							
							objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
							objectOutputStream.writeObject(server.getPlayers());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
