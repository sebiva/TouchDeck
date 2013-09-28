package se.chalmers.touchdeck.gui;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import se.chalmers.touchdeck.gamecontroller.GameState;
import se.chalmers.touchdeck.gui.dialogs.DialogText;
import se.chalmers.touchdeck.gui.dialogs.PileNameDialog;
import se.chalmers.touchdeck.models.Pile;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Controls the gui of the game. Singleton class
 * 
 * @author group17
 */
public class GuiController implements Observer {

	private GameState				mgs;
	private ArrayList<Pile>			piles;
	private ArrayList<Button>		mTableViewButtons	= new ArrayList<Button>();
	private TableView				mTableView;

	private PileView				mPileView;

	private static GuiController	instance			= null;

	private Socket					socket;
	private final Thread			th;

	public static GuiController getInstance() {
		if (instance == null) {
			instance = new GuiController();
		}
		return instance;
	}

	private GuiController() {
		th = new Thread(new Event());
		th.start();
	}

	class Event implements Runnable {

		@Override
		public void run() {

			try {
				InetAddress serverAddr = InetAddress.getByName("127.0.0.1");
				// et.setText("IP: " + adr);
				socket = new Socket(serverAddr, 4242);
				Log.d("network", "Client socket setup at 4242");

			} catch (Exception e1) {
				Log.d("network", "Error setting up client" + e1.getMessage());
			}
		}
	}

	/**
	 * Updates the tableview based on the current state of the piles
	 */
	public void updateTableView() {
		piles = mgs.getPiles();
		int i = 0;
		for (Pile p : piles) {
			Button b = mTableViewButtons.get(i);
			if (p == null) {
				b.setBackgroundResource(0);
			} else {
				b.setText(p.getName());
				if (p.getSize() > 0) {
					// Set the picture of the pile to be the picture of the card on top.
					String imgName = p.getCard(0).getImageName();
					int imgRes = mTableView.getResources().getIdentifier(imgName, "drawable", mTableView.getPackageName());
					b.setBackgroundResource(imgRes);
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
		String str = "hej";
		// PrintWriter out;
		ObjectOutputStream out = null;
		try {
			// out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			out = new ObjectOutputStream(socket.getOutputStream());
			// out.println(str);
			// out.flush();
			out.writeObject(mgs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Get which button has been pressed
		int id = v.getId();
		Pile p = getPile(id);

		// Check if there is a pile on this position
		if (p != null) {
			// Open the pile in the pileview
			Intent pileView = new Intent(mTableView, PileView.class);
			pileView.putExtra("pileId", id);
			mTableView.startActivity(pileView);
		} else {
			// Prompt the user to create a new pile
			String msg = "Please enter a name for the pile: ";
			PileNameDialog dialog = new PileNameDialog(this, id, msg);
			dialog.show(mTableView);
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
	 * @param table The TableView activity
	 * @param buttons Tables buttons
	 * @param gs
	 */
	public void updateTableViewReferences(TableView table, ArrayList<Button> buttons, GameState gs) {
		mgs = gs;
		mTableView = table;
		mTableViewButtons = buttons;
		updateTableView();
	}

	/**
	 * Called when a dialog gets text input. Creates a new pile with the name given in the dialog
	 * 
	 * @param obs The object (Dialogtext) that has been updated
	 * @param param The parameter that is passed along (in the case of the Dialogtext, it's the same object)
	 */
	@Override
	public void update(Observable obs, Object param) {
		if (obs instanceof DialogText) {
			DialogText dt = (DialogText) param;
			// See if the name provided is unique
			// if (gc.checkIfNameExists(dt.getString())) {
			// Prompt the user to try again
			String msg = "Please enter a unique name: ";
			PileNameDialog dialog = new PileNameDialog(this, dt.getId(), msg);
			dialog.show(mTableView);
			// } else {
			// Create the pile
			// gc.createPile(dt.getId(), dt.getString());
			updateTableView();
			// }

		}

	}

	/**
	 * Updates the GuiController with the PileView activity and its buttons
	 * 
	 * @param pile The pile view
	 * @param buttons The pile views buttons
	 */
	public void updatePileViewReferences(PileView pile, LinkedList<Button> buttons) {
		mPileView = pile;
	}

	/**
	 * Flips a card
	 * 
	 * @param pilePos The pile to where the card is located
	 * @param cardPos The position of the card in the pile to flip
	 */
	public void flip(int pilePos, int cardPos) {
		// gc.flip(pilePos, cardPos);
		updatePileView();
		updateTableView();
	}

	/**
	 * Moves a card from one pile to another
	 * 
	 * @param pileId The id of the pile to move from
	 * @param cardPos The position of the card to move
	 * @param destPileId The id of the pile to move to
	 */
	public void moveCard(int pileId, int cardPos, int destPileId) {
		// gc.moveCard(pileId, cardPos, destPileId);
		updatePileView();
		updateTableView();
	}

	/**
	 * Updates the pile view
	 */
	private void updatePileView() {
		mPileView.setupButtons();
	}
}
