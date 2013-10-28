package cargame.communication;

import java.io.Serializable;

/**
 * The Class NetworkHealthStatus.
 */
public class NetworkHealthStatus implements Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5011701254359254980L;
	//0-10; 0 is the worst quality and 10 is the best quality.
	/** The Network quality. */
	public int NetworkQuality;
	//latency as in milliseconds
	/** The latency. */
	public int latency;
}