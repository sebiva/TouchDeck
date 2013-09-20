package game.contoller;

import java.util.ArrayList;

import models.Card;
import models.Pile;
import enums.Rank;
import enums.Suit;

public class GameController {
	private static final int		MAX_NUMBER_OF_PILES	= 21;
	private final ArrayList<Pile>	mTable				= new ArrayList<Pile>(MAX_NUMBER_OF_PILES);

	public GameController() {
	}

	private Pile createDeck() {
		Pile deck = new Pile();
		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				deck.addCard(new Card(suit, rank));
			}
		}
		return deck;
	}
}
