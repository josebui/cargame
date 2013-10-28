package cargame.communication;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * The Class Player.
 */
public class Player implements Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6679980973245591900L;
	
	/** The id. */
	public int id;
	
	/** The health. */
	public int health;
	
	/** The car_id. */
	public int car_id;
	
	/** The is ready. */
	public boolean isReady;
	
	/** The lap count. */
	public int lapCount;
	
	/** The moving position. */
	public MovingPosition movingPosition;
	
	/** The network health status. */
	public NetworkHealthStatus networkHealthStatus;
	
	/** The best lap time. */
	private long bestLapTime;
	
	/** The track time. */
	public long trackTime;

	/** The laps. */
	private int laps;
	
	/** The df. */
	private transient DecimalFormat df;
	
	/**
	 * Instantiates a new player.
	 */
	public Player() {
		this.laps = 0;
		this.bestLapTime = Long.MAX_VALUE;
	}

	/**
	 * Adds the lap.
	 *
	 * @param time the time
	 */
	public void addLap(long time) {
		this.laps++;
		if(time < bestLapTime){
			this.bestLapTime = time;
		}
	}
	
	/**
	 * Gets the laps.
	 *
	 * @return the laps
	 */
	public int getLaps(){
		return this.laps;
	}
	
	/**
	 * Gets the number formatter.
	 *
	 * @return the number formatter
	 */
	private DecimalFormat getNumberFormatter(){
		if(df == null){
			df = new DecimalFormat("#.##");
		}
		return df;
	}
	
	/**
	 * Gets the track time txt.
	 *
	 * @return the track time txt
	 */
	public String getTrackTimeTxt(){
        return getNumberFormatter().format(trackTime/1000.0);
	}
	
	/**
	 * Gets the best lap txt.
	 *
	 * @return the best lap txt
	 */
	public String getBestLapTxt(){
		if(this.bestLapTime == Long.MAX_VALUE){
			return "--";
		}
		Double best = bestLapTime/1000.0;
        return getNumberFormatter().format(best);
	}

	/**
	 * Gets the best lap time.
	 *
	 * @return the best lap time
	 */
	public long getBestLapTime() {
		return bestLapTime;
	}

	/**
	 * Gets the track time.
	 *
	 * @return the track time
	 */
	public long getTrackTime() {
		return trackTime;
	}
	
	
}