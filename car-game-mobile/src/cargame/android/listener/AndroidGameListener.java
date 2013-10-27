package cargame.android.listener;

import android.app.Activity;
import cargame.listeners.GameCycleListener;

public class AndroidGameListener implements GameCycleListener {

	private Activity gameActivity;

	public AndroidGameListener(Activity gameActivity) {
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
