package cargame.ui;

import java.util.Map;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import cargame.elements.Car;
import cargame.screens.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class Hud {

	private GameScreen game;
	
	private BitmapFont font;
	
	private SpriteBatch batch;
	
	
	private BitmapFont msgFont;
	private TweenManager msgTweenManager;
	
	private boolean showLeaderBoard;
	
	public Hud(GameScreen game) {
		this.game = game;
		this.showLeaderBoard = false;
		font = new BitmapFont(Gdx.files.internal("fonts/font1.fnt"));
		batch = new SpriteBatch();
		
		msgFont = new BitmapFont(Gdx.files.internal("fonts/font1.fnt"));
		msgFont.setScale(0.08f, 0.08f);
		msgFont.setColor(0.6f, 0.6f, 0.6f, 1);
	}
	
	private void showMessage(float delta, String msg){
		if(msgTweenManager == null){
			
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
			
			msgTweenManager =new TweenManager();
			Tween.to(msgFont, 0 , 2.5f).target(0).ease(TweenEquations.easeInOutSine).start(msgTweenManager);
		}else if(msgTweenManager.size() != 0){
			msgTweenManager.update(delta);
			msgFont.draw(batch, msg, 10,this.game.getFixedCamera().viewportHeight/2.0f);
		}else{
			msgTweenManager.killAll();
			msgTweenManager = null;
			msgFont.setColor(0.6f, 0.6f, 0.6f, 1);
		}
	}
	
	public void render(float delta){
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		// Stats background
		Color backgroundColor = new Color(0, 0, 0, 0.7f);
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(this.game.getFixedCamera().combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.rect(0, this.game.getFixedCamera().viewportHeight-15, this.game.getFixedCamera().viewportWidth, 15, backgroundColor, backgroundColor, backgroundColor, backgroundColor);
	    shapeRenderer.end();
	    
	    float boxWidth = 100;
		float boxHeight = 100;
	    float leaderBoardX = 0;
	    float leaderBoardY = 0;
	    if(showLeaderBoard){
	    	// Leaderboard backgorund
	    	backgroundColor = new Color(0, 0, 0, 0.7f);
			shapeRenderer = new ShapeRenderer();
			shapeRenderer.setProjectionMatrix(this.game.getFixedCamera().combined);
			shapeRenderer.begin(ShapeType.Filled);
			
			leaderBoardX = this.game.getFixedCamera().viewportWidth/2 - boxWidth/2;
			leaderBoardY = this.game.getFixedCamera().viewportHeight/2 - boxHeight/2;
			shapeRenderer.rect(leaderBoardX, leaderBoardY, boxWidth, boxHeight, backgroundColor, backgroundColor, backgroundColor, backgroundColor);
		    shapeRenderer.end();
	    }
	    
	    Gdx.gl.glDisable(GL10.GL_BLEND);
	    
	    batch.setProjectionMatrix(game.getFixedCamera().combined);
		batch.begin();
		renderStats(batch);
		
		if(game.getPlayerCar().isWrongDirection()){
			showMessage(delta,"Wrong Direction");
		}
		
		if(showLeaderBoard){
			renderLeaderBoard(leaderBoardX,leaderBoardY + boxHeight,batch);
		}
		
		batch.end();
	}
	
	private void renderStats(SpriteBatch batch){
		font.setScale(0.04f, 0.04f);
		font.setColor(new Color(1f, 1f, 1f, 1));
		font.draw(batch, "Speed: ", 3,this.game.getFixedCamera().viewportHeight-3);
		font.draw(batch, "Lap: ", 200, this.game.getFixedCamera().viewportHeight-3);
		float speed = game.getPlayerCar().getSpeed();
		Color color = new Color(1, 1-speed/250.0f, 0, speed/160.0f+0.7f);
		font.setColor(color);
		font.draw(batch, Math.round(speed)+" km/h", 48,this.game.getFixedCamera().viewportHeight-3);
		font.setColor(new Color(1, 1, 1, 1));
		font.draw(batch, game.getPlayerCar().getLaps()+"", 230, this.game.getFixedCamera().viewportHeight-3);
	}
	
	private void renderLeaderBoard(float leaderBoardX, float leaderBoardY,SpriteBatch batch){
		
		float y= leaderBoardY-1;
		float x = leaderBoardX+1;
		font.setScale(0.03f);
		// header
		font.setColor(new Color(1f, 1f, 1f, 1));
		font.draw(batch, "Player", x,y);
		font.draw(batch, "Laps", x+50,y);
		y-= 10;
		
		
		Map<Integer,Car> allCars = game.getAllPlayersCars();
		for(Car car : allCars.values()){
			font.setColor(new Color(0.8f, 0.8f, 0.8f, 1));
			font.draw(batch, car.getPlayer().id+"", x,y);
			font.draw(batch, car.getPlayer().getLaps()+"",x + 50,y);
			y-=10;
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
	
}
