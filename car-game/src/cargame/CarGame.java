package cargame;

import java.util.ArrayList;
import java.util.List;

import pong.client.core.BodyEditorLoader;
import utils.Box2DUtils;
import cargame.elements.Car;
import cargame.elements.Element;
import cargame.elements.TrackSensor;
import cargame.ui.Hud;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxNativesLoader;

public class CarGame implements ApplicationListener {
	
	private static final String TRACKS_PATH = "tracks/track1";
	
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	static final float BOX_STEP = 1 / 60f;
	static final int BOX_VELOCITY_ITERATIONS = 6;
	static final int BOX_POSITION_ITERATIONS = 2;
	
	private List<Element> elements;
	private List<Sprite> staticSprites;
	
	private Hud playerHud;
	
	Car car1; // Player1's car
	Car car2; // Player2's car

	Car playerCar;
	
	static {
		GdxNativesLoader.load();
	}

	public CarGame() {
		super();
		world = new World(new Vector2(0, 0), true);
	}
	
	@Override
	public void create() {
		
		elements = new ArrayList<Element>();
		
		// Player Hud
		playerHud = new Hud(this);
		
		// Static sprites
		staticSprites = new ArrayList<Sprite>();
		
		// Background
		Sprite spriteBackground = new Sprite(new Texture("img/racetrack.png"));
		spriteBackground.setScale(0.27f,0.27f);
		spriteBackground.setOrigin(0f,0f);
		staticSprites.add(spriteBackground);
		
		Gdx.graphics.setDisplayMode(1200, 800, false);
//		Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		camera = new OrthographicCamera(276,205);  
        camera.position.set(camera.viewportWidth * 0.5f, camera.viewportHeight * 0.5f, 0f);  
        camera.update();
        
        batch = new SpriteBatch();
        
        // Boundaries
        Box2DUtils.createPolygonBody(world, new Vector2(0, 0), camera.viewportWidth, 1.0f, 0f, 0.1f, 2, false, false);
        Box2DUtils.createPolygonBody(world, new Vector2(0, camera.viewportHeight), (camera.viewportWidth) , 1.0f, 0f, 0.1f, 2, false, false);
        Box2DUtils.createPolygonBody(world, new Vector2(0, 0), 1.0f, camera.viewportHeight, 0f, 0.1f, 2, false, false);
        Box2DUtils.createPolygonBody(world, new Vector2(camera.viewportWidth, 0), 1.0f, camera.viewportHeight, 0f, 0.1f, 2, false, false);

        // Cars
        car1 = new Car(this,Car.SPRITE_2);
        playerCar = car1; 
        car2 = new Car(this,Car.SPRITE_4);
        elements.add(car1);
        elements.add(car2);
        
        //Load track
        loadTrack("track1");
        
        debugRenderer = new Box2DDebugRenderer(true,false,false,true,false,false);  
	}
	
	public void loadTrack(String trackName) {
		// Loads tracks file created with Physics body editor
		// (https://code.google.com/p/box2d-editor/)
		BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal(TRACKS_PATH));

		BodyDef bd = new BodyDef();
		bd.position.set(0, 0);
		// bd.type = BodyType.DynamicBody;

		FixtureDef fd = new FixtureDef();
		fd.density = 1;
		fd.friction = 0.1f;
		fd.restitution = 0.3f;

		Body body = world.createBody(bd);

		// Creates the box2D object with the name in the Physics body editor
		// bodies list
		loader.attachFixture(body, trackName, fd, 280);
		
		// Track contact listener
		TrackSensor sensor = new TrackSensor(this);
		world.setContactListener(sensor);
		
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		world.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);
		
		// Key listeners
		if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT)){
			car1.setSteeringAngle((float)Car.MAX_STEER_ANGLE);
		}
		if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT)){
			car1.setSteeringAngle((float)-Car.MAX_STEER_ANGLE);
		}
		if (Gdx.input.isKeyPressed(Keys.DPAD_UP)){
			car1.setEngineSpeed(Car.HORSEPOWERS);
		}
		if (Gdx.input.isKeyPressed(Keys.DPAD_DOWN)){
			car1.setEngineSpeed(-Car.HORSEPOWERS);
		}
		
		if (Gdx.input.isKeyPressed(Keys.A)){
			car2.setSteeringAngle((float)Car.MAX_STEER_ANGLE);
		}
		if (Gdx.input.isKeyPressed(Keys.D)){
			car2.setSteeringAngle((float)-Car.MAX_STEER_ANGLE);
		}
		if (Gdx.input.isKeyPressed(Keys.W)){
			car2.setEngineSpeed(Car.HORSEPOWERS);
		}
		if (Gdx.input.isKeyPressed(Keys.S)){
			car2.setEngineSpeed(-Car.HORSEPOWERS);
		}
		
		// Box2d Render
		debugRenderer.render(world, camera.combined);
		
		// Set object images
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		// Static sprites
		for(Sprite sprite: staticSprites){
			sprite.draw(batch);
		}
		
		// Hud
		playerHud.render(batch);
		
		for(Element element : elements){
			Body body = element.getBody();
			element.getBody().setAngularVelocity((float) (element.getBody().getAngularVelocity()*0.5));
			element.getBody().setLinearVelocity(element.getBody().getLinearVelocity().mul(0.95f));
			
			if(body.getUserData() != null && body.getUserData() instanceof Sprite) {
				Sprite sprite = (Sprite) body.getUserData();
				sprite.setPosition(body.getPosition().x - sprite.getWidth()/2, body.getPosition().y - sprite.getHeight() /2);
				sprite.setRotation(body.getAngle()*MathUtils.radiansToDegrees);
				sprite.draw(batch);
			}
		}
		batch.end();
		
//		debugRenderer.render(world, camera.combined);

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	public World getWorld() {
		return world;
	}

	public List<Element> getElements() {
		return elements;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public Car getPlayerCar() {
		return playerCar;
	}
	
}
