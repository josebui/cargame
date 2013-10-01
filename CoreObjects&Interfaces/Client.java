public interface Client{
private final int REQUESTS_PER_SECOND=10;

public HealthStatus networkHealthStatus(){
}

public ServerStatus ServerStatus(){
}

public GameInfo currentGame(){
}

//This method is used at the first set-up of game
public setCurrentGame( int gameInfoID, Player player){
}

//this method is used inside the game to provide the current player's information for server
public sendMyPlayerInfo(Player player){
}
}