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
import se.chalmers.touchdeck.game.client.StartScreen;
import se.chalmers.touchdeck.game.client.TableView;
import se.chalmers.touchdeck.misc.Constant;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;

public class TableViewTest extends ActivityInstrumentationTestCase2<StartScreen> {

	private TableView	tableView;
	private StartScreen	startScreen;
	private Solo		solo;
	private final int	deckPos	= Constant.MidOfTable;

	public TableViewTest() {
		super(StartScreen.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false); // Turn off touch mode in the emulator.
		startScreen = getActivity(); // Get the Activity under test, starting it if necessary.
		// Go Through the startscreen to get to the tableView
		solo = new Solo(getInstrumentation(), startScreen);
		solo.clickOnButton(startScreen.getResources().getString(R.string.create_btn));
		solo = new Solo(getInstrumentation(), tableView);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		setActivityInitialTouchMode(true); // Turn on touch mode in the emulator.
	}

	public void testCreatePile() {

		// Click on an empty pile position

		solo.clickOnButton(deckPos - 5);
		solo.clickOnButton("OK");

		for (TextView t : solo.getCurrentViews(TextView.class)) {
			Log.e("oaeu", "slkaskes" + t.getText().toString());
		}
		// Click on the new created pile
		String pilename = solo.getText(deckPos * 2 + 3 - 5 * 2 + 1).getText().toString();
		assertEquals("[0]Pile 1", pilename);

		// Click on a different empty pile position
		solo.clickOnButton(deckPos - 4);
		solo.enterText(0, "MyCoolPile");
		solo.clickOnButton("OK");

		String pilename2 = solo.getText(deckPos * 2 + 3 - 4 * 2 + 1).getText().toString();
		assertEquals("[0]MyCoolP", pilename2);
	}
}
