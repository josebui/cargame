package cargame.gamelogic.listeners;

/**
 * The listener interface for receiving gameCycle events.
 * The class that is interested in processing a gameCycle
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addGameCycleListener<code> method. When
 * the gameCycle event occurs, that object's appropriate
 * method is invoked.
 *
 * @see GameCycleEvent
 */
public interface GameCycleListener {
	
	/**
	 * End game.
	 */
	void endGame();

	/**
	 * Start game.
	 */
	void startGame();
}
