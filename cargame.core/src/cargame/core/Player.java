package cargame.core;

import java.io.Serializable;

public class Player implements Serializable{
	public int id;
	public int health;
	public int car_id;
	public boolean isReady;
	public int lapCount;
	public MovingPosition movingPosition;
	public NetworkHealthStatus networkHealthStatus;
}