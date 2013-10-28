package cargame.communication;
import java.util.List;

/**
 * The Class GameInfo.
 */
public class GameInfo{
	
	/** The id. */
	public int id;
	
	/** The server time stamp. */
	public long serverTimeStamp;
	
	/** The is active. */
	public boolean isActive;
	
	/** The is ready. */
	public boolean isReady;
	
	/** The players. */
	public List<Player> players;
}