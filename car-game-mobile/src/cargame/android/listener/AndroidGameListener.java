package cargame.android.listener;

import cargame.android.GameActivity;
import cargame.gamelogic.listeners.GameCycleListener;

public class AndroidGameListener implements GameCycleListener {

	private GameActivity gameActivity;

	public AndroidGameListener(GameActivity gameActivity) {
		super();
		this.gameActivity = gameActivity;
	}

	@Override
	public void endGame() {
		if(gameActivity == null){
////			System.exit(0);
////			CarGame.getInstance().restartGame();
//			Main.restartGame();
		}else{
//			gameActivity.finishActivity(0);
			gameActivity.finish();
//			carGameUI.bringToFront();
//			carGameUI.toggleButton();
//			System.out.println("Ended");
		}
	}

	@Override
	public void startGame() {
//		try {
//			// Gain focus
//			Display.setDisplayMode(Display.getDisplayMode());
//		} catch (LWJGLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
}
