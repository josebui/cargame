package cargame.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cargame.CarGame;
import cargame.communication.Player;
import cargame.elements.Car;
import cargame.elements.Element;
import cargame.elements.TrackContactListener;
import cargame.ui.Hud;
import cargame.utils.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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

public class GameScreen extends ScreenAdapter {

	private static final String TRACKS_PATH = "tracks/track1";
	private static final float BOX_STEP = 1 / 60f;
	private static final int BOX_VELOCITY_ITERATIONS = 6;
	private static final int BOX_POSITION_ITERATIONS = 2;
	
	private static final int COUNT_DOWN_NUMBER = 5;
	
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private TrackContactListener contactListener;
	
	private OrthographicCamera camera;
	private OrthographicCamera fixedCamera;
	private OrthographicCamera previewCamera;
	
	private SpriteBatch batch;
	
	private List<Element> elements;
	private List<Sprite> staticSprites;
	
	private Hud playerHud;
	
	private Car playerCar;
	private Map<Integer,Car> otherPlayersCars;
	
	private float trackWidth;
	private float trackHeight;
	
	private int lapsNumber;
	
	private long initialTrackTime;
	
	private int countDown;
	
	static {
		GdxNativesLoader.load();
	}
	
	public GameScreen() {
		super();
		world = new World(new Vector2(0, 0), true);
	}
	
	public void startGame(Player clientPlayer,int lapsNumber){
		cleanWorld();
		playerCar = new Car(clientPlayer, this, clientPlayer.car_id);
		otherPlayersCars = new HashMap<Integer, Car>();
		this.lapsNumber = lapsNumber;
		this.initialTrackTime = System.currentTimeMillis();
		this.countDown = COUNT_DOWN_NUMBER+1;
		
		// Cars
        playerCar.createCarObject();
        elements.add(playerCar);
        
        // Track contact listener
        if(contactListener == null){
        	contactListener = new TrackContactListener(this);
        	world.setContactListener(contactListener);
        }
        contactListener.startListener();
	}
	
	private void cleanWorld(){
		for(Element element : elements){
			world.destroyBody(element.getBody());
		}
		elements.clear();
	}
	
	@Override
	public void show() {
		elements = new ArrayList<Element>();
		
		// Load track
        loadTrack("track1");
		
		// Player Hud
		playerHud = new Hud(this);
		
		// Static sprites
		staticSprites = new ArrayList<Sprite>();
		
		// Background
		Sprite spriteBackground = new Sprite(new Texture("img/track4.png"));
		spriteBackground.setScale(0.27f,0.27f);
		spriteBackground.setOrigin(0f,0f);
		staticSprites.add(spriteBackground);
		
		Gdx.graphics.setDisplayMode(1200, 800, false);
//		Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		camera = new OrthographicCamera(276,205);
		previewCamera = new OrthographicCamera(2000,1486);
//		camera = new OrthographicCamera(226,165);
//		fixedCamera = new OrthographicCamera(276,205);
		fixedCamera = new OrthographicCamera(1200,800);
//		camera = new OrthographicCamera(200,150);
        camera.position.set(camera.viewportWidth * 0.5f, camera.viewportHeight * 0.5f, 0f);  
        camera.update();
        
        previewCamera.position.set(previewCamera.viewportWidth - 2700, previewCamera.viewportHeight - 2000, 0f);  
        previewCamera.update();
        
        fixedCamera.position.set(fixedCamera.viewportWidth * 0.5f, fixedCamera.viewportHeight * 0.5f, 0f);  
        fixedCamera.update();
        
        batch = new SpriteBatch();
        
        debugRenderer = new Box2DDebugRenderer(true,false,false,true,false,false);  
	}
	
	private void loadTrack(String trackName) {
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
		
		// Set track dimensions
		this.trackWidth = 195;
		this.trackHeight = 125;
		
	}

	@Override
	public void dispose() {
		super.dispose();
		this.world.dispose();
		this.batch.dispose();
		this.playerHud.dispose(); 
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		world.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);
		
		
		float cameraX = playerCar.getBody().getPosition().x;
		float cameraY = playerCar.getBody().getPosition().y;
		
		// Camera boundaries
		if(cameraX - camera.viewportWidth/2.0 < 0 ){
			cameraX = (float) (camera.viewportWidth/2.0);
		}
		
		if(cameraY - camera.viewportHeight /2.0 < 0 ){
			cameraY = (float) (camera.viewportHeight/2.0);
		}
		
		if(cameraX > trackWidth ){
			cameraX = (float) (trackWidth);
		}
		
		if(cameraY > trackHeight ){
			cameraY = (float) (trackHeight);
		}
		
//		System.out.println(cameraX+" | "+trackWidth);
//		System.out.println(cameraY+" | "+trackHeight);
		
		camera.position.set(cameraX, cameraY, 0);
		camera.update();
		
		// Game over
		playerHud.hideLeaderBoard();
		if(CarGame.getInstance().checkStatus(CarGame.STATUS_GAME_OVER)){
			playerHud.showLeaderBoard();
		} 
		
		// Playing
		if(CarGame.getInstance().checkStatus(CarGame.STATUS_PLAYING)){
			playerHud.hideLeaderBoard();
			if(CarGame.getInstance().isActionActive(CarGame.ACTION_TAB)){
				playerHud.showLeaderBoard();
			}
		}
		
		playerCar.updatePosition();
		
		for(int playerId : CarGame.getInstance().getPlayers().keySet()){
        	if(playerCar.getPlayer().id != playerId){
        		if(otherPlayersCars.containsKey(playerId)){
        			// Update other player position
        			Car otherCar = otherPlayersCars.get(playerId);
        			otherCar.setPlayer(CarGame.getInstance().getPlayers().get(playerId));
        		}else{
        			// Create new player
        			Player otherPlayer = CarGame.getInstance().getPlayers().get(playerId);
        			Car otherCar = new Car(otherPlayer, this, otherPlayer.car_id);
        			elements.add(otherCar);
        			otherCar.createCarObject();
        			otherPlayersCars.put(otherPlayer.id, otherCar);
        		}
        		
        	}
        }
		
		// Set object images
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		// Static sprites
		for(Sprite sprite: staticSprites){
			sprite.draw(batch);
		}
		
		for(Element element : elements){
			Body body = element.getBody();
			element.getBody().setAngularVelocity((float) (element.getBody().getAngularVelocity()*0.5));
			element.getBody().setLinearVelocity(element.getBody().getLinearVelocity().scl(0.95f));
			
			// Cars sprites
			if(body.getUserData() != null && body.getUserData() instanceof Sprite) {
				Sprite sprite = (Sprite) body.getUserData();
				sprite.setPosition(body.getPosition().x - sprite.getWidth()/2, body.getPosition().y - sprite.getHeight() /2);
				sprite.setRotation(body.getAngle()*MathUtils.radiansToDegrees);
				sprite.draw(batch);
			}
		}
		batch.end();
		
		// Hud
		playerHud.render(delta);
		
		// Box2d Render
//		debugRenderer.render(world, camera.combined);
		debugRenderer.render(world, previewCamera.combined);
		
		if(CarGame.getInstance().checkStatus(CarGame.STATUS_PLAYING)){
			playerCar.getPlayer().trackTime = System.currentTimeMillis() - initialTrackTime;
		}
		
		if(CarGame.getInstance().checkStatus(CarGame.STATUS_PLAYING) && playerCar.getPlayer().getLaps() >= this.lapsNumber){
			CarGame.getInstance().setStatus(CarGame.STATUS_GAME_OVER);
		}
		
		// Waiting
		if(CarGame.getInstance().checkStatus(CarGame.STATUS_WAITING)){
			
			if(otherPlayersCars.size() > 0){
				if(countDown <= 0){
					// Start race
					playerHud.showMessage("GO",new Color(0.5f, 1f, 0f, 1f),"");
					CarGame.getInstance().setStatus(CarGame.STATUS_PLAYING);
					initialTrackTime = System.currentTimeMillis();
					
					contactListener.startLapCounter();
					
				}else if(countDown == COUNT_DOWN_NUMBER+1){
					initialTrackTime = System.currentTimeMillis();
					countDown--;
				}else{
					int count = (int)((System.currentTimeMillis() - initialTrackTime)/1000.0);
					countDown = COUNT_DOWN_NUMBER - count;
					playerHud.showMessage(countDown+"",new Color(1f, 0f, 0f, 1f),"");
				}
			}else{
				playerHud.showMessage("Waiting...",new Color(1f, 1f, 0f, 1f),(CarGame.getInstance().isWaitStop())?"Nobody is online. You can keep waiting, or press space to exit.":"");
			}
		}
		if(CarGame.getInstance().checkStatus(CarGame.STATUS_PLAYING) && CarGame.getInstance().isConnectionLost()){
			playerHud.showMessage("Connection lost",new Color(1f, 1f, 0f, 1f),"Press space to exit.");
		}
		
		if(CarGame.getInstance().checkStatus(CarGame.STATUS_ENDED)){
			playerHud.showMessage("Connect and play...",new Color(1f, 1f, 0f, 1f),"");
		}
		
	}
	
	public void executeAction(int action){
		switch(action){
			case CarGame.ACTION_FORWARD:
				playerCar.setEngineSpeed(Car.HORSEPOWERS);
			break;
			case CarGame.ACTION_REVERSE:
				playerCar.setEngineSpeed(-Car.HORSEPOWERS);
			break;
			case CarGame.ACTION_RIGHT:
				playerCar.setSteeringAngle((float)-Car.MAX_STEER_ANGLE);
			break;
			case CarGame.ACTION_LEFT:
				playerCar.setSteeringAngle((float)Car.MAX_STEER_ANGLE);
			break;
		}
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
	
	public OrthographicCamera getFixedCamera() {
		return fixedCamera;
	}

	public Car getPlayerCar() {
		return playerCar;
	}
	
	public Map<Integer,Car> getAllPlayersCars(){
		Map<Integer,Car> allCars = new HashMap<Integer, Car>(this.otherPlayersCars); 
		allCars.put(this.playerCar.getPlayer().id, this.playerCar);
		return allCars;
	}

}
