package cargame.ui;

import cargame.screens.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
	
	public Hud(GameScreen game) {
		this.game = game;
		font = new BitmapFont(Gdx.files.internal("fonts/font1.fnt"));
		font.setScale(0.04f, 0.04f);
		batch = new SpriteBatch();
	}
	
	public void render(){
		batch.setProjectionMatrix(game.getFixedCamera().combined);
		batch.begin();
		Vector3 pos = new Vector3(0, 0, 0);
//		this.game.getCamera().unproject(pos);
		
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(this.game.getFixedCamera().combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.rect(0, this.game.getFixedCamera().viewportHeight-15, this.game.getFixedCamera().viewportWidth, 15, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
	    shapeRenderer.end();
		
		font.setColor(new Color(1f, 1f, 1f, 1));
//		font.draw(batch, "Speed: ",pos.x+3,pos.y-3);
		font.draw(batch, "Speed: ", 3,this.game.getFixedCamera().viewportHeight-3);
		font.draw(batch, "Lap: ", 200, this.game.getFixedCamera().viewportHeight-3);
//		font.draw(batch, "Lap: ", pos.x+200, pos.y-3);
		float speed = game.getPlayerCar().getSpeed();
		Color color = new Color(1, 1-speed/250.0f, 0, speed/160.0f+0.7f);
		font.setColor(color);
		font.draw(batch, Math.round(speed)+" km/h", 48,this.game.getFixedCamera().viewportHeight-3);
//		font.draw(batch, Math.round(speed)+" km/h", pos.x+48,pos.y-3);
		font.setColor(new Color(1, 1, 1, 1));
		font.draw(batch, game.getPlayerCar().getLaps()+"", 230, this.game.getFixedCamera().viewportHeight-3);
//		font.draw(batch, game.getPlayerCar().getLaps()+"", pos.x+230, pos.y-3);
		batch.end();
	}
	
	public void dispose(){
		font.dispose();
		batch.dispose();
	}
	
}
