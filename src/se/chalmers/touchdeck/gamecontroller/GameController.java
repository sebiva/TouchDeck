package se.chalmers.touchdeck.gamecontroller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import se.chalmers.touchdeck.enums.Rank;
import se.chalmers.touchdeck.enums.Suit;
import se.chalmers.touchdeck.models.Card;
import se.chalmers.touchdeck.models.Pile;
import android.util.Log;

/**
 * Controls the game logic
 * 
 * @author group17
 */
public class GameController {

	// Public constants
	public static final int				MAX_NUMBER_OF_PILES	= 21;
	public static final int				MID_OF_TABLE		= 10;
	public static final int				NUM_ROWS			= 3;
	public static final int				NUM_COLUMNS			= 7;

	private final ArrayList<Pile>		mTable				= new ArrayList<Pile>();
	private final HashSet<String>		pileNames			= new HashSet<String>();
	private final String				DECK_NAME			= "deck";

	private final GameState				gs;
	private final int					port				= 4243;
	private Socket						socket;
	private final LinkedList<Thread>	threads				= new LinkedList<Thread>();

	/**
	 * Creates a new gamecontroller and sets up a deck.
	 */
	public GameController() {
		// Fill the table empty positions.
		for (int i = 0; i < MAX_NUMBER_OF_PILES; i++) {
			mTable.add(i, null);
		}
		createDeck();
		gs = new GameState(mTable);
		new Listener(this);
	}

	/**
	 * Creates a standard 52-card deck
	 * 
	 * @return A pile containing the deck
	 */
	private Pile createDeck() {
		Pile deck = new Pile(DECK_NAME);
		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				deck.addCard(new Card(suit, rank));
			}
		}
		// Put the deck at the middle of the table
		mTable.set(MID_OF_TABLE, deck);
		return deck;
	}

	/**
	 * Sets up a connection back to the gui
	 */
	// TODO Refactor
	private class Connection implements Runnable {
		private final String	ipAddr;

		public Connection(String ipAddr) {
			this.ipAddr = ipAddr;
		}

		@Override
		public void run() {
			try {
				InetAddress serverAddr = InetAddress.getByName(ipAddr);
				socket = new Socket(serverAddr, port);
				Log.d("network GaC", "Client socket setup at " + port);
				sendUpdatedState();
			} catch (Exception e1) {
				Log.d("network GaC", "Error setting up client" + e1.getMessage());
			}
		}
	}

	/**
	 * Starts the thread for connecting back to the gui
	 */
	public void startConnectThread(String ipAddr) {
		Thread thread = new Thread(new Connection(ipAddr));
		thread.start();
		threads.add(thread);
	}

	/**
	 * Sends the gamestate to the gui
	 */
	public void sendUpdatedState() {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(gs);
			Log.d("network GaC", "State written into socket");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns a list of all piles currently in the game
	 * 
	 * @return A list of the piles
	 */
	public ArrayList<Pile> getPiles() {
		return mTable;

	}

	/**
	 * Insert a new, empty pile into the list of piles
	 * 
	 * @param id The table position identifier
	 */
	public void createPile(int id, String name) {
		// Make a new Pile object and set() it in the list
		mTable.set(id, new Pile(name));
		pileNames.add(name);
		sendUpdatedState();
	}

	/**
	 * Check if the name is already taken by another pile
	 * 
	 * @param s The name to check
	 * @return Whether it already exists or not
	 */
	public boolean checkIfNameExists(String s) {
		return pileNames.contains(s);

	}

	/**
	 * Flips a card in a pile
	 * 
	 * @param pilePos The pile index where the card is located in
	 * @param cardPos The card to flips index in the pile
	 */
	public void flip(int pilePos, int cardPos) {
		mTable.get(pilePos).getCard(cardPos).flipFace();
		sendUpdatedState();
	}

	/**
	 * Move a card from one pile to another
	 * 
	 * @param pileId The Pile to send from
	 * @param pos The position in p1 to send from
	 * @param destPileId The pile to send to
	 */
	public void moveCard(int pileId, int pos, int destPileId) {
		Card c = mTable.get(pileId).takeCard(pos);
		if (c != null) {
			mTable.get(destPileId).addCard(c);
			sendUpdatedState();
		}
	}

	/**
	 * @return the gamestate
	 */
	public GameState getGameState() {
		return gs;
	}

}
