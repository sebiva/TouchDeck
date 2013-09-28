package se.chalmers.touchdeck.gui;

import se.chalmers.touchdeck.R;
import se.chalmers.touchdeck.gamecontroller.GameController;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

/**
 * Activity for the startup screen of the application, allows the user to choose game mode. For now, only local play is
 * available
 * 
 * @author group17
 */
public class StartScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_screen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Called when the 'start local game' - button is pressed. An intent is created and a TableView activity is started,
	 * along with the gamecontroller as the user is the hosts of their own game.
	 * 
	 * @param v The view (button) that is pressed
	 */
	public void startLocal(View v) {
		Intent localGame = new Intent(this, TableView.class);
		GameController gc = new GameController();
		localGame.putExtra("state", gc.getGameState());
		startActivity(localGame);
	}
}
