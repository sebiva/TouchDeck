package se.chalmers.touchdeck.gui;

import java.io.IOException;
import java.io.ObjectOutputStream;
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
import se.chalmers.touchdeck.network.GuiToGameConnection;
import se.chalmers.touchdeck.network.GuiUpdater;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Controls the gui of the game. Singleton class
 * 
 * @author group17
 */
public class GuiController implements Observer {

	private GameState				mGameState;
	private ArrayList<LinearLayout>	mTableViewPileLayouts	= new ArrayList<LinearLayout>();
	private TableView				mTableView;
	private PileView				mPileView;

	private static GuiController	sInstance				= null;

	private final int				mPort					= 4242;
	private String					mIpAddr;
	private Thread					mGuiUpdater;
	private Socket					mGuiToGameSocket;

	public static GuiController getInstance() {
		if (sInstance == null) {
			sInstance = new GuiController();
		}
		return sInstance;
	}

	private GuiController() {
	}

	/**
	 * Sets the GuiToGame Socket
	 * 
	 * @param socket The socket to set
	 */
	public void setSocket(Socket socket) {
		mGuiToGameSocket = socket;
	}

	/**
	 * Sets up the sockets and connections for network play
	 * 
	 * @param ipAddr The ip address of the host.
	 */
	public void setupSockets(String ipAddr) {
		// TODO Inte nödvändig om Connection omstruktureras
		this.mIpAddr = ipAddr;

		mGuiUpdater = new Thread(new GuiUpdater(this, 4243));
		mGuiUpdater.start();
		Thread th = new Thread(new GuiToGameConnection(mIpAddr, mPort, this));
		th.start();

	}

	/**
	 * Sends an operation to the gameController that represents what the user has done
	 * 
	 * @param op The operation that has been made
	 */
	public void sendOperation(Operation op) {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(mGuiToGameSocket.getOutputStream());
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

	// TODO Flytta till tableview
	/**
	 * Called when a button is pressed in the tableview
	 * 
	 * @param v The view (button) that has been pressed
	 */
	public void tableButtonPressed(View v) {
		// Get which button has been pressed
		int id = v.getId();
		Pile p = mGameState.getPiles().get(id);

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
	 * Updates the GuiController with the TableView activity and its buttons
	 * 
	 * @param table The TableView activity
	 * @param layouts Tables buttons
	 */
	public void setTableViewReferences(TableView table, ArrayList<LinearLayout> layouts) {
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
			if (mGameState.getPileNames().contains(dt.getString())) {
				// Prompt the user to try again
				String msg = "Please enter a unique name: ";
				PileNameDialog dialog = new PileNameDialog(this, dt.getId(), msg, mGameState.getDefaultPileName());
				dialog.show(mTableView);
			}
			if (dt.getString().length() > 20) {
				// Prompt the user to try again
				String msg = "Please enter a shorter name: ";
				PileNameDialog dialog = new PileNameDialog(this, dt.getId(), msg, mGameState.getDefaultPileName());
				dialog.show(mTableView);

			} else {
				// Create the pile
				sendOperation(new Operation(Op.create, dt.getId(), dt.getString()));
				updateTableView();
			}

		} else if (obs instanceof GuiUpdater) {
			// Update the state of the game
			GameState gs = (GameState) param;
			setGameState(gs);
			Log.d("network GuC", "New state Received");
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
	 * Updates the pile view
	 */
	public void updatePileView() {
		mPileView.setupButtons();
	}

	/**
	 * Sets the gameState
	 * 
	 * @param gs The gameState
	 */
	public void setGameState(GameState gs) {
		mGameState = gs;
	}

	/**
	 * @return The GameState
	 */
	public GameState getGameState() {
		return mGameState;
	}

}
