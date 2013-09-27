package se.chalmers.touchdeck.test.game.controller;

import junit.framework.TestCase;
import se.chalmers.touchdeck.enums.Face;
import se.chalmers.touchdeck.gamecontroller.GameController;
import se.chalmers.touchdeck.models.Card;
import se.chalmers.touchdeck.models.Pile;

/**
 * Tests the gamecontroller class
 * 
 * @author group17
 */
public class GameControllerTest extends TestCase {
	private static final GameController	gc				= new GameController();
	private static final int			MID_OF_TABLE	= GameController.MID_OF_TABLE;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Test the creation of the gamecontroller and deck
	 */
	public void testCreate() {
		Pile p = gc.getPiles().get(MID_OF_TABLE);
		assertEquals(52, p.getSize());
	}

	/**
	 * Test flipping a card in a pile
	 */
	public void testFlip() {
		Face f1 = gc.getPiles().get(MID_OF_TABLE).getCard(42).getFaceState();
		gc.flip(MID_OF_TABLE, 42);
		Face f2 = gc.getPiles().get(MID_OF_TABLE).getCard(42).getFaceState();
		assertEquals(false, f1.equals(f2));
		gc.flip(MID_OF_TABLE, 42);
		Face f3 = gc.getPiles().get(MID_OF_TABLE).getCard(42).getFaceState();
		assertEquals(true, f1.equals(f3));
	}

	/**
	 * Test moving a card between piles
	 */
	public void testMove() {
		gc.createPile(5, "NewPile1");
		gc.createPile(15, "NewPile2");
		assertEquals(0, gc.getPiles().get(15).getSize());
		Card c1 = gc.getPiles().get(MID_OF_TABLE).getCard(27);
		gc.moveCard(MID_OF_TABLE, 27, 15);

		assertEquals(1, gc.getPiles().get(15).getSize());
		gc.moveCard(15, 0, 15);
		assertEquals(1, gc.getPiles().get(15).getSize());
		gc.moveCard(15, 0, 5);
		assertEquals(0, gc.getPiles().get(15).getSize());
		assertEquals(1, gc.getPiles().get(5).getSize());

		Card c2 = gc.getPiles().get(5).getCard(0);

		assertEquals(c1, c2);
	}
}
