package se.chalmers.touchdeck.test.zgui;

import se.chalmers.touchdeck.R;
import se.chalmers.touchdeck.gui.GuiController;
import se.chalmers.touchdeck.gui.PileView;
import se.chalmers.touchdeck.gui.StartScreen;
import se.chalmers.touchdeck.gui.TableView;
import se.chalmers.touchdeck.models.Card;
import se.chalmers.touchdeck.models.Pile;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.KeyEvent;

import com.jayway.android.robotium.solo.Condition;
import com.jayway.android.robotium.solo.Solo;

public class PileViewMoveTest extends ActivityInstrumentationTestCase2<StartScreen> {

	private static final String	FLIP_CARD_OPTION	= "Flip card";
	private static final String	MOVE_CARD_OPTION	= "Move card";
	private static final String	DECK				= "deck";

	private StartScreen			startScreen;
	private TableView			tableView;
	private Solo				soloStart;
	private Solo				soloTable;
	private Solo				soloPile;

	private GuiController		gc;
	private int					deckPos;
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

		soloTable.clickOnButton(tableView.getResources().getString(R.string.main_deck_name));

		// Prepare for testing the pileview
		soloTable.waitForActivity(PileView.class);
		pileView = (PileView) soloTable.getCurrentActivity();

		gc = GuiController.getInstance();
		deckPos = tableView.getResources().getInteger(R.integer.initial_pile_id);
	}

	public void testMoveCard() {
		Log.e("aue", "hejjejejeo");

		Pile deck = gc.getPile(deckPos);
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
		Pile secondPile = gc.getPile(secondPilePos);
		assertTrue(secondPile.getSize() == 1);
		Card movedCard = secondPile.getCard(0);

		assertEquals(movedCard, cardToBeMoved);

		clickBack(soloStart);
		soloStart.waitForActivity(TableView.class);
		soloTable.clickOnView(tableView.findViewById(deckPos));

		// Update
		secondPile = gc.getPile(secondPilePos);
		deck = gc.getPile(deckPos);
		Card secondCardToBeMoved = deck.getCard(2);

		soloTable.waitForActivity(PileView.class);
		// Flips the third card
		pileView = (PileView) soloTable.getCurrentActivity();
		if (pileView == null) {
			Log.d("ee", "feefefefufaoleuo");
		}
		soloTable.clickOnButton(2);
		soloTable.clickOnText(FLIP_CARD_OPTION);

		// Send it to the new pile
		soloTable.clickOnButton(2);
		soloTable.clickOnText(MOVE_CARD_OPTION);
		soloTable.clickOnText(secondPileName);
		clickBack(soloTable);

		// Update the pile
		secondPile = gc.getPile(secondPilePos);

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

	private void waitTime(Solo solo, int timeMilli) {
		solo.waitForCondition(new Condition() {

			@Override
			public boolean isSatisfied() {
				return false;
			}
		}, timeMilli);
	}
}
