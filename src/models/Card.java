package models;

import enums.Face;
import enums.Rank;
import enums.Suit;

/**
 * Class modeling a card in a deck
 * 
 * @author fwe, ke
 */
public class Card {
	private final Suit	mSuit;
	private final Rank	mRank;
	private boolean		mFaceUp;

	/**
	 * Constructor for creating a card with face status down
	 * 
	 * @param suit The suit of the card
	 * @param rank The rank of the card
	 */
	public Card(Suit suit, Rank rank) {
		this(suit, rank, false);
	}

	/**
	 * Constructor for creating a card
	 * 
	 * @param suit The suit of the card
	 * @param rank The rank of the card
	 * @param faceUp Indicator if the card should be face up
	 */
	public Card(Suit suit, Rank rank, boolean faceUp) {
		mSuit = suit;
		mRank = rank;
		mFaceUp = faceUp;
	}

	/**
	 * Returns the suit of the card
	 * 
	 * @return The suit of the card
	 */
	public Suit getSuit() {
		return mSuit;
	}

	/**
	 * Returns the rank of the card
	 * 
	 * @return the rank of the card
	 */
	public Rank getRank() {
		return mRank;
	}

	/**
	 * Returns the face state of the card
	 * 
	 * @return The face state
	 */
	public Face getFaceState() {
		if (mFaceUp) {
			return Face.up;
		} else {
			return Face.down;
		}
	}

	/**
	 * Flips the face state of the card
	 */
	public void flipFace() {
		mFaceUp = !mFaceUp;
	}

	/**
	 * Set the face state of the card to up
	 */
	public void setFaceUp() {
		mFaceUp = true;
	}

	/**
	 * Set the face state of the card to down
	 */
	public void setFaceDown() {
		mFaceUp = false;
	}

	/**
	 * Returns the hash code of the card
	 * 
	 * @return The hash code of the card
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mRank == null) ? 0 : mRank.hashCode());
		result = prime * result + ((mSuit == null) ? 0 : mSuit.hashCode());
		return result;
	}

	/**
	 * Checks if the card given as argument is equal to the current card
	 * 
	 * @return True if the two cards are equal, otherwise false
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		return (mRank == other.mRank && mSuit == other.mSuit);

	}

	/**
	 * Returns a string representation of the card
	 */
	@Override
	public String toString() {
		return mRank + " of " + mSuit;
	}
}
