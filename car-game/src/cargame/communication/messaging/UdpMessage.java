package cargame.communication.messaging;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * The Class UdpMessage.
 */
public class UdpMessage implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4858509450358488163L;
	
	/** The Constant TYPE_PLAYER_DATA. */
	public static final int TYPE_PLAYER_DATA = 0;
	
	/** The Constant TYPE_SYNC_MESSAGE. */
	public static final int TYPE_SYNC_MESSAGE = 1;
	
	/** The type. */
	private int type;
	
	/** The data. */
	private Serializable data;
	
	/** The time. */
	private long time;
	
	/** The address. */
	private transient InetAddress address;
	
	/** The game id. */
	private String gameId;
	
	/**
	 * Instantiates a new udp message.
	 *
	 * @param type the type
	 * @param data the data
	 * @param time the time
	 * @param gameId the game id
	 */
	public UdpMessage(int type, Serializable data, long time,String gameId) {
		super();
		this.type = type;
		this.data = data;
		this.time = time;
		this.gameId = gameId;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public Serializable getData() {
		return data;
	}

	/**
	 * Gets the time.
	 *
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public InetAddress getAddress() {
		return address;
	}

	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(InetAddress address) {
		this.address = address;
	}

	/**
	 * Gets the game id.
	 *
	 * @return the game id
	 */
	public String getGameId() {
		return this.gameId;
	}
	
}
