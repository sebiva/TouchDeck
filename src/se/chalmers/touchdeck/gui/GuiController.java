/**
 Copyright (c) 2013 Karl Engstr�m, Sebastian Ivarsson, Jacob Lundberg, Joakim Karlsson, Alexander Persson and Fredrik Westling
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

package se.chalmers.touchdeck.gui;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import se.chalmers.touchdeck.enums.TableState;
import se.chalmers.touchdeck.gamecontroller.GameState;
import se.chalmers.touchdeck.gamecontroller.Operation;
import se.chalmers.touchdeck.gamecontroller.Operation.Op;
import se.chalmers.touchdeck.network.GuiToGameConnection;
import se.chalmers.touchdeck.network.GuiUpdater;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Controls the gui of the game. Singleton class
 * 
 * @author group17
 */
public class GuiController implements Observer {

	private GameState				mGameState;
	private TableView				mTableView;
	private PileView				mPileView;

	private static GuiController	sInstance			= null;

	private final int				mPort				= 4242;
	private String					mHostIpAddr;
	private String					mMyIpAddr;
	private GuiUpdater				mGuiUpdater;
	private Socket					mGuiToGameSocket;
	private GuiToGameConnection		mGuiToGameConnection;
	private boolean					mTerminating;
	private boolean					mConnectedToGame	= false;

	public static GuiController getInstance() {
		if (sInstance == null) {
			sInstance = new GuiController();
		}
		return sInstance;
	}

	private GuiController() {
		Log.d("GuC", "Creating new GuiController");
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
	 * Sets up the connections for network play
	 * 
	 * @param hostIpAddr The ip address of the host.
	 * @param myGameIpAddr The ip address of the client
	 */
	public void setupConnections(String hostIpAddr, String myGameIpAddr) {
		mHostIpAddr = hostIpAddr;
		mMyIpAddr = myGameIpAddr;
		mGuiUpdater = new GuiUpdater(this, 4243);
		new Thread(mGuiUpdater).start();
		mGuiToGameConnection = new GuiToGameConnection(mHostIpAddr, mPort, this);
		new Thread(mGuiToGameConnection).start();
	}

	/**
	 * Sends an operation to the gameController that represents what the user has done
	 * 
	 * @param op The operation that has been made
	 */
	public void sendOperation(Operation op) {
		if (!mConnectedToGame && !op.getOp().equals(Op.connect)) {
			Toast.makeText(mTableView, "Not connected!", Toast.LENGTH_SHORT).show();
			return;
		}
		ObjectOutputStream out = null;
		op.setIpAddr(mMyIpAddr);
		try {
			out = new ObjectOutputStream(mGuiToGameSocket.getOutputStream());
			out.writeObject(op);
			Log.d("SendOp GuC", "Operation written into socket" + op.getOp().toString());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Called when the GuiUpdater gets an update from the gameController
	 * 
	 * @param obs The GuiUpdater that sent the update
	 * @param param The updated gameState
	 */
	@Override
	public void update(Observable obs, Object param) {
		if (obs instanceof GuiUpdater) {
			mConnectedToGame = true;
			Log.d("network GuC", "in observer - update, connected : " + mConnectedToGame);
			// Update the state of the game
			GameState gs = (GameState) param;
			// If the host has left, close the session
			if (!gs.getHostStillLeft()) {
				mTableView.setTerminate(true);
				terminate();
				Intent i = new Intent(mTableView, StartScreen.class);
				mTerminating = true;
				mTableView.startActivity(i);
				return;
			}
			setGameState(gs);

			Log.d("network GuC", "New state Received");
			// Force it to run on the UI-thread
			mTableView.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mTableView.updateTableView();
					if (mPileView != null) {
						mPileView.setupButtons();
					}
				}
			});
		}
	}

	/**
	 * Updates the GuiController with the TableView activity
	 * 
	 * @param table The TableView activity
	 */
	public void setTableView(TableView table) {
		mTableView = table;
		mTableView.updateTableView();
	}

	/**
	 * Sets the pileView of the GuiController
	 * 
	 * @param pile The pile view
	 */
	public void setPileView(PileView pile) {
		mPileView = pile;
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

	/**
	 * @param state The state to set for the TableView
	 */
	public void setTableState(TableState state) {
		mTableView.setTableState(state);
	}

	/**
	 * @param state The operation to set for the TableView
	 */
	public void setMoveOp(Operation op) {
		mTableView.setmMoveOp(op);
	}

	/**
	 * Terminates the session
	 */
	public void terminate() {

		Log.d("in GuC terminate", "ip : " + mMyIpAddr);
		if (mGuiUpdater != null) {
			mGuiUpdater.end(mMyIpAddr);
			mGuiUpdater = null;
			Log.d("in GuC terminate", "GuiUpdater ended");
		}
		if (!mTerminating && mConnectedToGame) {
			// If it was the user itself initiating the termination, tell the host
			Operation op = new Operation(Op.disconnect);
			op.setIpAddr(mMyIpAddr);
			sendOperation(op);
			Log.d("in GuC terminate", "Disconnect sent");
		}
		if (mGuiToGameConnection != null) {
			mGuiToGameConnection.end();
			mGuiToGameConnection = null;
			Log.d("in GuC terminate", "GuiToGame Ended");
		}

		sInstance = null;
	}

	/**
	 * Remove the socket to the gameController
	 */
	public void removeSocket() {
		mGuiToGameSocket = null;
	}
}
