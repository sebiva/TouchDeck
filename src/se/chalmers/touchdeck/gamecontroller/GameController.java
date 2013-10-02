package se.chalmers.touchdeck.gamecontroller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import se.chalmers.touchdeck.enums.Rank;
import se.chalmers.touchdeck.enums.Suit;
import se.chalmers.touchdeck.models.Card;
import se.chalmers.touchdeck.models.Pile;
import se.chalmers.touchdeck.network.GameListener;
import se.chalmers.touchdeck.network.GameToGuiConnection;
import android.util.Log;

/**
 * Controls the game logic
 * 
 * @author group17
 */
public class GameController {

	// Public constants
	public static final int				NUM_ROWS				= 3;
	public static final int				NUM_COLUMNS				= 8;
	public static final int				MAX_NUMBER_OF_PILES		= NUM_ROWS * NUM_COLUMNS;
	public static final int				MID_OF_TABLE			= MAX_NUMBER_OF_PILES / 2 - 1;
	public static final String			MAIN_DECK_NAME			= "deck";

	private final ArrayList<Pile>		mTable					= new ArrayList<Pile>();
	private final HashSet<String>		mPileNames				= new HashSet<String>();

	private final GameState				mGameState;
	private final int					mPort					= 4243;
	private final LinkedList<Thread>	mGameToGuiThreads		= new LinkedList<Thread>();
	private final LinkedList<Socket>	mAllGameToGuiSockets	= new LinkedList<Socket>();
	private final GameListener			mGameListener;

	private static int					mDefaultPileNameNo		= 1;

	/**
	 * Creates a new gameController and sets up a deck.
	 */
	public GameController() {
		// Fill the table empty positions.
		for (int i = 0; i < MAX_NUMBER_OF_PILES; i++) {
			mTable.add(i, null);
		}
		createDeck();
		mGameState = new GameState(mTable, mPileNames);

		// Start the listener for incoming connections
		mGameListener = new GameListener(this, 4242);
		new Thread(mGameListener).start();
	}

	/**
	 * @param socket The socket to add as the connection to the guiController
	 */
	public void addSocket(Socket socket) {
		Log.d("network GaC", "socket added to list " + socket.getRemoteSocketAddress().toString());
		mAllGameToGuiSockets.add(socket);
	}

	/**
	 * Sends the gameState to all the guis
	 */
	public void sendUpdatedState() {
		ObjectOutputStream out = null;
		try {
			for (Socket socket : mAllGameToGuiSockets) {
				out = new ObjectOutputStream(socket.getOutputStream());
				out.writeObject(mGameState);
				Log.d("sendUpdated GaC", "State written into socket " + socket.getRemoteSocketAddress().toString());
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a standard 52-card deck
	 * 
	 * @return A pile containing the deck
	 */
	private Pile createDeck() {
		Pile deck = new Pile(MAIN_DECK_NAME);
		mPileNames.add(MAIN_DECK_NAME);
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
	 * Performs the given operation and sends out the updated state to all guis
	 * 
	 * @param op
	 */
	public synchronized void performOperation(Operation op) {

		switch (op.getOp()) {

		case move:
			Pile pile = mTable.get(op.getPile1());
			Card cardToMove = op.getCard();
			for (int i = 0; i < pile.getSize(); i++) {
				Card card = pile.getCard(i);
				if (card.equals(cardToMove)) {
					pile.takeCard(i);
					mTable.get(op.getPile2()).addCard(card);
					sendUpdatedState();
					return;
				}
			}
			Log.d("handle GaC", "move");
			break;

		case flip:
			Card cardToFlip = op.getCard();
			Pile currentPile = mTable.get(op.getPile1());
			for (int i = 0; i < currentPile.getSize(); i++) {
				Card c = currentPile.getCard(i);
				if (c.equals(cardToFlip)) {
					c.flipFace();
					sendUpdatedState();
					return;
				}
			}
			Log.d("handle GaC", "flip");
			break;

		case create:
			int pilePos = op.getPile1();
			String name = op.getName();
			if (mTable.get(pilePos) != null) {
				return; // There was already a pile there
			}
			if (mPileNames.contains(name)) {
				return;
			} else if (name.equals("Pile " + mDefaultPileNameNo)) {
				mDefaultPileNameNo++;
				mGameState.setDefaultPileNo(mDefaultPileNameNo);
			}
			mTable.set(pilePos, new Pile(name));
			mPileNames.add(name);
			sendUpdatedState();
			Log.d("handle GaC", "create");
			break;

		case connect:
			Thread thread = new Thread(new GameToGuiConnection(op.getIpAddr(), mPort, this));
			thread.start();
			mGameToGuiThreads.add(thread);
			Log.d("handle GaC", "connected : " + op.getIpAddr());
			break;

		case shuffle:
			Pile pileToShuffle = mTable.get(op.getPile1());
			if (pileToShuffle != null) {
				pileToShuffle.shuffle();
				sendUpdatedState();
			}
			Log.d("handle GaC", "shuffle");
			break;

		case delete:
			int pilePosToDelete = op.getPile1();
			if (mTable.get(pilePosToDelete) != null && mTable.get(pilePosToDelete).getSize() == 0) {
				mPileNames.remove(mTable.get(pilePosToDelete).getName());
				mTable.set(pilePosToDelete, null);
				sendUpdatedState();
			}
			break;
		default:
		}
	}

	/**
	 * @return the gamestate
	 */
	public GameState getGameState() {
		return mGameState;
	}
}
