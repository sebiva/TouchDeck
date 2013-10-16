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

/**
 * Contains the data from the PileNameDialog, including the id of the button that created it, which allows the
 * GuiController to listen to the text of the dialog by listening to this object.
 * 
 * @author group17
 */
public class DialogText extends Observable {
	private String		mStr		= null;
	private final int	mButtonId;
	private Context		mContext	= Context.namePile;

	public enum Context {
		namePile, renamePile
	}

	/**
	 * Creates a new Object to handle the data from the dialog from a specific button
	 * 
	 * @param o The observer
	 * @param id The id
	 */
	public DialogText(Observer o, int id) {
		addObserver(o);
		mButtonId = id;
	}

	public DialogText(Observer o, int id, Context context) {
		this(o, id);
		mContext = context;
	}

	/**
	 * Set the text that is contained
	 * 
	 * @param str The text to set
	 */
	public void setText(String str) {
		this.mStr = str;
		setChanged();
		notifyObservers(this);
	}

	/**
	 * Gives the text that was given to the dialog
	 * 
	 * @return The text from the dialog
	 */
	public String getString() {
		return mStr;
	}

	/**
	 * Gives the id of the button the dialog was created from
	 * 
	 * @return The id of the button
	 */
	public int getId() {
		return mButtonId;
	}

	public Context getContext() {
		return mContext;
	}
}
