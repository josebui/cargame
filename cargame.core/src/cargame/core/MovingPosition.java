package cargame.core;

import java.io.Serializable;

public class MovingPosition implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7380393278989300265L;
	public float xPos, yPos, angle, linearSpeedX,linearSpeedY, angularSpeed;
	
	public float[] getValues(){
		return new float[]{xPos, yPos, angle, linearSpeedX,linearSpeedY, angularSpeed};
	}
	
	public void setValues(float[] values){
		xPos = values[0];
		yPos = values[1];
		angle=values[2];
		linearSpeedX = values[3];
		linearSpeedY = values[4]; 
		angularSpeed = values[5];
	}
}