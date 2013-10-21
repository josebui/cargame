package cargame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main{
	
	public static void startCarGameDesktop(boolean server, String serverIp, int lapsNumber, int carType){
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "car-game";
		cfg.useGL20 = false;
		cfg.width = 1024;
		cfg.height = 768;
		
		new LwjglApplication(CarGame.createInstance(server,serverIp,lapsNumber,carType), cfg);
	}
	
	public static void main(String[] args) {
		
		boolean server = ("1".equals(args[0]));
		String serverIp = (args.length>1)?args[1]:null;
		int carType = ("1".equals(args[0])?0:1);
		
		startCarGameDesktop(server,serverIp,1,carType);
	}
}
