package cargame.desktop.input;
import cargame.CarGame;

import com.badlogic.gdx.InputProcessor;


public class DesktopInputProcessor implements InputProcessor {
	
	private CarGame game;
	
	public DesktopInputProcessor(CarGame game) {
		super();
		this.game = game;
	}

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

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
