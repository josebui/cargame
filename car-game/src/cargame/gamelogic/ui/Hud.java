package cargame.gamelogic.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import cargame.CarGame;
import cargame.gamelogic.elements.Car;
import cargame.gamelogic.screens.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * The Class Hud.
 */
public class Hud {

	/** The game screen. */
	private GameScreen gameScreen;
	
	/** The font. */
	private BitmapFont font;
	
	/** The batch. */
	private SpriteBatch batch;
	
	/** The msg font. */
	private BitmapFont msgFont;
	
	/** The msg tween manager. */
	private TweenManager msgTweenManager;
	
	/** The message. */
	private String message;
	
	/** The show leader board. */
	private boolean showLeaderBoard;

	/** The sub message. */
	private String subMessage;
	
	/**
	 * Instantiates a new hud.
	 *
	 * @param game the game
	 */
	public Hud(GameScreen game) {
		this.gameScreen = game;
		this.showLeaderBoard = false;
		font = new BitmapFont(Gdx.files.internal("fonts/font2.fnt"));
		batch = new SpriteBatch();
		
		message = null;
		
		msgFont = new BitmapFont(Gdx.files.internal("fonts/font1.fnt"));
		msgFont.setColor(new Color(1f, 1f, 0f, 1));
		
		Tween.registerAccessor(BitmapFont.class, new TweenAccessor<BitmapFont>() {
			@Override
			public int getValues(BitmapFont target, int arg1, float[] returnValues) {
				returnValues[0] = target.getColor().a;
				return 1;
			}
			@Override
			public void setValues(BitmapFont target, int arg1, float[] newValues) {
				target.setColor(target.getColor().r,target.getColor().g,target.getColor().b,newValues[0]);
			}
		});
	}
	
	/**
	 * Show message.
	 *
	 * @param delta the delta
	 * @param msg the msg
	 */
	private void showMessage(float delta, String msg){
		msgFont.setScale(1f);
		if(msgTweenManager != null && msgTweenManager.size() == 0){
			this.message = null;
			this.subMessage = null;
		}
		if(msgTweenManager == null || msgTweenManager.size() == 0){
			if(msgTweenManager == null){
				msgTweenManager =new TweenManager();
			}
			msgFont.getColor().set(msgFont.getColor().r, msgFont.getColor().g, msgFont.getColor().b, 0);
			Tween tweenIn = Tween.to(msgFont, 0 , 0.7f).target(1f).ease(TweenEquations.easeInExpo);//.start(msgTweenManager);
			Tween tweenOut = Tween.to(msgFont, 0 , 0.3f).target(0f).ease(TweenEquations.easeOutExpo);//.start(msgTweenManager);
			Timeline.createSequence().push(tweenIn).push(tweenOut).start(msgTweenManager);
		}else if(msgTweenManager.size() != 0){
			msgTweenManager.update(delta);
		}
		float msgY = this.gameScreen.getFixedCamera().viewportHeight/2.0f;
		
		float msgX = this.gameScreen.getFixedCamera().viewportWidth /2.0f - 500;
		msgFont.drawMultiLine(batch, msg, msgX,msgY,1000,BitmapFont.HAlignment.CENTER);
		if(this.subMessage != null){
			
			 
			
			Color msgColor = msgFont.getColor();
			msgFont.setScale(0.3f);
			msgFont.setColor(0.11f, 0.79f, 0.8f, 0.9f);
			msgFont.drawMultiLine(batch, this.subMessage, msgX,msgY-70,1000,BitmapFont.HAlignment.CENTER);
			msgFont.setColor(msgColor);
		}
	}
	
	/**
	 * Render.
	 *
	 * @param delta the delta
	 */
	public void render(float delta){
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		// Stats background
		Color backgroundColor = new Color(0, 0, 0, 0.7f);
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(this.gameScreen.getFixedCamera().combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.rect(0, this.gameScreen.getFixedCamera().viewportHeight-140, this.gameScreen.getFixedCamera().viewportWidth, 140, backgroundColor, backgroundColor, backgroundColor, backgroundColor);
	    shapeRenderer.end();
	    
	    float boxWidth = 600;
		float boxHeight = 500;
	    float leaderBoardX = 0;
	    float leaderBoardY = 0;
	    if(showLeaderBoard){
	    	// Leaderboard backgorund
	    	backgroundColor = new Color(0, 0, 0, 0.7f);
			shapeRenderer = new ShapeRenderer();
			shapeRenderer.setProjectionMatrix(this.gameScreen.getFixedCamera().combined);
			shapeRenderer.begin(ShapeType.Filled);
			
			leaderBoardX = this.gameScreen.getFixedCamera().viewportWidth/2 - boxWidth/2;
			leaderBoardY = this.gameScreen.getFixedCamera().viewportHeight/2 - boxHeight/2;
			shapeRenderer.rect(leaderBoardX, leaderBoardY, boxWidth, boxHeight, backgroundColor, backgroundColor, backgroundColor, backgroundColor);
		    shapeRenderer.end();
	    }
	    
	    Gdx.gl.glDisable(GL10.GL_BLEND);
	    
	    batch.setProjectionMatrix(gameScreen.getFixedCamera().combined);
		batch.begin();
		renderStats(batch);
		
		if(gameScreen.getPlayerCar().isWrongDirection()){
			showMessage(delta,"Wrong Direction");
		}
		
		if(message != null){
			showMessage(delta,message);
//			message = null;
		}
		
		if(showLeaderBoard){
			renderLeaderBoard(leaderBoardX,leaderBoardY + boxHeight,batch);
		}
		
		batch.end();
	}
	
	/**
	 * Render stats.
	 *
	 * @param batch the batch
	 */
	private void renderStats(SpriteBatch batch){
//		font.setScale(0.03f, 0.03f);
		font.setScale(1f, 1f);
		font.setColor(new Color(1f, 1f, 1f, 1));
		font.draw(batch, "Speed: ", 10,this.gameScreen.getFixedCamera().viewportHeight-10);
		font.draw(batch, "Lap: ", 38, this.gameScreen.getFixedCamera().viewportHeight-45);
		font.draw(batch, "Time: ", 30, this.gameScreen.getFixedCamera().viewportHeight-87);
		font.draw(batch, "Best lap: ", 500, this.gameScreen.getFixedCamera().viewportHeight-10);
		float speed = gameScreen.getPlayerCar().getSpeed();
		Color color = new Color(1, 1-speed/250.0f, 0, speed/160.0f+0.7f);
		font.setColor(color);
		font.draw(batch, Math.round(speed)+" km/h", 100,this.gameScreen.getFixedCamera().viewportHeight-10);
		font.setColor(new Color(0.7f, 0.7f, 0.7f, 1));
		font.draw(batch, gameScreen.getPlayerCar().getPlayer().getLaps()+"", 100, this.gameScreen.getFixedCamera().viewportHeight-45);
		font.draw(batch, gameScreen.getPlayerCar().getPlayer().getTrackTimeTxt(), 100, this.gameScreen.getFixedCamera().viewportHeight-87);
		font.draw(batch, gameScreen.getPlayerCar().getPlayer().getBestLapTxt(), 623, this.gameScreen.getFixedCamera().viewportHeight-10);
		
		// Player car
		if(this.gameScreen.getPlayerCar().getSpritePath() != null) {
			Sprite sprite = new Sprite(new Texture(this.gameScreen.getPlayerCar().getSpritePath()));
//			sprite.setSize(15f, 15f);
//			sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
//			body.setUserData(sprite);
			sprite.setPosition(950, this.gameScreen.getFixedCamera().viewportHeight-100);
			sprite.rotate(180f);
			sprite.draw(batch);
		}
	}
	
	/**
	 * Render leader board.
	 *
	 * @param leaderBoardX the leader board x
	 * @param leaderBoardY the leader board y
	 * @param batch the batch
	 */
	private void renderLeaderBoard(float leaderBoardX, float leaderBoardY,SpriteBatch batch){
		Map<Integer,Car> allCars = gameScreen.getAllPlayersCars();
		List<Car> cars = new ArrayList<Car>(allCars.values());
		if(CarGame.getInstance().checkStatus(CarGame.STATUS_GAME_OVER)){
			Collections.sort(cars,new Comparator<Car>() {
				@Override
				public int compare(Car o1, Car o2) {
					return Long.compare(o1.getPlayer().getTrackTime(), o2.getPlayer().getTrackTime());
				}
			});
		}else{
			Collections.sort(cars);
		}
		
		float y= leaderBoardY-10;
		float x = leaderBoardX+10;
		float colspan = 110;
		float rowspan = 50;
		
		if(CarGame.getInstance().checkStatus(CarGame.STATUS_GAME_OVER)){
			font.setColor(new Color(1f, 0f, 0f, 1));
			font.draw(batch,"GAME OVER - "+((cars.get(0) == gameScreen.getPlayerCar())?" You win":"You lose"),x,y);
			y-=rowspan;
		}
		
		// Header
		font.setColor(new Color(1f, 1f, 1f, 1));
		font.draw(batch, "Pos", x,y);
		font.draw(batch, "Player", x+colspan,y);
		font.draw(batch, "Laps", x+colspan*2,y);
		font.draw(batch, "Best", x+colspan*3,y);
		font.draw(batch, "Time", x+colspan*4,y);
		y-= rowspan;
		 
		
		for(Car car : cars){
			font.setColor(new Color(0.8f, 0.8f, 0.8f, 1));
			if(CarGame.getInstance().checkStatus(CarGame.STATUS_GAME_OVER)){
				font.draw(batch, cars.indexOf(car)+1+"",x,y);
			}else{
				font.draw(batch, "--",x,y);
			}
			font.draw(batch, (car.getPlayer().id+1)+((car == gameScreen.getPlayerCar())?" ( me )":""), x+ colspan,y);
			font.draw(batch, car.getPlayer().getLaps()+"",x + colspan*2,y);
			font.draw(batch, car.getPlayer().getBestLapTxt(),x + colspan*3,y);
			if(CarGame.getInstance().checkStatus(CarGame.STATUS_GAME_OVER)){
				font.draw(batch, car.getPlayer().getTrackTimeTxt(),x + colspan*4,y);
			}
			y-=rowspan;
		}
		if(CarGame.getInstance().checkStatus(CarGame.STATUS_GAME_OVER)){
			font.setScale(0.7f); 
			font.setColor(new Color(1f, 0f, 0f, 1));
			font.draw(batch,"Press space to exit",x,y);
			y-=rowspan;
		}
	}
	
	/**
	 * Dispose.
	 */
	public void dispose(){
		font.dispose();
		batch.dispose();
	}

	/**
	 * Hide leader board.
	 */
	public void hideLeaderBoard() {
		this.showLeaderBoard = false;
	}
	
	/**
	 * Show leader board.
	 */
	public void showLeaderBoard() {
		this.showLeaderBoard = true;
	}

	/**
	 * Show message.
	 *
	 * @param msg the msg
	 * @param color the color
	 * @param subMsg the sub msg
	 */
	public void showMessage(String msg,Color color,String subMsg) {
		this.message = msg;
		this.subMessage = subMsg;
		msgFont.setColor(color);
		msgTweenManager = null;
	}
}
