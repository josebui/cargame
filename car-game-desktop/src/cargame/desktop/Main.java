package cargame.desktop;

import cargame.CarGame;
import cargame.desktop.gui.CarGameUI;
import cargame.desktop.input.DesktopInputProcessor;
import cargame.desktop.listener.DesktopApplicationListener;
import cargame.desktop.listener.DesktopGameListener;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main{
	
	private static LwjglApplication app;
	
	public static LwjglApplication startCarGameDesktop(CarGameUI carGameUI, String gameId, boolean server, String serverIp, int lapsNumber, int carType){
		if(CarGame.getInstance() == null){
			LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
			cfg.title = "car-game";
			cfg.useGL20 = false;
			cfg.width = 1024;
			cfg.height = 768;
			cfg.forceExit = true;
			cfg.resizable = true;
	//		cfg.vSyncEnabled = true;
			
			CarGame game = CarGame.createInstance(gameId,server,serverIp,lapsNumber,carType);
			app = new LwjglApplication(game, cfg);
			app.addLifecycleListener(new DesktopApplicationListener(carGameUI));
			app.getInput().setInputProcessor(new DesktopInputProcessor(game));
			game.setGameCycleListener(new DesktopGameListener(carGameUI));
		}else{
			CarGame.getInstance().newGame(gameId,server, serverIp, lapsNumber, carType);
		}
		return app;
	}
	
	public static void main(String[] args) {
		
		boolean server = ("1".equals(args[0]));
		String serverIp = (args.length>1)?args[1]:null;
		int carType = ("1".equals(args[0])?0:1);
		
		startCarGameDesktop(null,"localgame",server,serverIp,1,carType);
	}

	public static void restartGame() {
		startCarGameDesktop(null,"localgame",true,"127.0.0.1",1,0);
	}
}
