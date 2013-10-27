package cargame.communication.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import cargame.communication.messaging.UdpMessage;

public class UdpMessageUtils {

	public static void sendMessage(UdpMessage msg, int port){
		DatagramSocket serverSocket = null;
		try {
			serverSocket = new DatagramSocket();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(msg);
			byte[] data = outputStream.toByteArray();
			
			DatagramPacket sendPacket = new DatagramPacket(data, data.length,msg.getAddress() , port);
			serverSocket.send(sendPacket);
			serverSocket.close();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(serverSocket!=null && !serverSocket.isClosed()){
			serverSocket.close();
		}
	}
	
	public static UdpMessage receiveMessage(int port, int messageLength, int timeout){
		DatagramSocket serverSocket = null;
		UdpMessage message = null;
		try {
//			serverSocket = new DatagramSocket(port);
			serverSocket = new DatagramSocket(null);
			serverSocket.setReuseAddress(true);
//			serverSocket.setBroadcast(true);
			serverSocket.bind(new InetSocketAddress(port));
			serverSocket.setSoTimeout(timeout);
			byte[] receiveData = new byte[messageLength];

			DatagramPacket receivePacket = new DatagramPacket(receiveData,	receiveData.length);
			serverSocket.receive(receivePacket);
			
			byte[] data = receivePacket.getData();
			ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			message = (UdpMessage)objectInputStream.readObject();
			message.setAddress(receivePacket.getAddress());
		}catch(SocketTimeoutException e){
			// Message timeout
		}catch (Exception e) {
			e.printStackTrace();
		} 
		if(serverSocket!=null && !serverSocket.isClosed()){
			serverSocket.close();
		}
		return message;
	}
}
