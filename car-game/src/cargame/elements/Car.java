package cargame.elements;

import utils.Box2DUtils;
import cargame.CarGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Car implements Element{
	
	public static final double MAX_STEER_ANGLE = Math.PI*2.5;
	public static final float HORSEPOWERS = 1800;
	private static final Vector2 CAR_STARTING_POS = new Vector2(115,115);
	 
	// Sprites
	public static final String SPRITE_1 = "img\\car2.png";
	public static final String SPRITE_2 = "img\\car3.png";
	public static final String SPRITE_3 = "img\\car4.png";
	public static final String SPRITE_4 = "img\\coche.png";
	
	private CarGame game;
	
	private Body body;
	
	private String spritePath; 
	
	private int laps;
	
	public Car(CarGame game,String spritePath) {
		this.game = game;
		this.spritePath = spritePath;
		this.laps = 0;
		createCarObject();
	}
	
	public void setEngineSpeed(float engineSpeed){
		Vector2 vector = (new Vector2(engineSpeed,engineSpeed)); 
		body.applyForce(Box2DUtils.rotateVector(vector,body.getAngle()), body.getPosition(),true);
	}
	
	public void setSteeringAngle(float steeringAngle){
		if(body.getLinearVelocity().len() < 20) return;
		body.setAngularVelocity(steeringAngle);
	}
	
	private void createCarObject(){
		body = Box2DUtils.createPolygonBody(game.getWorld(), Car.CAR_STARTING_POS, 2f, 7f, 0.1f, 10f, 0.1f,true,false);
		
		// Car image
		Sprite sprite = new Sprite(new Texture(this.spritePath));
		sprite.setSize(15f, 15f);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		body.setUserData(sprite);
	}

	public Body getBody(){
		return body;
	}
	
	public void addLap(){
		this.laps++;
		System.out.println("Laps "+this.toString()+":"+this.laps);
	}
	
	public void removeLap(){
		this.laps--;
		System.out.println("Laps "+this.toString()+":"+this.laps);
	}

}
