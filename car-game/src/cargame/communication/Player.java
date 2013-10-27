package cargame.communication;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Player implements Serializable{
	private static final long serialVersionUID = -6679980973245591900L;
	public int id;
	public int health;
	public int car_id;
	public boolean isReady;
	public int lapCount;
	public MovingPosition movingPosition;
	public NetworkHealthStatus networkHealthStatus;
	
	private long bestLapTime;
	public long trackTime;

	private int laps;
	
	private transient DecimalFormat df;
	
	public Player() {
		this.laps = 0;
		this.bestLapTime = Long.MAX_VALUE;
	}

	public void addLap(long time) {
		this.laps++;
		if(time < bestLapTime){
			this.bestLapTime = time;
		}
	}
	
	public int getLaps(){
		return this.laps;
	}
	
	private DecimalFormat getNumberFormatter(){
		if(df == null){
			df = new DecimalFormat("#.##");
		}
		return df;
	}
	
	public String getTrackTimeTxt(){
        return getNumberFormatter().format(trackTime/1000.0);
	}
	
	public String getBestLapTxt(){
		if(this.bestLapTime == Long.MAX_VALUE){
			return "--";
		}
		Double best = bestLapTime/1000.0;
        return getNumberFormatter().format(best);
	}

	public long getBestLapTime() {
		return bestLapTime;
	}

	public long getTrackTime() {
		return trackTime;
	}
	
	
}