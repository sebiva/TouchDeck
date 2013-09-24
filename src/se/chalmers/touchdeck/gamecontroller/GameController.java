package se.chalmers.touchdeck.gamecontroller;

import java.util.ArrayList;

import se.chalmers.touchdeck.enums.Rank;
import se.chalmers.touchdeck.enums.Suit;
import se.chalmers.touchdeck.models.Card;
import se.chalmers.touchdeck.models.Pile;

/**
 * Controls the game logic
 * 
 * @author group17
 *
 */
public class GameController {
	private static final int		MAX_NUMBER_OF_PILES	= 21;
	private static final int		MID_OF_TABLE		= 10;
	private final ArrayList<Pile>	mTable				= new ArrayList<Pile>();

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
		Pile deck = new Pile();
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
	}
}
