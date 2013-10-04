/**
 Copyright (c) 2013 Karl Engstršm, Sebastian Ivarsson, Jacob Lundberg, Joakim Karlsson, Alexander Persson and Fredrik Westling
 */

/**
 This file is part of TouchDeck.

 TouchDeck is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 2 of the License, or
 (at your option) any later version.

 TouchDeck is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with TouchDeck.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.chalmers.touchdeck.test.models;

import junit.framework.TestCase;
import se.chalmers.touchdeck.enums.Rank;
import se.chalmers.touchdeck.enums.Suit;
import se.chalmers.touchdeck.models.Card;
import se.chalmers.touchdeck.models.Pile;

/**
 * Tests the pile class
 * 
 * @author group17
 */
public class PileTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Test creating piles
	 */
	public void testCreate() {
		Pile p1 = new Pile();
		Pile p2 = new Pile("name");

		assertEquals(null, p1.getName());
		assertEquals("name", p2.getName());
	}

	/**
	 * Test adding, taking and getting cards to a pile
	 */
	public void testAddTakeGet() {
		Pile p = new Pile();
		Card c = new Card(Suit.spades, Rank.ace);
		p.addCard(c);
		Card c2;

		assertTrue(p.getSize() == 1);
		c2 = p.takeCard(0);
		assertEquals(c, c2);
		assertTrue(p.getSize() == 0);

		p.addCard(new Card(Suit.hearts, Rank.ace));
		p.addCard(new Card(Suit.hearts, Rank.king));
		p.addCard(new Card(Suit.hearts, Rank.queen));
		p.addCard(new Card(Suit.hearts, Rank.jack));
		p.addCard(new Card(Suit.hearts, Rank.ten));
		p.addCard(new Card(Suit.hearts, Rank.nine));

		Card c3 = p.takeCard(0);
		int x = p.getSize();

		assertEquals(5, x);
		assertEquals(new Card(Suit.hearts, Rank.nine), c3);

		Card c4 = p.takeCard(4);
		assertEquals(new Card(Suit.hearts, Rank.ace), c4);

		for (int i = 0; i < 4; i++) {
			p.takeCard(0);
		}
		Card c5 = p.takeCard(0);
		assertEquals(null, c5);
	}
}
