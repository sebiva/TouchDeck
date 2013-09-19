package models;

import java.util.LinkedList;

import enums.Rank;
import enums.Suit;
import exceptions.CardNotFoundException;

/**
 * Class modeling a pile
 * 
 * @author fwe
 */
public class Pile {
	private final LinkedList<Card>	cards	= new LinkedList<Card>();

	/**
	 * Constructor
	 */
	public Pile() {

	}

	/**
	 * Adds a card to the pile
	 * 
	 * @param card The card to add
	 */
	public void addCard(Card card) {
		cards.add(card);
	}

	/**
	 * Takes one card from the pile
	 * 
	 * @param suit The suit of the card to take
	 * @param rank The rank of the card to take
	 * @return The card
	 * @throws CardNotFoundException If there is no such card in the pile
	 */
	public Card takeCard(Suit suit, Rank rank) throws CardNotFoundException {
		try {
			Card card = cards.remove(cards.indexOf(new Card(suit, rank)));
			return card;
		} catch (IndexOutOfBoundsException e) {
			throw new CardNotFoundException("Couldn't take card from pile: " + suit + "," + rank, e);
		}
	}

	/**
	 * Returns the number of cards in the pile
	 * 
	 * @return The number of cars in the pile
	 */
	public int getSize() {
		return cards.size();
	}

	/**
	 * Returns the cards in the pile
	 * 
	 * @return The cards in the piles
	 */
	public LinkedList<Card> getCards() {
		return cards;
	}
}
