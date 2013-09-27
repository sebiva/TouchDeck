package se.chalmers.touchdeck.test.gui;

import se.chalmers.touchdeck.R;
import se.chalmers.touchdeck.gui.TableView;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

public class TableViewTest extends ActivityInstrumentationTestCase2<TableView> {

	private TableView	tableView;

	public TableViewTest() {
		super(TableView.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false); // Turn off touch mode in the emulator.
		tableView = getActivity(); // Get the Activity under test, starting it if necessary.
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		setActivityInitialTouchMode(true); // Turn on touch mode in the emulator.
	}

	public void testButtonPressed() {
		Solo solo = new Solo(getInstrumentation(), tableView);
		solo.clickOnView(tableView.findViewById(tableView.getResources().getInteger(R.integer.initial_pile_id) + 1));
		solo.clickOnButton("OK");
	}

	public void testInspectPile() {
		Solo solo = new Solo(getInstrumentation(), tableView);
		solo.clickOnView(tableView.findViewById(tableView.getResources().getInteger(R.integer.initial_pile_id)));
		solo.clickOnButton(0);
		solo.clickOnText("Flip card");
		solo.clickOnView(solo.getView(R.id.menu_item_move));
	}
}
