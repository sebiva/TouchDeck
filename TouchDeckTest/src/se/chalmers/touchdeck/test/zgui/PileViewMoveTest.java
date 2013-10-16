/**
 Copyright (c) 2013 Karl Engström, Sebastian Ivarsson, Jacob Lundberg, Joakim Karlsson, Alexander Persson and Fredrik Westling
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

package se.chalmers.touchdeck.test.zgui;

import se.chalmers.touchdeck.R;
import se.chalmers.touchdeck.game.client.GuiController;
import se.chalmers.touchdeck.game.client.PileView;
import se.chalmers.touchdeck.game.client.StartScreen;
import se.chalmers.touchdeck.game.client.TableView;
import se.chalmers.touchdeck.game.server.Card;
import se.chalmers.touchdeck.game.server.Pile;
import se.chalmers.touchdeck.misc.Constant;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.jayway.android.robotium.solo.Solo;

public class PileViewMoveTest extends ActivityInstrumentationTestCase2<StartScreen> {

	private static final String	FLIP_CARD_OPTION	= "Flip card";
	private static final String	MOVE_CARD_OPTION	= "Move card";

	private StartScreen			startScreen;
	private TableView			tableView;
	private Solo				soloStart;
	private Solo				soloTable;
	private Solo				soloPile;

	private GuiController		gc;
	private int					deckPos				= Constant.MidOfTable;
	private PileView			pileView;
	private String				secondPileName;
	private int					secondPilePos;

	public PileViewMoveTest() {
		super(StartScreen.class);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false); // Turn off touch mode in the emulator.

		// Go through the startscreen
		startScreen = getActivity(); // Get the Activity under test, starting it if necessary.
		soloStart = new Solo(getInstrumentation(), startScreen);

		soloStart.clickOnButton(startScreen.getResources().getString(R.string.create_btn));

		// Go through the tableView to a pileView, while creating a new pile
		soloStart.waitForActivity(TableView.class);
		tableView = (TableView) soloStart.getCurrentActivity();
		soloTable = new Solo(getInstrumentation(), tableView);

		secondPileName = "MyPile";
		secondPilePos = deckPos + 7;

		// Create a new pile
		soloTable.clickOnView(tableView.findViewById(secondPilePos));
		soloTable.enterText(0, secondPileName);
		soloTable.clickOnButton("OK");

		soloTable.clickOnButton(deckPos);

		// Prepare for testing the pileview
		soloTable.waitForActivity(PileView.class);
		pileView = (PileView) soloTable.getCurrentActivity();

		gc = GuiController.getInstance();
		deckPos = Constant.MidOfTable;
	}

	public void testMoveCard() {

		Pile deck = gc.getGameState().getPiles().get(deckPos + 1);
		// Must be here for some reason
		soloPile = new Solo(getInstrumentation(), pileView);

		Card cardToBeMoved = deck.getCard(3);

		// Send the fourth card to the new pile
		soloPile.clickOnView(pileView.findViewById(3));
		soloPile.clickOnText(MOVE_CARD_OPTION);
		soloPile.clickOnText(secondPileName);

		clickBack(soloPile);
		soloTable.clickOnView(tableView.findViewById(secondPilePos));

		// Update
		Pile secondPile = gc.getGameState().getPiles().get(secondPilePos);
		assertTrue(secondPile.getSize() == 1);
		Card movedCard = secondPile.getCard(0);

		assertEquals(movedCard, cardToBeMoved);

		clickBack(soloPile);

		// Update
		secondPile = gc.getGameState().getPiles().get(secondPilePos);
		deck = gc.getGameState().getPiles().get(deckPos + 1);
		Card secondCardToBeMoved = deck.getCard(2);

		soloTable.clickOnButton(deckPos + 1);

		// Flips the third card
		soloTable.clickOnButton(2);
		soloTable.clickOnText(FLIP_CARD_OPTION);

		// Send it to the new pile
		soloTable.clickOnButton(2);
		soloTable.clickOnText(MOVE_CARD_OPTION);
		soloTable.clickOnText(secondPileName);
		clickBack(soloTable);

		// Update the pile
		secondPile = gc.getGameState().getPiles().get(secondPilePos);

		soloTable.clickOnView(tableView.findViewById(secondPilePos));
		Card secondMovedCard = secondPile.getCard(0);
		assertEquals(secondMovedCard, secondCardToBeMoved);

		clickBack(soloPile);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		setActivityInitialTouchMode(true); // Turn on touch mode in the emulator.
	}

	private void clickBack(Solo solo) {
		solo.sendKey(KeyEvent.KEYCODE_BACK);
	}
}
