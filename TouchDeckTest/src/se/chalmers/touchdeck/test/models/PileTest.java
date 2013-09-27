package se.chalmers.touchdeck.test.models;

import junit.framework.TestCase;
import se.chalmers.touchdeck.enums.Rank;
import se.chalmers.touchdeck.enums.Suit;
import se.chalmers.touchdeck.exceptions.CardNotFoundException;
import se.chalmers.touchdeck.models.Card;
import se.chalmers.touchdeck.models.Pile;

public class PileTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testCreate() {
		Pile p1 = new Pile();
		Pile p2 = new Pile("name");

		assertEquals(null, p1.getName());
		assertEquals("name", p2.getName());
	}

	public void testAddTakeGet() {
		Pile p = new Pile();
		Card c = new Card(Suit.spades, Rank.ace);
		p.addCard(c);
		Card c2;
		try {
			c2 = p.takeCard(0);
			assertEquals(c, c2);
		} catch (CardNotFoundException e) {
			assert (false); // Shouldn't happen
		}

		p.addCard(new Card(Suit.hearts, Rank.ace));
		p.addCard(new Card(Suit.hearts, Rank.king));
		p.addCard(new Card(Suit.hearts, Rank.queen));
		p.addCard(new Card(Suit.hearts, Rank.jack));
		p.addCard(new Card(Suit.hearts, Rank.ten));
		p.addCard(new Card(Suit.hearts, Rank.nine));

		try {
			Card c3 = p.takeCard(0);
			int x = p.getSize();

			assertEquals(5, x);
			assertEquals(new Card(Suit.hearts, Rank.ace), c3);

			Card c4 = p.takeCard(4);
			assertEquals(new Card(Suit.hearts, Rank.nine), c4);
		} catch (CardNotFoundException e) {
			assert (false); // Shouldn't happen
		}
		for (int i = 0; i < 4; i++) {
			try {
				p.takeCard(i);
			} catch (CardNotFoundException e) {
				assert (false); // Shouldn't happen
			}
		}
		try {
			p.takeCard(0);
			assert (false);
		} catch (CardNotFoundException e) {
			assert (true); // The pile should be empty now
		}
	}
}
