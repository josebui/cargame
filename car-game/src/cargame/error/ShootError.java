package cargame.error;


public class ShootError {
	
	private static ShootErrorImplementation log;
	
	static{
		log = null;
	}
	
	public static void registerShootErrorImplementation(ShootErrorImplementation log){
		ShootError.log = log;
	}
	
	public static void shoot(final String messageHeader, final String message){
		log.writeMessage(messageHeader, message);
	}
}
