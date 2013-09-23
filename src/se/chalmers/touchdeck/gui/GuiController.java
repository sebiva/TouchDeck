package se.chalmers.touchdeck.gui;
import java.util.ArrayList;

import se.chalmers.touchdeck.enums.*;
import se.chalmers.touchdeck.exceptions.CardNotFoundException;
import se.chalmers.touchdeck.gamecontroller.*;
import se.chalmers.touchdeck.models.*;

import com.example.fortytwo.R;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Controls the gui of the game
 * 
 * @author group17
 *
 */
public class GuiController {
	private Activity a;
	private ArrayList<Button> buttons;
	private GameController gc;
	private ArrayList<Pile> piles;
	private Toast toast;

	/**
	 * Creates a new guicontroller
	 * @param a			The activity that is associated with the table
	 * @param buttons	A list of all the buttons representing the pile positions
	 */
	public GuiController(Activity a, ArrayList<Button> buttons) {
		this.a = a;
		this.buttons = buttons;
		gc = new GameController();
		updateTable();
	}

	/**
	 * Updates the tableview based on the current state of the piles
	 * 
	 */
	public void updateTable() {
		piles = gc.getPiles();
		int i = 0;
		for (Pile p : piles) {
			Button b = buttons.get(i);
			if (p==null) {
				b.setBackgroundResource(0);
			} else {
				if (p.getSize() > 0) {
					b.setBackgroundResource(R.drawable.b2fv);
				} else {
					b.setBackgroundColor(0xff00dd00);
				}
				
			}
			i++;
		}

	}

	/**
	 * Called when a button is pressed in the tableview
	 * 
	 * @param v		The view (button) that has been pressed
	 */
	public void buttonPressed(View v) {
		// Get which button has been pressed
		int id = v.getId();
		Pile p = piles.get(id);
		String msg;
		if (p != null) {
			Card topCard;
			try {
				// Take out the top of the pile
				topCard = p.takeCard(0);
				Rank r = topCard.getRank();
				Suit s = topCard.getSuit();
				msg = r.toString() + " of " + s.toString();
			} catch (CardNotFoundException e) {
				msg = "Pile empty";
			}
			
		} else {
			msg = "No pile";
		}
		// Remove The previous toast
		if (toast != null) {
			toast.cancel();
		}
		// Update the piles on the table
		updateTable();
		// Show which card was taken from the pile or if it were empty
		toast = Toast.makeText(a, msg, Toast.LENGTH_SHORT);
		toast.show();
	}
}
