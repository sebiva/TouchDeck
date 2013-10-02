package se.chalmers.touchdeck.gui;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import se.chalmers.touchdeck.enums.Rank;
import se.chalmers.touchdeck.enums.Suit;
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
import android.widget.Toast;

/**
 * Controls the gui of the game. Singleton class
 * 
 * @author group17
 */
public class GuiController implements Observer {

	private GameState				mGameState;
	private ArrayList<Button>		mTableViewButtons	= new ArrayList<Button>();
	private TableView				mTableView;

	private PileView				mPileView;

	private static GuiController	sInstance			= null;

	private Socket					mSocket;
	private final int				mPort				= 4242;
	private String					mIpAddr;

	public static GuiController getInstance() {
		if (sInstance == null) {
			sInstance = new GuiController();
		}
		return sInstance;
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
				InetAddress serverAddr = InetAddress.getByName(mIpAddr);
				mSocket = new Socket(serverAddr, mPort);
				Log.d("network GuC", "Client socket setup at " + mPort);
				// Tell the controller you want to connect
				sendUpdate(new Operation(Op.connect));
			} catch (Exception e1) {
				Log.d("network GuC", "Error setting up client" + e1.getMessage());
			}
		}
	}

	/**
	 * Sends an update of what the user has done
	 * 
	 * @param op The operation that has been made
	 */
	public void sendUpdate(Operation op) {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(mSocket.getOutputStream());
			out.writeObject(op);
			Log.d("network GuC", "Operation written into socket");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// TODO flytta till tableview
	/**
	 * Updates the tableview based on the current state of the piles
	 */
	public void updateTableView() {
		int i = 0;
		for (Pile p : mGameState.getPiles()) {
			Button b = mTableViewButtons.get(i);
			if (p == null) {
				b.setBackgroundResource(0);
				b.setText("");
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

	// TODO Flytta till tableview
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
			PileNameDialog dialog = new PileNameDialog(this, id, msg, mGameState.getDefaultPileName());
			dialog.show(mTableView);
		}

	}

	/**
	 * Shuffle the specified pile
	 * 
	 * @param pileId The id of the pile to shuffled
	 */
	public void shufflePile(int pileId) {
		sendUpdate(new Operation(Op.shuffle, pileId));
		Toast.makeText(mTableView, mGameState.getPiles().get(pileId).getName() + " shuffled!", Toast.LENGTH_SHORT).show();
	}

	/**
	 * Delete the specified pile
	 * 
	 * @param pileId The id of the pile to deleted
	 */
	public void deletePile(int pileId) {
		String pileName = mGameState.getPiles().get(pileId).getName();
		sendUpdate(new Operation(Op.delete, pileId));
		Toast.makeText(mTableView, pileName + " deleted!", Toast.LENGTH_SHORT).show();
	}

	/**
	 * Get a pile from an id
	 * 
	 * @param id The id of the pile you want to retrieve
	 * @return The requested pile
	 */
	public Pile getPile(int id) {
		Pile p = mGameState.getPiles().get(id);
		return p;
	}

	/**
	 * Updates the GuiController with the TableView activity and its buttons
	 * 
	 * @param table The TableView activity
	 * @param buttons Tables buttons
	 */
	public void updateTableViewReferences(TableView table, ArrayList<Button> buttons) {
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
		Log.d("network GuC", "in observ");
		if (obs instanceof DialogText) {
			DialogText dt = (DialogText) param;
			// See if the name provided is unique
			if (mGameState.getPileNames().contains(dt.getString())) {
				// Prompt the user to try again
				String msg = "Please enter a unique name: ";
				PileNameDialog dialog = new PileNameDialog(this, dt.getId(), msg, mGameState.getDefaultPileName());
				dialog.show(mTableView);
			} else {
				// Create the pile
				sendUpdate(new Operation(Op.create, dt.getId(), dt.getString()));
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
	 * @param rank The rank of the card that is to be moved
	 * @param suit The suit of the card that is to be moved
	 */
	public void flip(int pilePos, Rank rank, Suit suit) {
		sendUpdate(new Operation(Op.flip, pilePos, rank, suit));
	}

	/**
	 * Moves a card from one pile to another
	 * 
	 * @param pileId The id of the pile to move from
	 * @param destPileId The id of the pile to move to
	 * @param rank The rank of the card that is to be moved
	 * @param suit The suit of the card that is to be moved
	 */
	public void moveCard(int pileId, int destPileId, Rank rank, Suit suit) {
		// gc.moveCard(pileId, cardPos, destPileId);
		sendUpdate(new Operation(Op.move, pileId, destPileId, rank, suit));
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
		mGameState = gs;
	}

	public void setIP(String ipAddr) {
		// TODO Inte nödvändig om Connection omstruktureras
		this.mIpAddr = ipAddr;

		new Updater(this);
		Thread th = new Thread(new Connection());
		th.start();

	}
}
