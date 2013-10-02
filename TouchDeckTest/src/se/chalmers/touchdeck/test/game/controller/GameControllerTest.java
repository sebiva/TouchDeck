package se.chalmers.touchdeck.test.game.controller;

import junit.framework.TestCase;
import se.chalmers.touchdeck.enums.Face;
import se.chalmers.touchdeck.gamecontroller.GameController;
import se.chalmers.touchdeck.gamecontroller.GameState;
import se.chalmers.touchdeck.gamecontroller.Operation;
import se.chalmers.touchdeck.gamecontroller.Operation.Op;
import se.chalmers.touchdeck.models.Card;
import se.chalmers.touchdeck.models.Pile;

/**
 * Tests the gamecontroller class
 * 
 * @author group17
 */
public class GameControllerTest extends TestCase {
	private static GameState		gs;
	private final GameController	gc				= new GameController();
	private static final int		MID_OF_TABLE	= GameController.MID_OF_TABLE;

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
