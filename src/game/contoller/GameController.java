package game.contoller;

import java.util.ArrayList;

import models.Card;
import models.Pile;
import enums.Rank;
import enums.Suit;

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
}
