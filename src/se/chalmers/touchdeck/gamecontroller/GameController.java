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
	private final HashSet<String>		mPileNames			= new HashSet<String>();

	private final GameState				mGameState;
	private final int					mPort				= 4243;
	private final LinkedList<Socket>	mSockets			= new LinkedList<Socket>();
	private final LinkedList<Thread>	mThreads			= new LinkedList<Thread>();

	private static int					defaultPileNameNo	= 1;

	/**
	 * Creates a new gamecontroller and sets up a deck.
	 */
	public GameController() {
		// Fill the table empty positions.
		for (int i = 0; i < MAX_NUMBER_OF_PILES; i++) {
			mTable.add(i, null);
		}
		createDeck();
		mGameState = new GameState(mTable, mPileNames, defaultPileNameNo);
		new Listener(this);
	}

	/**
	 * Creates a standard 52-card deck
	 * 
	 * @return A pile containing the deck
	 */
	private Pile createDeck() {
		Pile deck = new Pile("deck"); // TODO Refactor, no hardcoded string
		mPileNames.add("deck");
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
				mSockets.add(new Socket(serverAddr, mPort));
				Log.d("network GaC", "Client socket setup at " + mPort);
				sendUpdatedState();
			} catch (Exception e1) {
				Log.d("network GaC", "Error setting up client" + e1.getMessage());
			}
		}
	}

	/**
	 * Starts the thread for connecting back to the gui
	 */
	public void startConnectThread(Operation op) {
		Thread thread = new Thread(new Connection(op.getIpAddr()));
		thread.start();
		mThreads.add(thread);
	}

	/**
	 * Sends the gamestate to gui
	 */
	public void sendUpdatedState() {
		ObjectOutputStream out = null;
		try {
			for (Socket socket : mSockets) {
				out = new ObjectOutputStream(socket.getOutputStream());
				out.writeObject(mGameState);
				Log.d("network GaC", "State written into socket");
				out.flush();
			}
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
	 * @param op The operation to carry out, including the position of the pile to create and its name
	 */
	public void createPile(Operation op) {
		int pile = op.getPile1();
		String name = op.getName();
		if (mTable.get(pile) != null) {
			return; // There was already a pile there
		}
		// Check that the name is unique and that it
		if (mPileNames.contains(name)) {
			return;
		} else if (name.equals("Pile " + defaultPileNameNo)) {
			defaultPileNameNo++;
			mGameState.setDefaultPileNo(defaultPileNameNo);
		}
		// Make a new Pile object and set() it in the list
		mTable.set(pile, new Pile(name));
		mPileNames.add(name);
		sendUpdatedState();
	}

	/**
	 * Flips a card in a pile
	 * 
	 * @param pilePos The pile index where the card is located in
	 * @param cardPos The card to flips index in the pile
	 */
	public synchronized void flip(Operation op) {
		Card cardToFlip = new Card(op.getSuit(), op.getRank());
		Pile p = mTable.get(op.getPile1());
		for (int i = 0; i < p.getSize(); i++) {
			Card c = p.getCard(i);
			if (c.equals(cardToFlip)) {
				c.flipFace();
				sendUpdatedState();
				return;
			}
		}
	}

	/**
	 * Move a card from one pile to another
	 * 
	 * @param op The operation to carry out, containing the pile to move from, the pile to move to, and the card to move
	 */
	public synchronized void moveCard(Operation op) {
		Pile p = mTable.get(op.getPile1());
		Card cardToMove = new Card(op.getSuit(), op.getRank());
		for (int i = 0; i < p.getSize(); i++) {
			Card c = p.getCard(i);
			if (c.equals(cardToMove)) {
				p.takeCard(i);
				mTable.get(op.getPile2()).addCard(c);
				sendUpdatedState();
				Log.d("aou", cardToMove.toString());
				return;
			}
		}
	}

	/**
	 * Shuffle the specified pile
	 * 
	 * @param op The operation to carry out, containing the pile shuffle
	 */
	public synchronized void shufflePile(Operation op) {
		Pile p = mTable.get(op.getPile1());
		if (p != null) {
			p.shuffle();
			sendUpdatedState();
		}
	}

	/**
	 * Delete the specified pile
	 * 
	 * @param op The operation to carry out, containing the pile to delete
	 */
	public synchronized void deletePile(Operation op) {
		int pile = op.getPile1();
		if (mTable.get(pile) != null && mTable.get(pile).getSize() == 0) {
			mPileNames.remove(mTable.get(pile).getName());
			mTable.set(pile, null);
			sendUpdatedState();
		}
	}

	/**
	 * @return the gamestate
	 */
	public GameState getGameState() {
		return mGameState;
	}
}
