package cargame.core;
public interface Client {
	int REQUESTS_PER_SECOND = 10;

	// public HealthStatus networkHealthStatus();

	ServerStatus ServerStatus();

	GameInfo currentGame();

	// This method is used at the first set-up of game
	void setCurrentGame(int gameInfoID, Player player);

	// this method is used inside the game to provide the current player's
	// information for server
	void sendMyPlayerInfo(Player player);

	void startGame();
	
}