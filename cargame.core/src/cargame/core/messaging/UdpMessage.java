package cargame.core.messaging;

import java.io.Serializable;
import java.net.InetAddress;

public class UdpMessage implements Serializable {
	
	private static final long serialVersionUID = -4858509450358488163L;
	public static final int TYPE_PLAYER_DATA = 0;
	public static final int TYPE_SYNC_MESSAGE = 1;
	
	private int type;
	private Serializable data;
	private long time;
	private transient InetAddress address;
	private String gameId;
	
	public UdpMessage(int type, Serializable data, long time,String gameId) {
		super();
		this.type = type;
		this.data = data;
		this.time = time;
		this.gameId = gameId;
	}

	public int getType() {
		return type;
	}

	public Serializable getData() {
		return data;
	}

	public long getTime() {
		return time;
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public String getGameId() {
		return this.gameId;
	}
	
}
