package cargame.error;


/**
 * The Class ShootError.
 */
public class ShootError {
	
	/** The log. */
	private static ShootErrorImplementation log;
	
	static{
		log = null;
	}
	
	/**
	 * Register shoot error implementation.
	 *
	 * @param log the log
	 */
	public static void registerShootErrorImplementation(ShootErrorImplementation log){
		ShootError.log = log;
	}
	
	/**
	 * Shoot.
	 *
	 * @param messageHeader the message header
	 * @param message the message
	 */
	public static void shoot(final String messageHeader, final String message){
		log.writeMessage(messageHeader, message);
	}
}
