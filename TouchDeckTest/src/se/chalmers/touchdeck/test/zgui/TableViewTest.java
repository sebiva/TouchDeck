package se.chalmers.touchdeck.test.zgui;

import se.chalmers.touchdeck.R;
import se.chalmers.touchdeck.gui.StartScreen;
import se.chalmers.touchdeck.gui.TableView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.jayway.android.robotium.solo.Solo;

public class TableViewTest extends ActivityInstrumentationTestCase2<StartScreen> {

	private TableView	tableView;
	private StartScreen	startScreen;
	private Solo		solo;

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
		solo.waitForActivity(TableView.class);
		tableView = (TableView) solo.getCurrentActivity();

		solo = new Solo(getInstrumentation(), tableView);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		setActivityInitialTouchMode(true); // Turn on touch mode in the emulator.
	}

	public void testCreatePile() {

		// Click on an empty pile position
		solo.clickOnView(tableView.findViewById(tableView.getResources().getInteger(R.integer.initial_pile_id) + 1));
		solo.clickOnButton("OK");
		// Click on the new created pile
		solo.clickOnButton("Pile 1");
		// Go back to table view
		clickBack(solo);

		// Click on a different empty pile position
		solo.clickOnView(tableView.findViewById(tableView.getResources().getInteger(R.integer.initial_pile_id) - 1));
		solo.enterText(0, "MyCoolPile");
		solo.clickOnButton("OK");

		solo.clickOnButton("MyCoolPile");
		clickBack(solo);
	}

	private void clickBack(Solo solo) {
		solo.sendKey(KeyEvent.KEYCODE_BACK);
	}
}
