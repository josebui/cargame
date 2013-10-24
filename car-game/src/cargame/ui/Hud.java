package cargame.ui;

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
import cargame.elements.Car;
import cargame.screens.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Hud {

	private GameScreen gameScreen;
	
	private BitmapFont font;
	
	private SpriteBatch batch;
	
	private BitmapFont msgFont;
	private TweenManager msgTweenManager;
	private String message;
	
	private boolean showLeaderBoard;

	private String subMessage;
	
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
		float msgX = this.gameScreen.getFixedCamera().viewportWidth /2.0f;
		msgX -= ((msg.length()*20)/2.0); 
		float msgY = this.gameScreen.getFixedCamera().viewportHeight/2.0f;
		msgFont.draw(batch, msg, msgX,msgY);
		if(this.subMessage != null){
			
			 
			
			Color msgColor = msgFont.getColor();
			msgFont.setScale(0.3f);
			float subMsgX = this.gameScreen.getFixedCamera().viewportWidth /2.0f;
			subMsgX -= ((subMessage.length()*20*msgFont.getScaleX())/2.0);
			msgFont.setColor(0.11f, 0.79f, 0.8f, 0.9f);
			msgFont.draw(batch, this.subMessage, subMsgX,msgY-70);
			msgFont.setColor(msgColor);
		}
	}
	
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
	}
	
	private void renderLeaderBoard(float leaderBoardX, float leaderBoardY,SpriteBatch batch){
		
		float y= leaderBoardY-10;
		float x = leaderBoardX+10;
		float colspan = 110;
		float rowspan = 50;
		
		if(CarGame.getInstance().isGameOver()){
			font.setColor(new Color(1f, 0f, 0f, 1));
			font.draw(batch,"GAME OVER",x,y);
			y-=rowspan;
		}
		
		// header
		
		font.setColor(new Color(1f, 1f, 1f, 1));
		font.draw(batch, "Pos", x,y);
		font.draw(batch, "Player", x+colspan,y);
		font.draw(batch, "Laps", x+colspan*2,y);
		font.draw(batch, "Best", x+colspan*3,y);
		font.draw(batch, "Time", x+colspan*4,y);
		y-= rowspan;
		
		
		Map<Integer,Car> allCars = gameScreen.getAllPlayersCars();
		List<Car> cars = new ArrayList<Car>(allCars.values()); 
		if(CarGame.getInstance().isGameOver()){
			Collections.sort(cars,new Comparator<Car>() {
				@Override
				public int compare(Car o1, Car o2) {
					return Long.compare(o1.getPlayer().getTrackTime(), o2.getPlayer().getTrackTime());
				}
			});
		}else{
			Collections.sort(cars);
		}
		for(Car car : cars){
			font.setColor(new Color(0.8f, 0.8f, 0.8f, 1));
			if(CarGame.getInstance().isGameOver()){
				font.draw(batch, cars.indexOf(car)+1+"",x,y);
			}else{
				font.draw(batch, "--",x,y);
			}
			font.draw(batch, (car.getPlayer().id+1)+((car == gameScreen.getPlayerCar())?" ( me )":""), x+ colspan,y);
			font.draw(batch, car.getPlayer().getLaps()+"",x + colspan*2,y);
			font.draw(batch, car.getPlayer().getBestLapTxt(),x + colspan*3,y);
			if(CarGame.getInstance().isGameOver()){
				font.draw(batch, car.getPlayer().getTrackTimeTxt(),x + colspan*4,y);
			}
			y-=rowspan;
		}
		
	}
	
	public void dispose(){
		font.dispose();
		batch.dispose();
	}

	public void hideLeaderBoard() {
		this.showLeaderBoard = false;
	}
	public void showLeaderBoard() {
		this.showLeaderBoard = true;
	}

	public void showMessage(String msg,Color color,String subMsg) {
		this.message = msg;
		this.subMessage = subMsg;
		msgFont.setColor(color);
		msgTweenManager = null;
	}
}
