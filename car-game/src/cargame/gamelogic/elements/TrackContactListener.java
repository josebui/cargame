package cargame.gamelogic.elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import cargame.CarGame;
import cargame.gamelogic.screens.GameScreen;
import cargame.gamelogic.utils.Box2DUtils;

/**
 * The listener interface for receiving trackContact events.
 * The class that is interested in processing a trackContact
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addTrackContactListener<code> method. When
 * the trackContact event occurs, that object's appropriate
 * method is invoked.
 *
 * @see TrackContactEvent
 */
public class TrackContactListener implements ContactListener {

	/** The game screen. */
	private GameScreen gameScreen;
	
	/** The sensor1. */
	private Body sensor1;
	
	/** The sensor2. */
	private Body sensor2;
	
	/** The wrong direction. */
	private Map<Car,Boolean> wrongDirection;
	
	/** The passed sensor1. */
	private Map<Car,Boolean> passedSensor1;
	
	/** The passed sensor2. */
	private Map<Car,Boolean> passedSensor2;
	
	/** The initial lap time. */
	private long initialLapTime;
	
	/**
	 * Instantiates a new track contact listener.
	 *
	 * @param game the game
	 */
	public TrackContactListener(GameScreen game) {
		super();
		
		this.gameScreen = game;
		
		// Finish line sensors
		passedSensor1 = new HashMap<Car, Boolean>();
		passedSensor2 = new HashMap<Car, Boolean>();
		sensor2 = Box2DUtils.createPolygonBody(game.getWorld(), new Vector2(135, 25), 1f, 15f, 0f, 0f, 0f, false,true);
		sensor1 = Box2DUtils.createPolygonBody(game.getWorld(), new Vector2(140, 25), 1f, 15f, 0f, 0f, 0f, false,true);
		
		wrongDirection =  new HashMap<Car, Boolean>();
	}
	
	/**
	 * Start listener.
	 */
	public void startListener(){
		wrongDirection.clear();
		passedSensor1.clear();
		passedSensor2.clear();
		
		// Move cars to start line
		for(int i=0;i<this.gameScreen.getElements().size();i++){
			Car car = (Car) this.gameScreen.getElements().get(i);
			if(car != null){
				wrongDirection.put(car, true);
				car.getBody().setTransform(sensor1.getPosition().x+20, sensor1.getPosition().y +6 -this.gameScreen.getPlayerCar().getPlayer().id*10, (float)( sensor1.getAngle()-Math.PI/2.0));
			}
			
		}
	}
	
	/**
	 * Start lap counter.
	 */
	public void startLapCounter(){
		initialLapTime = System.currentTimeMillis();
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.physics.box2d.ContactListener#endContact(com.badlogic.gdx.physics.box2d.Contact)
	 */
	@Override
	public void endContact(Contact contact) {

		if(!CarGame.getInstance().checkStatus(CarGame.STATUS_PLAYING)) return;
		// Finish line sensors
		Car car = getCarCollision(contact,sensor1);
        if(car != null && car == gameScreen.getPlayerCar()){
        	passedSensor1.put(car, true);
        	if(passedSensor2.containsKey(car) && passedSensor2.get(car)){
        		if(!wrongDirection.containsKey(car) || !wrongDirection.get(car)){
        			wrongDirection.put(car,true); 
        			car.setWrongDirection(true);
        		}
        		
        		passedSensor2.put(car, false);
        		passedSensor1.put(car, false);
        	}
        }
        car = getCarCollision(contact,sensor2);
        if(car != null && car == gameScreen.getPlayerCar()){
        	passedSensor2.put(car, true);
        	if(passedSensor1.containsKey(car) && passedSensor1.get(car)){
        		if(wrongDirection.containsKey(car) && wrongDirection.get(car)){
        			wrongDirection.put(car, false);
        			car.setWrongDirection(false);
        		}else{
        			car.addLap(System.currentTimeMillis() - initialLapTime);
        			initialLapTime = System.currentTimeMillis();
        		}
        		passedSensor1.put(car, false);
        		passedSensor2.put(car, false);
        	}
        }
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.physics.box2d.ContactListener#beginContact(com.badlogic.gdx.physics.box2d.Contact)
	 */
	@Override
	public void beginContact(Contact contact) {
	}
	
	/**
	 * Gets the car collision.
	 *
	 * @param contact the contact
	 * @param sensor the sensor
	 * @return the car collision
	 */
	private Car getCarCollision(Contact contact,Body sensor){
		List<Element> gameElements = gameScreen.getElements();
		
		for(Element element: gameElements){
			if(contact.getFixtureA().getBody().equals(element.getBody()) && contact.getFixtureB().getBody().equals(sensor)){
				return (Car) element;
			}
			if(contact.getFixtureB().getBody().equals(element.getBody()) && contact.getFixtureA().getBody().equals(sensor)){
				return (Car) element;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.physics.box2d.ContactListener#postSolve(com.badlogic.gdx.physics.box2d.Contact, com.badlogic.gdx.physics.box2d.ContactImpulse)
	 */
	@Override
	public void postSolve(Contact arg0, ContactImpulse arg1) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.physics.box2d.ContactListener#preSolve(com.badlogic.gdx.physics.box2d.Contact, com.badlogic.gdx.physics.box2d.Manifold)
	 */
	@Override
	public void preSolve(Contact arg0, Manifold arg1) {
		// TODO Auto-generated method stub
		
	}
	
	
}
