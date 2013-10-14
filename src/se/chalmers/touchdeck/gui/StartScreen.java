/**
 Copyright (c) 2013 Karl Engstr�m, Sebastian Ivarsson, Jacob Lundberg, Joakim Karlsson, Alexander Persson and Fredrik Westling
 */

/**
 This file is part of TouchDeck.

 TouchDeck is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 2 of the License, or
 (at your option) any later version.

 TouchDeck is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with TouchDeck.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.chalmers.touchdeck.gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;

import se.chalmers.touchdeck.R;
import se.chalmers.touchdeck.gamecontroller.GameController;
import se.chalmers.touchdeck.gamecontroller.GameState;
import se.chalmers.touchdeck.gui.dialogs.DialogText;
import se.chalmers.touchdeck.gui.dialogs.JoinGameDialog;
import se.chalmers.touchdeck.models.Pile;
import se.chalmers.touchdeck.network.IpFinder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Activity for the startup screen of the application, allows the user to choose game mode. For now, only local play is
 * available
 * 
 * @author group17
 */
public class StartScreen extends Activity implements Observer {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_screen);
	}

	/**
	 * Called when the 'create game' - button is pressed. An intent is created and a TableView activity is started,
	 * along with the gamecontroller as the user is the host of the game.
	 * 
	 * @param v The view (button) that is pressed
	 */
	public void createGame(View v) {
		Intent launchGui = new Intent(this, TableView.class);
		GameController gc = new GameController();
		launchGui.putExtra("state", gc.getGameState());
		launchGui.putExtra("ipAddr", IpFinder.getMyIp());
		startActivity(launchGui);
	}

	/**
	 * Called when the 'join game' - button is pressed. An intent is created and a TableView activity is started. As the
	 * user is not the host, no GameController will be created
	 * 
	 * @param v The view (button) that is pressed
	 */
	public void joinGame(View v) {
		int id = v.getId();
		String msg = "Please enter the host IP: ";
		JoinGameDialog dialog = new JoinGameDialog(this, id, msg);
		dialog.show(this);

	}

	/**
	 * Make this view not open again when pressing back
	 */
	@Override
	public void onStop() {
		super.onStop();
		finish();
	}

	/**
	 * Listens to the dialog for the ip to be entered
	 */
	@Override
	public void update(Observable obs, Object param) {

		if (obs instanceof DialogText) {
			DialogText dt = (DialogText) param;

			if (!JoinGameDialog.validIP(dt.getString())) {
				// Prompt the user to try again
				String msg = "Please enter a valid IP: ";
				JoinGameDialog dialog = new JoinGameDialog(this, dt.getId(), msg);
				dialog.show(this);
			} else {
				Intent launchGui = new Intent(this, TableView.class);
				ArrayList<Pile> emptyPiles = new ArrayList<Pile>();
				int numButtons = getResources().getInteger(R.integer.table_Col_Count)
						* getResources().getInteger(R.integer.table_RowC_ount);
				for (int i = 0; i < numButtons; i++) {
					emptyPiles.add(null);
				}
				launchGui.putExtra("state", new GameState(emptyPiles, new HashSet<String>()));
				launchGui.putExtra("ipAddr", dt.getString());
				startActivity(launchGui);
			}

		}
	}
}
