package cargame.core;

import java.io.Serializable;

public class Player implements Serializable{
	private static final long serialVersionUID = -6679980973245591900L;
	public int id;
	public int health;
	public int car_id;
	public boolean isReady;
	public int lapCount;
	public MovingPosition movingPosition;
	public NetworkHealthStatus networkHealthStatus;
	public long time;
	
	private int laps;
	
	public Player() {
		this.laps = 0;
	}

	public void addLap() {
		this.laps++;
	}
	
	public int getLaps(){
		return this.laps;
	}
}