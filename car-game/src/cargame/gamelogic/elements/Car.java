package cargame.gamelogic.elements;

import cargame.communication.MovingPosition;
import cargame.communication.Player;
import cargame.gamelogic.screens.GameScreen;
import cargame.gamelogic.utils.Box2DUtils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * The Class Car.
 */
public class Car implements Element,Comparable<Car>{
	
	/** The Constant MAX_STEER_ANGLE. */
	public static final double MAX_STEER_ANGLE = Math.PI*2.5;
	
	/** The Constant HORSEPOWERS. */
	public static final float HORSEPOWERS = 2000;
	
	/** The Constant CAR_STARTING_POS. */
	private static final Vector2 CAR_STARTING_POS = new Vector2(115,115);
	 
	// Sprites
	/** The Constant SPRITE_1. */
	public static final String SPRITE_1 = "img\\coche.png";
	
	/** The Constant SPRITE_2. */
	public static final String SPRITE_2 = "img\\car3.png";
	
	/** The Constant SPRITE_3. */
	public static final String SPRITE_3 = "img\\car4.png";
	
	/** The Constant SPRITE_4. */
	public static final String SPRITE_4 = "img\\car2.png";
	
	/** The Constant TYPE_1. */
	public static final int TYPE_1 = 0;
	
	/** The Constant TYPE_2. */
	public static final int TYPE_2 = 1;
	
	/** The Constant TYPE_3. */
	public static final int TYPE_3 = 2;
	
	/** The Constant TYPE_4. */
	public static final int TYPE_4 = 3;
	
	
	/** The game. */
	private GameScreen game;
	
	/** The body. */
	private Body body;
	
	/** The sprite path. */
	private String spritePath; 
	
	/** The player. */
	private Player player;
	
	/** The wrong direction. */
	private boolean wrongDirection;
	
	/**
	 * Instantiates a new car.
	 *
	 * @param player the player
	 * @param game the game
	 * @param carType the car type
	 */
	public Car(Player player,GameScreen game,int carType) {
		this.game = game;
		player.trackTime = 0;
		this.player =player;
		this.wrongDirection = false;
		
		switch(carType){
			case TYPE_1:
				this.spritePath = SPRITE_1;
			break;
			case TYPE_2:
				this.spritePath = SPRITE_2;
			break;
			case TYPE_3:
				this.spritePath = SPRITE_3;
			break;
			case TYPE_4:
				this.spritePath = SPRITE_4;
			break;
		}
	}
	
	/**
	 * Sets the engine speed.
	 *
	 * @param engineSpeed the new engine speed
	 */
	public void setEngineSpeed(float engineSpeed){
		Vector2 vector = (new Vector2(engineSpeed,engineSpeed)); 
		body.applyForce(Box2DUtils.rotateVector(vector,body.getAngle()), body.getPosition(),true);
	}
	
	/**
	 * Sets the steering angle.
	 *
	 * @param steeringAngle the new steering angle
	 */
	public void setSteeringAngle(float steeringAngle){
		if(body.getLinearVelocity().len() < 20) return;
		body.setAngularVelocity(steeringAngle);
		body.setAngularVelocity((float) (body.getAngularVelocity()*0.5));
	}
	
	/**
	 * Creates the car object.
	 */
	public void createCarObject(){
		body = Box2DUtils.createPolygonBody(game.getWorld(), Car.CAR_STARTING_POS, 2f, 7f, 0.1f, 10f, 0.1f,true,false);
		
		// Car image
		Sprite sprite = new Sprite(new Texture(this.spritePath));
		sprite.setSize(15f, 15f);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		body.setUserData(sprite);
	}

	/* (non-Javadoc)
	 * @see cargame.gamelogic.elements.Element#getBody()
	 */
	public Body getBody(){
		return body;
	}
	
	/**
	 * Adds the lap.
	 *
	 * @param time the time
	 */
	public void addLap(long time){
		this.player.addLap(time);
	}
	
	/**
	 * Gets the speed.
	 *
	 * @return the speed
	 */
	public float getSpeed(){
		return getBody().getLinearVelocity().len()*1.5f;
	}
	
	/**
	 * Sets the position.
	 *
	 * @param movingPosition the new position
	 */
	public void setPosition(MovingPosition movingPosition){
		if(movingPosition == null) return;
		this.getBody().setTransform(movingPosition.xPos, movingPosition.yPos, movingPosition.angle);
		this.getBody().setLinearVelocity(new Vector2(movingPosition.linearSpeedX, movingPosition.linearSpeedY));
		this.getBody().setAngularVelocity(movingPosition.angularSpeed);
	}

	/**
	 * Gets the player.
	 *
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Sets the player.
	 *
	 * @param player the new player
	 */
	public void setPlayer(Player player) {
//		if(player.time <= this.player.time) return;
		this.player = player;
		this.setPosition(player.movingPosition);
	}
	
	/**
	 * Update position.
	 */
	public void updatePosition(){
		if(player.movingPosition == null){
			player.movingPosition = new MovingPosition();
		}
		
		player.movingPosition.xPos = getBody().getPosition().x;
		player.movingPosition.yPos = getBody().getPosition().y;
		player.movingPosition.angle = getBody().getAngle();
		player.movingPosition.linearSpeedX = getBody().getLinearVelocity().x;
		player.movingPosition.linearSpeedY = getBody().getLinearVelocity().y;
		player.movingPosition.angularSpeed = getBody().getAngularVelocity();
	}

	/**
	 * Sets the wrong direction.
	 *
	 * @param wrongDirection the new wrong direction
	 */
	public void setWrongDirection(boolean wrongDirection) {
		this.wrongDirection = wrongDirection;
	}

	/**
	 * Checks if is wrong direction.
	 *
	 * @return true, if is wrong direction
	 */
	public boolean isWrongDirection() {
		return wrongDirection;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Car o) {
		if(o.getPlayer().getLaps() == this.getPlayer().getLaps() && o.getPlayer().getBestLapTime() == this.getPlayer().getBestLapTime()){
			return 0;
		}
		if(o.getPlayer().getLaps() > this.getPlayer().getLaps()){
			return 1;
		}else if(o.getPlayer().getBestLapTime() < this.getPlayer().getBestLapTime()){
			return 1;
		}else{
			return -1;
		}
	}

	/**
	 * Gets the sprite path.
	 *
	 * @return the sprite path
	 */
	public String getSpritePath() {
		return spritePath;
	}
	
}
