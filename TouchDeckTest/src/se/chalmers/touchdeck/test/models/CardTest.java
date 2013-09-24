package se.chalmers.touchdeck.test.models;

import se.chalmers.touchdeck.enums.Face;
import se.chalmers.touchdeck.enums.Rank;
import se.chalmers.touchdeck.enums.Suit;
import se.chalmers.touchdeck.models.Card;
import junit.framework.TestCase;

/**
 * Tests the methods in the Card class
 * 
 * @author group17
 */

public class CardTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}	

/**
 * 
 * Tests the Equals method.
 * 
 */
	public void testEquals(){
		Card cardOne;
		Card cardTwo;
		
		cardOne = new Card(Suit.hearts, Rank.five);			// Create Five of Hearts with default face state.
		cardTwo = new Card(Suit.hearts, Rank.five);			// Create Five of Hearts with default face state.
		assertTrue(cardOne.equals(cardTwo));				// Should have identical suit+rank. Face State shouldn't matter. I.e. they should be equal. 
		
		cardTwo = new Card(Suit.hearts, Rank.four);
		assertFalse(cardOne.equals(cardTwo));
		
		cardOne = new Card(Suit.hearts, Rank.five, true); 	// Create Five of Hearts with face up.
		cardTwo = new Card(Suit.hearts, Rank.five, false); 	// Create Five of Hearts with face down.
		assertTrue(cardOne.equals(cardTwo)); 				// Face State shouldn't matter, i.e. they should be equal.
		
		cardTwo = cardOne;
		assertTrue(cardOne.equals(cardTwo));				// Same object should = equal cards.
	}

	/**
	 * 
	 * Tests the constructors.
	 * 
	 */
	public void testConstructors(){
		Card card;
		
		card = new Card(Suit.clubs, Rank.ace);				// Create Ace of Clubs with default face state (face down).
		assertEquals(Suit.clubs, card.getSuit());
		assertEquals(Rank.ace, card.getRank());
		assertEquals(Face.down, card.getFaceState());
		
		card = new Card(Suit.diamonds, Rank.eight, false);	// Create Eight of Diamonds with face down.
		assertEquals(Suit.diamonds, card.getSuit());
		assertEquals(Rank.eight, card.getRank());
		assertEquals(Face.down, card.getFaceState());
		
		card = new Card(Suit.clubs, Rank.nine, true);		// Create Nine of Clubs with face up.
		assertEquals(Suit.clubs, card.getSuit());
		assertEquals(Rank.nine, card.getRank());
		assertEquals(Face.up, card.getFaceState());
	}
	
	/**
	 * 
	 * Tests flipping a card (toggling face states, face down => face up and vice versa).
	 * 
	 */
	public void testFlipFace(){
		Card card;
		
		card = new Card(Suit.clubs, Rank.ace, false); 		// Starting with face down.
		assertEquals(Face.down, card.getFaceState());
		card.flipFace();									// Flip so it card is face up
		assertEquals(Face.up, card.getFaceState());
		card.flipFace();									// Flip so it card is face down
		assertEquals(Face.down, card.getFaceState());
		card.flipFace();									// Flip so it card is face up
		card.flipFace();									// Flip so it card is face down
		assertEquals(Face.down, card.getFaceState());
		
		card = new Card(Suit.clubs, Rank.ace, true); 		// Starting with face up.
		assertEquals(Face.up, card.getFaceState());
		card.flipFace();									// Flip so it card is face down
		assertEquals(Face.down, card.getFaceState());
		card.flipFace();									// Flip so it card is face up
		assertEquals(Face.up, card.getFaceState());
		card.flipFace();									// Flip so it card is face down
		card.flipFace();									// Flip so it card is face up
		assertEquals(Face.up, card.getFaceState());
	}
	
	
	/**
	 * 
	 * Tests setting face state to Face.up.
	 * 
	 */
	public void testSetFaceUp(){
		Card card;
		
		card = new Card(Suit.clubs, Rank.ace, false); 		// Starting with face down.
		card.setFaceUp();
		assertEquals(Face.up, card.getFaceState());
		
		card = new Card(Suit.clubs, Rank.ace, true); 		// Starting with face already up.
		card.setFaceUp();
		assertEquals(Face.up, card.getFaceState());
	}
	
	
	/**
	 * 
	 * Tests setting face state to Face.down.
	 * 
	 */
	public void testSetFaceDown(){
		Card card;
		
		card = new Card(Suit.clubs, Rank.ace, true); 		// Starting with face up
		card.setFaceDown();
		assertEquals(Face.down, card.getFaceState());
		
		card = new Card(Suit.clubs, Rank.ace, false); 		// Starting with face already down
		card.setFaceDown();
		assertEquals(Face.down, card.getFaceState());
	}
	
	
	/**
	 * 
	 * Tests getting the face state.
	 * 
	 */
	public void testGetFaceState(){
		Card card;
		
		card = new Card(Suit.clubs, Rank.ace, true); 		// Starting with face up
		assertEquals(Face.up, card.getFaceState());
		
		card = new Card(Suit.clubs, Rank.ace, false); 		// Starting with face down.
		assertEquals(Face.down, card.getFaceState());
		
		card.setFaceUp();
		assertEquals(Face.up, card.getFaceState());

		card.setFaceDown();
		assertEquals(Face.down, card.getFaceState());
	}
}
