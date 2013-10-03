package se.chalmers.touchdeck.test.zgui;

import se.chalmers.touchdeck.R;
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
	private int					pilePos;
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

		solo.clickOnButton(tableView.getResources().getString(R.string.main_deck_name));

		// Prepare for testing the pileview
		solo.waitForActivity(PileView.class);
		pileView = (PileView) solo.getCurrentActivity();

		gc = GuiController.getInstance();
		pilePos = tableView.getResources().getInteger(R.integer.initial_pile_id);

	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		new Solo(getInstrumentation()).finishOpenedActivities();
		setActivityInitialTouchMode(true); // Turn on touch mode in the emulator.
	}

	public void testFlipCard() {
		Pile deck = gc.getGameState().getPiles().get(pilePos);
		// Must be here for some reason
		solo = new Solo(getInstrumentation(), pileView);
		String startImage = deck.getCard(0).getImageName();

		// Clicks on the first card in the pile and flips it
		solo.clickOnView(pileView.findViewById(0));
		solo.clickOnText(FLIP_CARD_OPTION);

		waitTime(100);
		// Get the updated pile
		deck = gc.getGameState().getPiles().get(pilePos);
		String firstImage = deck.getCard(0).getImageName();

		assertNotSame(startImage, firstImage);

		// Flips the card again
		solo.clickOnView(pileView.findViewById(0));
		solo.clickOnText(FLIP_CARD_OPTION);

		waitTime(100);
		// Get the updated pile
		deck = gc.getGameState().getPiles().get(pilePos);

		String secondImage = deck.getCard(0).getImageName();
		assertEquals(startImage, secondImage);

		String startImageCard2 = deck.getCard(1).getImageName();
		assertEquals(startImage, startImageCard2);

		// Flips the second card
		solo.clickOnView(pileView.findViewById(1));
		solo.clickOnText(FLIP_CARD_OPTION);

		waitTime(100);
		// Get the updated pile
		deck = gc.getGameState().getPiles().get(pilePos);

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
