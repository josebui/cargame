package cargame.desktop.input;
import cargame.CarGame;

import com.badlogic.gdx.InputProcessor;


/**
 * The Class DesktopInputProcessor.
 */
public class DesktopInputProcessor implements InputProcessor {
	
	/** The game. */
	private CarGame game;
	
	/**
	 * Instantiates a new desktop input processor.
	 *
	 * @param game the game
	 */
	public DesktopInputProcessor(CarGame game) {
		super();
		this.game = game;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#keyDown(int)
	 */
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode){
			case 19: //Up
				game.addAction(CarGame.ACTION_FORWARD);
			break;
			case 20: //Down
				game.addAction(CarGame.ACTION_REVERSE);
			break;
			case 21: //Left
				game.addAction(CarGame.ACTION_LEFT);
			break;
			case 22: //Right
				game.addAction(CarGame.ACTION_RIGHT);
			break;
			case 61: //Tab
				game.addAction(CarGame.ACTION_TAB);
			break;
			case 62: //Space
				game.addAction(CarGame.ACTION_SPACE);
			break;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#keyUp(int)
	 */
	@Override
	public boolean keyUp(int keycode) {
		switch(keycode){
		case 19: //Up
			game.removeAction(CarGame.ACTION_FORWARD);
		break;
		case 20: //Down
			game.removeAction(CarGame.ACTION_REVERSE);
		break;
		case 21: //Left
			game.removeAction(CarGame.ACTION_LEFT);
		break;
		case 22: //Right
			game.removeAction(CarGame.ACTION_RIGHT);
		break;
		case 61: //Tab
			game.removeAction(CarGame.ACTION_TAB);
		break;
		case 62: //Space
			game.removeAction(CarGame.ACTION_SPACE);
		break;
	}
		return true;
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
		return false;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#touchUp(int, int, int, int)
	 */
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#touchDragged(int, int, int)
	 */
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
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

}
