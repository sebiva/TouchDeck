package se.chalmers.touchdeck.gui.dialogs;

import java.util.Observable;
import java.util.Observer;

import android.util.Log;

/**
 * Contains the data from the PileNameDialog, including the id of the button that 
 * created it, which allows the GuiController to listen to the text of the dialog by 
 * listening to this object. 
 * 
 * @author group17
 *
 */
public class DialogText extends Observable{
	private String str = null;
	private int buttonId;
	
	/**
	 * Creates a new Object to handle the data from the dialog from a specific button
	 * 
	 * @param o
	 * @param id
	 */
	public DialogText(Observer o, int id) {
		addObserver(o);
		buttonId = id;
	}
	
	/**
	 * Set the text that is contained
	 * 
	 * @param str	The text to set
	 */
	public void setText(String str) {
		Log.d("dialog", "in DialogText");
		this.str = str;
		setChanged();
		notifyObservers(this);
	}
	/**
	 * Gives the text that was given to the dialog
	 * 
	 * @return		The text from the dialog
	 */
	public String getString() {
		return str;
	}
	
	/**
	 * Gives the id of the button the dialog was created from
	 * 
	 * @return		The id of the button
	 */
	public int getId() {
		return buttonId;
	}
}

