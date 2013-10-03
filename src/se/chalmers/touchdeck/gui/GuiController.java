package se.chalmers.touchdeck.gui;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import se.chalmers.touchdeck.gamecontroller.GameState;
import se.chalmers.touchdeck.gamecontroller.Operation;
import se.chalmers.touchdeck.network.GuiToGameConnection;
import se.chalmers.touchdeck.network.GuiUpdater;
import android.util.Log;

/**
 * Controls the gui of the game. Singleton class
 * 
 * @author group17
 */
public class GuiController implements Observer {

	private GameState				mGameState;
	private TableView				mTableView;
	private PileView				mPileView;

	private static GuiController	sInstance	= null;

	private final int				mPort		= 4242;
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
	 * Sets up the connections for network play
	 * 
	 * @param ipAddr The ip address of the host.
	 */
	public void setupConnections(String ipAddr) {
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
			Log.d("SendOp GuC", "Operation written into socket");
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
			Log.d("network GuC", "in observer - update");
			// Update the state of the game
			GameState gs = (GameState) param;
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

}
