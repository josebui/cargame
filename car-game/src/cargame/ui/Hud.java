package cargame.ui;

import cargame.screens.GameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Hud {

	private GameScreen game;
	
	private BitmapFont font;
	
	public Hud(GameScreen game) {
		this.game = game;
		font = new BitmapFont(Gdx.files.internal("fonts/font1.fnt"));
		font.setScale(0.04f, 0.04f);
		
	}
	
	public void render(SpriteBatch batch){
		
		Vector3 pos = new Vector3(0, 0, 0);
		this.game.getCamera().unproject(pos);
		
		font.setColor(new Color(1f, 1f, 1f, 1));
		font.draw(batch, "Speed: ", (float)pos.x+3,(float)pos.y-3);
//		font.draw(batch, "Speed: ", 3,this.game.getCamera().viewportHeight-3);
//		font.draw(batch, "Lap: ", 200, this.game.getCamera().viewportHeight-3);
		font.draw(batch, "Lap: ", pos.x+200, pos.y-3);
		float speed = game.getPlayerCar().getSpeed();
		Color color = new Color(1, 1-speed/250.0f, 0, speed/160.0f+0.7f);
		font.setColor(color);
//		font.draw(batch, Math.round(speed)+" km/h", 48,this.game.getCamera().viewportHeight-3);
		font.draw(batch, Math.round(speed)+" km/h", (float)pos.x+48,(float)pos.y-3);
		font.setColor(new Color(1, 1, 1, 1));
		font.draw(batch, game.getPlayerCar().getLaps()+"", 230, this.game.getCamera().viewportHeight-3);
	}
	
	public void dispose(){
		font.dispose();
	}
	
}
