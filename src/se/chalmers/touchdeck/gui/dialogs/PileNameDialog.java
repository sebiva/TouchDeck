package se.chalmers.touchdeck.gui.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.text.Editable;
import android.R;
import android.util.Log;

public class PileNameDialog { 
	
	private EditText input;	
	
	public String getName() {
		
		Editable editable = input.getText();
	    return editable == null ? "": editable.toString();
	}
	
	public void show(Activity act) { 
		
		input = new EditText(act);
	    AlertDialog.Builder alert = new AlertDialog.Builder(act);

	    alert.setTitle("Name");
	    alert.setMessage("Choose a name for the pile");

	    // Set an EditText view to get user input
	    alert.setView(input);

	    alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int id) {
	            // OK
	        	Log.d("dialog", "Name is " + input.getText().toString());	        	
	        }
	    });
	    alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int id) {
	            // Cancelled
	        	Log.d("dialog", "You cancelled!");
	        }
	    });
	    
	    alert.show();
		
	}	

}
