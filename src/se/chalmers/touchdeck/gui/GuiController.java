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
import se.chalmers.touchdeck.gamecontroller.Operation;
import se.chalmers.touchdeck.gamecontroller.Operation.Op;
import se.chalmers.touchdeck.gui.dialogs.DialogText;
import se.chalmers.touchdeck.gui.dialogs.PileNameDialog;
import se.chalmers.touchdeck.models.Pile;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Controls the gui of the game. Singleton class
 * 
 * @author group17
 */
public class GuiController implements Observer {

	private GameState				mGs;
	private ArrayList<LinearLayout>	mTableViewPileLayouts	= new ArrayList<LinearLayout>();
	private TableView				mTableView;

	private PileView				mPileView;

	private static GuiController	instance				= null;

	private Socket					socket;
	private final int				port					= 4242;
	private String					ipAddr;

	public static GuiController getInstance() {
		if (instance == null) {
			instance = new GuiController();
		}
		return instance;
	}

	private GuiController() {
	}

	/**
	 * Sets up a connection to the gamecontroller
	 */
	private class Connection implements Runnable {
		@Override
		public void run() {
			try {
				InetAddress serverAddr = InetAddress.getByName(ipAddr);
				socket = new Socket(serverAddr, port);
				Log.d("network GuC", "Client socket setup at " + port);
				// Tell the controller you want to connect
				// TODO pass the ip
				sendUpdate(Op.connect, null, null, null, null);
			} catch (Exception e1) {
				Log.d("network GuC", "Error setting up client" + e1.getMessage());
			}
		}
	}

	/**
	 * Sends an update of what the user has done
	 * 
	 * @param op The operation that has been made
	 * @param pile1 The id of the first pile
	 * @param pile2 The id of the second pile (if op==move)
	 * @param cardPos The position of the card (if op==flip)
	 * @param name The name of the pile (if op==create)
	 */
	public void sendUpdate(Op op, Integer pile1, Integer pile2, Integer cardPos, String name) {
		Operation operation = new Operation(op, pile1, pile2, cardPos, name);
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(operation);
			Log.d("network GuC", "Operation written into socket");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates the tableview based on the current state of the piles
	 */
	public void updateTableView() {
		int i = 0;
		for (Pile p : mGs.getPiles()) {
			LinearLayout ll = mTableViewPileLayouts.get(i);
			Button b = (Button) ll.getChildAt(0);
			TextView tv = (TextView) ll.getChildAt(1);

			if (p == null) {
				b.setBackgroundResource(0);
				b.setBackgroundColor(0);
				tv.setText("");
			} else {

				String name = p.getName();
				if (name.length() > 6) {
					name = name.substring(0, 6);
				}
				tv.setText("[" + p.getSize() + "]" + name);

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
			PileNameDialog dialog = new PileNameDialog(this, id, msg, mGs.getDefaultPileName());
			dialog.show(mTableView);
		}

	}

	/**
	 * Shuffle the specified pile
	 * 
	 * @param pileId The id of the pile to shuffled
	 */
	public void shufflePile(int pileId) {
		sendUpdate(Op.shuffle, pileId, null, null, null);
		Toast.makeText(mTableView, mGs.getPiles().get(pileId).getName() + " shuffled!", Toast.LENGTH_SHORT).show();
	}

	/**
	 * Delete the specified pile
	 * 
	 * @param pileId The id of the pile to deleted
	 */
	public void deletePile(int pileId) {
		String pileName = mGs.getPiles().get(pileId).getName();
		sendUpdate(Op.delete, pileId, null, null, null);
		Toast.makeText(mTableView, pileName + " deleted!", Toast.LENGTH_SHORT).show();
	}

	/**
	 * Get a pile from an id
	 * 
	 * @param id The id of the pile you want to retrieve
	 * @return The requested pile
	 */
	public Pile getPile(int id) {
		Pile p = mGs.getPiles().get(id);
		return p;
	}

	/**
	 * Updates the GuiController with the TableView activity and its buttons
	 * 
	 * @param table The TableView activity
	 * @param layouts Tables buttons
	 */
	public void updateTableViewReferences(TableView table, ArrayList<LinearLayout> layouts) {
		mTableView = table;
		mTableViewPileLayouts = layouts;
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
		Log.d("network GuC", "in observ");
		if (obs instanceof DialogText) {
			DialogText dt = (DialogText) param;
			// See if the name provided is unique
			if (mGs.getPileNames().contains(dt.getString())) {
				// Prompt the user to try again
				String msg = "Please enter a unique name: ";
				PileNameDialog dialog = new PileNameDialog(this, dt.getId(), msg, mGs.getDefaultPileName());
				dialog.show(mTableView);
			}
			if (dt.getString().length() > 20) {
				// Prompt the user to try again
				String msg = "Please enter a shorter name: ";
				PileNameDialog dialog = new PileNameDialog(this, dt.getId(), msg, mGs.getDefaultPileName());
				dialog.show(mTableView);

			} else {
				// Create the pile
				sendUpdate(Op.create, dt.getId(), null, null, dt.getString());
				// gc.createPile(dt.getId(), dt.getString());
				updateTableView();
			}

		} else if (obs instanceof Updater) {
			// Update the state of the game
			GameState gs = (GameState) param;
			updateGameState(gs);
			// Force it to run on the UI-thread
			mTableView.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					updateTableView();
					if (mPileView != null) {
						updatePileView();
					}
				}
			});

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
		sendUpdate(Op.flip, pilePos, null, cardPos, null);
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
		sendUpdate(Op.move, pileId, destPileId, cardPos, null);
	}

	/**
	 * Updates the pile view
	 */
	public void updatePileView() {
		mPileView.setupButtons();
	}

	/**
	 * Updates the gameState
	 * 
	 * @param gs The gameState
	 */
	public void updateGameState(GameState gs) {
		mGs = gs;
	}

	public void setIP(String ipAddr) {
		// TODO Auto-generated method stub
		this.ipAddr = ipAddr;

		new Updater(this);
		Thread th = new Thread(new Connection());
		th.start();

	}
}
