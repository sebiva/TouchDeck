/**
 Copyright (c) 2013 Karl Engström, Sebastian Ivarsson, Jacob Lundberg, Joakim Karlsson, Alexander Persson and Fredrik Westling
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

package se.chalmers.touchdeck.gui.dialogs;

import java.util.Observable;
import java.util.Observer;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.EditText;

/**
 * A dialog shown to the user that lets it enter the ip address of the game to join
 * 
 * @author group17
 */
public class JoinGameDialog extends Observable {
	private EditText			input;
	private final DialogText	dt;
	private final String		msg;

	/**
	 * Creates a new Dialog object
	 * 
	 * @param o The object that will listen to the input
	 * @param id The id of the button that was pressed
	 * @param msg The message that will be shown to the user
	 */
	public JoinGameDialog(Observer o, int id, String msg) {
		dt = new DialogText(o, id);
		this.msg = msg;
	}

	/**
	 * Shows the dialog in the specified activity. Prompts the user to enter the ip address of the host
	 * 
	 * @param act The activity to show the dialog in
	 */
	public void show(Activity act) {
		// A text input for the user to enter the name in
		input = new EditText(act);
		input.setText("192.168.0.1");
		AlertDialog.Builder alert = new AlertDialog.Builder(act);

		alert.setTitle("Join game");
		alert.setMessage(msg);

		// Set an EditText view to get user input
		alert.setView(input);
		// What to do if the ok-button is pressed
		alert.setPositiveButton(string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {

				// Set the IP to the entered value
				dt.setText(input.getText().toString());
				Log.d("joinDialog", "IP is " + input.getText().toString());

			}
		});
		// What to do if the cancel-button is pressed
		alert.setNegativeButton(string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// Cancelled
				Log.d("joinDialog", "You cancelled!");
			}
		});
		// Show the dialog
		alert.show();

	}

	/**
	 * Checks if the given string is a valid ip address
	 * 
	 * @param ip The string to check
	 * @return True if vaild
	 */
	public static boolean validIP(String ip) {
		try {
			if (ip == null || ip.isEmpty()) {
				return false;
			}

			String[] parts = ip.split("\\.");
			if (parts.length != 4) {
				return false;
			}

			for (String s : parts) {
				int i = Integer.parseInt(s);
				if ((i < 0) || (i > 255)) {
					return false;
				}
			}

			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

}
