package se.chalmers.touchdeck.models;

import java.util.LinkedList;

import se.chalmers.touchdeck.exceptions.CardNotFoundException;

/**
 * Class modeling a pile
 * 
 * @author group17
 */
public class Pile {
	private final LinkedList<Card>	cards	= new LinkedList<Card>();
	private String name;

	/**
	 * Constructor
	 */
	public Pile() {

	}
	
	public Pile(String name) {
		this.name = name;
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
	 * @param pos	The position of the card
	 * @return The card
	 * @throws CardNotFoundException If there is no such card in the pile
	 */
	public Card takeCard(int pos) throws CardNotFoundException {
		try {
			Card card = cards.remove(pos);
			return card;
		} catch (IndexOutOfBoundsException e) {
			throw new CardNotFoundException("Couldn't take card from pile at position: " + pos, e);
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
