package se.chalmers.touchdeck.gui;

import java.util.ArrayList;

import se.chalmers.touchdeck.R;
import se.chalmers.touchdeck.gamecontroller.GameController;
import se.chalmers.touchdeck.models.Pile;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

/**
 * Controls the gui of the game. Singleton class
 * 
 * @author group17
 */
public class GuiController {

	private final GameController	gc;
	private ArrayList<Pile>			piles;
	private ArrayList<Button>		mTableViewButtons	= new ArrayList<Button>();
	private Activity				mTableView;

	private static GuiController	instance			= null;

	public static GuiController getInstance() {
		if (instance == null) {
			instance = new GuiController();
		}
		return instance;
	}

	private GuiController() {
		gc = new GameController();
	}

	/**
	 * Updates the tableview based on the current state of the piles
	 */
	public void updateTable() {
		piles = gc.getPiles();
		int i = 0;
		for (Pile p : piles) {
			Button b = mTableViewButtons.get(i);
			if (p == null) {
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
	 * @param v The view (button) that has been pressed
	 */
	public void tableButtonPressed(View v) {
		// Get which button has been pressed
		int id = v.getId();
		Pile p = getPile(id);

		if (p != null) {
			Intent pileView = new Intent(mTableView, PileView.class);
			pileView.putExtra("pileId", id);
			mTableView.startActivity(pileView);
		} else {
			// No pile
		}

	}

	/**
	 * Get a pile from an id
	 * 
	 * @param id The id of the pile you want to retrieve
	 * @return The requested pile
	 */
	public Pile getPile(int id) {
		Pile p = piles.get(id);
		return p;
	}

	/**
	 * Updates the GuiController with the TableView activity and its buttons
	 * 
	 * @param act The TableView activity
	 * @param buttons Tables buttons
	 */
	public void updateTableViewReferences(Activity act, ArrayList<Button> buttons) {
		mTableView = act;
		mTableViewButtons = buttons;
		updateTable();
	}
}
