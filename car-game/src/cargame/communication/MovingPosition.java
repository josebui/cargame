package cargame.communication;

import java.io.Serializable;

/**
 * The Class MovingPosition.
 */
public class MovingPosition implements Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7380393278989300265L;
	
	/** The angular speed. */
	public float xPos, yPos, angle, linearSpeedX,linearSpeedY, angularSpeed;
	
	/**
	 * Gets the values.
	 *
	 * @return the values
	 */
	public float[] getValues(){
		return new float[]{xPos, yPos, angle, linearSpeedX,linearSpeedY, angularSpeed};
	}
	
	/**
	 * Sets the values.
	 *
	 * @param values the new values
	 */
	public void setValues(float[] values){
		xPos = values[0];
		yPos = values[1];
		angle=values[2];
		linearSpeedX = values[3];
		linearSpeedY = values[4]; 
		angularSpeed = values[5];
	}
}