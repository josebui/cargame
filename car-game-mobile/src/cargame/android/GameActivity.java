package cargame.android;

import cargame.CarGame;
import cargame.android.input.AndroidInputProcessor;
import cargame.android.listener.AndroidApplicationListener;
import cargame.android.listener.AndroidGameListener;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class GameActivity extends AndroidApplication {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent i = getIntent();
		String gameId = i.getStringExtra("gameId");
		boolean server = i.getBooleanExtra("server",false);
		String peerIp = i.getStringExtra("peerIp");
		
		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        CarGame game = CarGame.createInstance(gameId,server,peerIp,1,(server)?1:3); 
        initialize(game, cfg);
        this.getInput().setInputProcessor(new AndroidInputProcessor(this.getWindowManager()));
        this.addLifecycleListener(new AndroidApplicationListener());
		game.setGameCycleListener(new AndroidGameListener(this));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}