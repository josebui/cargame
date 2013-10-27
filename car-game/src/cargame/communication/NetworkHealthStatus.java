package cargame.communication;

import java.io.Serializable;

public class NetworkHealthStatus implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5011701254359254980L;
	//0-10; 0 is the worst quality and 10 is the best quality.
	public int NetworkQuality;
	//latency as in milliseconds
	public int latency;
}