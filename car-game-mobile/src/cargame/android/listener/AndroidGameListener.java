package cargame.android.listener;

import cargame.android.GameActivity;
import cargame.gamelogic.listeners.GameCycleListener;

/**
 * The listener interface for receiving androidGame events.
 * The class that is interested in processing a androidGame
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addAndroidGameListener<code> method. When
 * the androidGame event occurs, that object's appropriate
 * method is invoked.
 *
 * @see AndroidGameEvent
 */
public class AndroidGameListener implements GameCycleListener {

	/** The game activity. */
	private GameActivity gameActivity;

	/**
	 * Instantiates a new android game listener.
	 *
	 * @param gameActivity the game activity
	 */
	public AndroidGameListener(GameActivity gameActivity) {
		super();
		this.gameActivity = gameActivity;
	}

	/* (non-Javadoc)
	 * @see cargame.gamelogic.listeners.GameCycleListener#endGame()
	 */
	@Override
	public void endGame() {
		if(gameActivity == null){
		}else{
			gameActivity.finish();
		}
	}

	/* (non-Javadoc)
	 * @see cargame.gamelogic.listeners.GameCycleListener#startGame()
	 */
	@Override
	public void startGame() {
	}
	
}
