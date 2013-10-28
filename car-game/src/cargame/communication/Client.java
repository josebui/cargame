package cargame.communication;

/**
 * The Interface Client.
 */
public interface Client {
	
	/** The requests per second. */
	int REQUESTS_PER_SECOND = 10;

	/**
	 * Server status.
	 *
	 * @return the server status
	 */
	ServerStatus ServerStatus();

	/**
	 * Current game.
	 *
	 * @return the game info
	 */
	GameInfo currentGame();

	/**
	 * Sets the current game. This method is used at the first set-up of game
	 *
	 * @param gameInfoID the game info id
	 * @param player the player
	 */
	void setCurrentGame(int gameInfoID, Player player);

	/**
	 * Send my player info. this method is used inside the game to provide the current player's information for server
	 *
	 * @param player the player
	 */
	void sendMyPlayerInfo(Player player);

}