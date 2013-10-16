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

package se.chalmers.touchdeck.game.client.dialogs;

import java.util.Observable;
import java.util.Observer;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.EditText;

/**
 * A dialog shown to the user that lets it choose a name when creating a new pile
 * 
 * @author group17
 */
public class PileNameDialog extends Observable {
	private EditText			mInput;
	private final DialogText	mDialogText;
	private final String		mMessage;
	private final String		mDefaultName;
	private DialogText.Context	mContext	= DialogText.Context.namePile;

	/**
	 * Creates a new Dialog object
	 * 
	 * @param o The object that will listen to the input
	 * @param id The id of the button that was pressed
	 * @param msg The message that will be shown to the user
	 */

	public PileNameDialog(Observer o, int id, String msg, String defaultName, DialogText.Context context) {
		mDialogText = new DialogText(o, id, context);
		mContext = context;
		this.mMessage = msg;
		this.mDefaultName = defaultName;
	}

	/**
	 * Shows the dialog in the specified activity. Prompts the user to enter an name for the pile to be created. If no
	 * name is entered, a default name is given
	 * 
	 * @param act The activity to show the dialog in
	 */
	public void show(Activity act) {
		// A text input for the user to enter the name in
		mInput = new EditText(act);
		AlertDialog.Builder alert = new AlertDialog.Builder(act);
		switch (mContext) {
		case namePile:
			alert.setTitle("Create pile");
			break;
		case renamePile:
			alert.setTitle("Rename pile");
			break;
		default:
			break;
		}

		alert.setMessage(mMessage);

		// Set an EditText view to get user input
		alert.setView(mInput);
		// What to do if the ok-button is pressed
		alert.setPositiveButton(string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// OK
				// Check if a name was entered
				if (mInput.getText().toString().trim().equals("")) {
					// Set the name to a unique default value
					mDialogText.setText(mDefaultName);
					Log.d("joinDialog", "Name is (default) " + mDefaultName);
				} else {
					// Set the name to the entered value
					mDialogText.setText(mInput.getText().toString());
					Log.d("joinDialog", "Name is " + mInput.getText().toString());
				}

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

}
