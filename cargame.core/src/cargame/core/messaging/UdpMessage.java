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
	
	public UdpMessage(int type, Serializable data, long time) {
		super();
		this.type = type;
		this.data = data;
		this.time = time;
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
	
}
