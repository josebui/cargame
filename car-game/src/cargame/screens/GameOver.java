package cargame.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOver implements Screen {

	private BitmapFont font;
	private SpriteBatch batch;
	
	private TweenManager tweenManager;
	
	public GameOver() {
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
//		Gdx.gl.glClearColor( 1, 0, 0, 0.5f );
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
		tweenManager.update(delta);
		
		batch.begin();
		font.draw(batch, "GAME OVER", Gdx.graphics.getWidth()/2-350,Gdx.graphics.getHeight()/2+30);
		batch.end();
	}

	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		font = new BitmapFont(Gdx.files.internal("fonts/font1.fnt"));
		font.setScale(0.4f, 0.4f);
		batch = new SpriteBatch();
		
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
		
		font.setColor(1, 1, 1, 0);
		
		tweenManager =new TweenManager();
		Tween.to(font, 0, 2.5f).target(1).ease(TweenEquations.easeInQuad).start(tweenManager);
	}

}
