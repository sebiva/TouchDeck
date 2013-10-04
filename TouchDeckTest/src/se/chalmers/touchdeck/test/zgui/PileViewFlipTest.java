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

package se.chalmers.touchdeck.test.zgui;

import java.util.ArrayList;

import se.chalmers.touchdeck.R;
import se.chalmers.touchdeck.gamecontroller.GameController;
import se.chalmers.touchdeck.gamecontroller.GameState;
import se.chalmers.touchdeck.gui.GuiController;
import se.chalmers.touchdeck.gui.PileView;
import se.chalmers.touchdeck.gui.StartScreen;
import se.chalmers.touchdeck.gui.TableView;
import se.chalmers.touchdeck.models.Pile;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.jayway.android.robotium.solo.Condition;
import com.jayway.android.robotium.solo.Solo;

public class PileViewFlipTest extends ActivityInstrumentationTestCase2<StartScreen> {

	private static final String	FLIP_CARD_OPTION	= "Flip card";
	private static final String	MOVE_CARD_OPTION	= "Move card";
	private static final String	DECK				= "deck";

	private StartScreen			startScreen;
	private TableView			tableView;
	private Solo				solo;

	private GuiController		gc;
	private int					pilePos				= GameController.MID_OF_TABLE;
	private PileView			pileView;
	private String				secondPileName;
	private int					secondPilePos;

	public PileViewFlipTest() {
		super(StartScreen.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false); // Turn off touch mode in the emulator.

		// Go through the startscreen
		startScreen = getActivity(); // Get the Activity under test, starting it if necessary.
		Solo solo = new Solo(getInstrumentation(), startScreen);
		solo.clickOnButton(startScreen.getResources().getString(R.string.create_btn));

		// Go through the tableView to a pileView, while creating a new pile
		solo.waitForActivity(TableView.class);
		tableView = (TableView) solo.getCurrentActivity();
		solo = new Solo(getInstrumentation(), tableView);

		secondPileName = "MyPile";
		secondPilePos = pilePos + 6;

		// Create a new pile
		solo.clickOnView(tableView.findViewById(secondPilePos));
		solo.enterText(0, secondPileName);
		solo.clickOnButton("OK");

		solo.clickOnButton(pilePos);

		// Prepare for testing the pileview
		solo.waitForActivity(PileView.class);
		pileView = (PileView) solo.getCurrentActivity();

		gc = GuiController.getInstance();
		pilePos = tableView.getResources().getInteger(R.integer.initial_pile_id);

	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		setActivityInitialTouchMode(true); // Turn on touch mode in the emulator.
	}

	public void testFlipCard() {
		GameState gs = gc.getGameState();
		ArrayList<Pile> list = gs.getPiles();
		Pile deck = list.get(pilePos + 1);
		// Must be here for some reason
		solo = new Solo(getInstrumentation(), pileView);
		String startImage = deck.getCard(0).getImageName();

		// Clicks on the first card in the pile and flips it
		solo.clickOnView(pileView.findViewById(0));
		solo.clickOnText(FLIP_CARD_OPTION);

		waitTime(100);
		// Get the updated pile
		deck = gc.getGameState().getPiles().get(pilePos + 1);
		String firstImage = deck.getCard(0).getImageName();

		assertNotSame(startImage, firstImage);

		// Flips the card again
		solo.clickOnView(pileView.findViewById(0));
		solo.clickOnText(FLIP_CARD_OPTION);

		waitTime(100);
		// Get the updated pile
		deck = gc.getGameState().getPiles().get(pilePos + 1);

		String secondImage = deck.getCard(0).getImageName();
		assertEquals(startImage, secondImage);

		String startImageCard2 = deck.getCard(1).getImageName();
		assertEquals(startImage, startImageCard2);

		// Flips the second card
		solo.clickOnView(pileView.findViewById(1));
		solo.clickOnText(FLIP_CARD_OPTION);

		waitTime(100);
		// Get the updated pile
		deck = gc.getGameState().getPiles().get(pilePos + 1);

		String firstImageCard2 = deck.getCard(1).getImageName();
		assertNotSame(firstImageCard2, startImageCard2);
		assertNotSame(firstImage, firstImageCard2);

	}

	private void clickBack(Solo solo) {
		solo.sendKey(KeyEvent.KEYCODE_BACK);
	}

	private void waitTime(int timeMilli) {
		solo.waitForCondition(new Condition() {

			@Override
			public boolean isSatisfied() {
				return false;
			}
		}, timeMilli);
	}
}
