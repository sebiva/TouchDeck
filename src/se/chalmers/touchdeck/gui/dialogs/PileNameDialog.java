package se.chalmers.touchdeck.gui.dialogs;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import java.util.Observable;
import java.util.Observer;

import android.widget.EditText;
import android.R.string;
import android.util.Log;


/**
 * A dialog shown to the user that lets it choose a name when creating a new pile
 * @author group17
 *
 */
public class PileNameDialog extends Observable { 
	private static int num = 1;
	private EditText input;	
	private DialogText dt;
	private String msg;
	
	/**
	 * Creates a new Dialog object
	 * 
	 * @param o			The object that will listen to the input
	 * @param id		The id of the button that was pressed
	 * @param msg		The message that will be shown to the user
	 */
	public PileNameDialog(Observer o, int id, String msg) {
		dt = new DialogText(o, id);
		this.msg = msg;
	}
	
	/**
	 * Shows the dialog in the specified activity. Prompts the user to enter
	 * an name for the pile to be created. If no name is entered, a default name is
	 * given
	 * @param act	The activity to show the dialog in
	 */
	public void show(Activity act) { 
		// A text input for the user to enter the name in
		input = new EditText(act);
	    AlertDialog.Builder alert = new AlertDialog.Builder(act);

	    alert.setTitle("Name");
	    alert.setMessage(msg);

	    // Set an EditText view to get user input
	    alert.setView(input);
	    // What to do if the ok-button is pressed
	    alert.setPositiveButton(string.ok, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int id) {
	            // OK
	        	// Check if a name was entered
	        	if (input.getText().toString().equals("")) {
	        		// Set the name to a unique default value
	        		String defaultName = "Pile " + num++;
	        		dt.setText(defaultName);
	        		Log.d("dialog", "Name is (default) " + defaultName);
	        	} else {
	        		// Set the name to the entered value
	        		dt.setText(input.getText().toString());
	        		Log.d("dialog", "Name is " + input.getText().toString());
	        	}
	        		        	
	        }
	    });
	    // What to do if the cancel-button is pressed
	    alert.setNegativeButton(string.cancel, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int id) {
	            // Cancelled
	        	Log.d("dialog", "You cancelled!");
	        }
	    });
	    // Show the dialog
	    alert.show();
		
	}	
	

}
