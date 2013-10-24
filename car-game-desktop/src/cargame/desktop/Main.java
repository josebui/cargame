package cargame.desktop;

import cargame.CarGame;
import cargame.desktop.listener.DesktopGameListener;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main{
	
	public static LwjglApplication startCarGameDesktop(boolean server, String serverIp, int lapsNumber, int carType){
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "car-game";
		cfg.useGL20 = false;
		cfg.width = 1024;
		cfg.height = 768;
		cfg.forceExit = false;
		
		CarGame game = CarGame.createInstance(server,serverIp,lapsNumber,carType);
		LwjglApplication app = new LwjglApplication(game, cfg);
		game.setCycleListener(new DesktopGameListener(app));
		return app;
	}
	
	public static void main(String[] args) {
		
		boolean server = ("1".equals(args[0]));
		String serverIp = (args.length>1)?args[1]:null;
		int carType = ("1".equals(args[0])?0:1);
		
		startCarGameDesktop(server,serverIp,2,carType);
	}
}
