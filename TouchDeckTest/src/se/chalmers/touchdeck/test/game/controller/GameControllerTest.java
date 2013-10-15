/**
 Copyright (c) 2013 Karl Engstr�m, Sebastian Ivarsson, Jacob Lundberg, Joakim Karlsson, Alexander Persson and Fredrik Westling
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

package se.chalmers.touchdeck.test.game.controller;

import junit.framework.TestCase;
import se.chalmers.touchdeck.enums.Face;
import se.chalmers.touchdeck.gamecontroller.GameController;
import se.chalmers.touchdeck.gamecontroller.GameState;
import se.chalmers.touchdeck.gamecontroller.Operation;
import se.chalmers.touchdeck.gamecontroller.Operation.Op;
import se.chalmers.touchdeck.models.Card;
import se.chalmers.touchdeck.models.Constant;
import se.chalmers.touchdeck.models.Pile;

/**
 * Tests the gamecontroller class
 * 
 * @author group17
 */
public class GameControllerTest extends TestCase {
	private static GameState		gs;
	private final GameController	gc				= new GameController();
	private static final int		MID_OF_TABLE	= Constant.MidOfTable;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gs = gc.getGameState();
	}

	/**
	 * Test the creation of the gamecontroller and deck
	 */
	public void testCreate() {
		Pile p = gs.getPiles().get(MID_OF_TABLE);
		assertEquals(52, p.getSize());
	}

	/**
	 * Test flipping a card in a pile
	 */
	public void testFlip() {
		Card c1 = gs.getPiles().get(MID_OF_TABLE).getCard(42);
		Face f1 = c1.getFaceState();
		gc.performOperation(new Operation(Op.flip, MID_OF_TABLE, c1));
		Card c2 = gs.getPiles().get(MID_OF_TABLE).getCard(42);
		Face f2 = c2.getFaceState();
		assertEquals(false, f1.equals(f2));
		gc.performOperation(new Operation(Op.flip, MID_OF_TABLE, c1));
		Face f3 = gs.getPiles().get(MID_OF_TABLE).getCard(42).getFaceState();
		assertEquals(true, f1.equals(f3));
	}

	/**
	 * Test moving a card between piles
	 */
	public void testMove() {
		gc.performOperation(new Operation(Op.create, 5, "NewPile1"));
		gc.performOperation(new Operation(Op.create, 15, "NewPile2"));
		assertEquals(0, gs.getPiles().get(15).getSize());
		Card c1 = gs.getPiles().get(MID_OF_TABLE).getCard(27);

		gc.performOperation(new Operation(Op.move, MID_OF_TABLE, 15, c1));

		assertEquals(1, gs.getPiles().get(15).getSize());
		gc.performOperation(new Operation(Op.move, 15, 15, c1));
		assertEquals(1, gs.getPiles().get(15).getSize());

		gc.performOperation(new Operation(Op.move, 15, 5, c1));
		assertEquals(0, gs.getPiles().get(15).getSize());
		assertEquals(1, gs.getPiles().get(5).getSize());

		Card c2 = gs.getPiles().get(5).getCard(0);

		assertEquals(c1, c2);
	}
}
