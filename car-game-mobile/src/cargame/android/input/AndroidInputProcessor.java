package cargame.android.input;

import android.graphics.Point;
import android.util.SparseArray;
import android.view.Display;
import android.view.WindowManager;
import cargame.CarGame;

import com.badlogic.gdx.InputProcessor;


/**
 * The Class AndroidInputProcessor.
 */
public class AndroidInputProcessor implements InputProcessor {
	
	/** The game. */
	private CarGame game;
	
	/** The window manager. */
	private WindowManager windowManager;
	
	/** The touch start. */
	private SparseArray<SparseArray<Point>> touchStart;
	
	/**
	 * Instantiates a new android input processor.
	 *
	 * @param windowManager the window manager
	 */
	public AndroidInputProcessor(WindowManager windowManager) {
		super();
		this.game = CarGame.getInstance();
		this.windowManager = windowManager;
		
		touchStart = new SparseArray<SparseArray<Point>>();
		touchStart.put(0, new SparseArray<Point>());
		touchStart.put(1, new SparseArray<Point>());
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#keyDown(int)
	 */
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#keyUp(int)
	 */
	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#keyTyped(char)
	 */
	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#touchDown(int, int, int, int)
	 */
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(game.checkStatus(CarGame.STATUS_GAME_OVER) || game.isConnectionLost()){
			game.addAction(CarGame.ACTION_SPACE);
		}
		if(game.checkStatus(CarGame.STATUS_WAITING) || game.isWaitStop()){
			game.addAction(CarGame.ACTION_SPACE);
		}
		Point size = getScreenSize();
		if(screenX > size.x/2){ // Right side
			this.touchStart.get(0).put(pointer, new Point(screenX, screenY));
		}else{ // Left side
			this.touchStart.get(1).put(pointer, new Point(screenX, screenY));
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#touchUp(int, int, int, int)
	 */
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		game.removeAction(CarGame.ACTION_SPACE);
		
		if(this.touchStart.get(0).indexOfKey(pointer) >= 0){
			this.touchStart.get(0).clear();
			game.removeAction(CarGame.ACTION_FORWARD);
			game.removeAction(CarGame.ACTION_REVERSE);
		}
		
		if(this.touchStart.get(1).indexOfKey(pointer) >= 0){
			this.touchStart.get(1).clear();
			game.removeAction(CarGame.ACTION_LEFT);
			game.removeAction(CarGame.ACTION_RIGHT);
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#touchDragged(int, int, int)
	 */
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		
		if(this.touchStart.get(1).size() > 0 && this.touchStart.get(1).indexOfKey(pointer) >= 0){
			
			Point initial = this.touchStart.get(1).get(pointer);
			int difX = initial.x - screenX;
			System.out.println("Curva "+difX+" ScreenX: "+screenX+", ScreenY:"+screenY+", pointer:"+pointer);
			if(difX > 20){
				game.removeAction(CarGame.ACTION_RIGHT);
				game.addAction(CarGame.ACTION_LEFT);
			}else if(difX < -20){
				game.removeAction(CarGame.ACTION_LEFT);
				game.addAction(CarGame.ACTION_RIGHT);
			}
		}
		
		if(this.touchStart.get(0).size() > 0 && this.touchStart.get(0).indexOfKey(pointer) >= 0){
			System.out.println("Acelera ScreenX: "+screenX+", ScreenY:"+screenY+", pointer:"+pointer);
			Point initial = this.touchStart.get(0).get(pointer);
			int difY = initial.y - screenY;
			if(difY > 30){
				game.removeAction(CarGame.ACTION_REVERSE);
				game.addAction(CarGame.ACTION_FORWARD);
			}else if(difY < -30){
				game.removeAction(CarGame.ACTION_FORWARD);
				game.addAction(CarGame.ACTION_REVERSE);
			}
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#mouseMoved(int, int)
	 */
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#scrolled(int)
	 */
	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	/**
	 * Gets the screen size.
	 *
	 * @return the screen size
	 */
	private Point getScreenSize(){
		Display display = this.windowManager.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size;
	}
	
}
