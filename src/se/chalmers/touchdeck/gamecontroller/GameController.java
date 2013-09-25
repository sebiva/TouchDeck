package se.chalmers.touchdeck.gamecontroller;

import java.util.ArrayList;
import java.util.HashSet;

import se.chalmers.touchdeck.enums.Rank;
import se.chalmers.touchdeck.enums.Suit;
import se.chalmers.touchdeck.exceptions.CardNotFoundException;
import se.chalmers.touchdeck.models.Card;
import se.chalmers.touchdeck.models.Pile;

/**
 * Controls the game logic
 * 
 * @author group17
 *
 */
public class GameController {
	// Public constants
	public static final int		MAX_NUMBER_OF_PILES	= 21;
	public static final int		MID_OF_TABLE		= 10;
	public static final int		NUM_ROWS			= 3;
	public static final int		NUM_COLUMNS			= 7;
	
	private final ArrayList<Pile>	mTable				= new ArrayList<Pile>();
	private HashSet<String> pileNames = new HashSet<String>();
	private final String DECK_NAME = "deck";
	
	/**
	 * Creates a new gamecontroller and sets up a deck. 
	 */
	public GameController() {
		// Fill the table empty positions.
		for(int i=0 ; i<MAX_NUMBER_OF_PILES;i++) {
			mTable.add(i,null);
		}
		createDeck();
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
	 * Returns a list of all piles currently in the game
	 * 
	 * @return		A list of the piles
	 */
	public ArrayList<Pile> getPiles () {
		return mTable;
		
	}
	
	/**
	 * Insert a new, empty pile into the list of piles
	 * @param id The table position identifier
	 */
	public void createPile(int id, String name) {
		// Make a new Pile object and set() it in the list
		mTable.set(id, new Pile(name));		
		pileNames.add(name);
	}
	/**
	 * Check if the name is already taken by another pile
	 * 
	 * @param s			The name to check
	 * @return			Whether it already exists or not
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
	}
	
	/**
	 * Move a card from one pile to another
	 * 
	 * @param pileId		The Pile to send from
	 * @param pos			The position in p1 to send from
	 * @param destPileId	The pile to send to
	 */
	public void moveCard(int pileId, int pos, int destPileId) {
		try {
			Card c = mTable.get(pileId).takeCard(pos);
			mTable.get(destPileId).addCard(c);
		} catch (CardNotFoundException e) {
			// Will not happen
		}
		
	}
	
}
