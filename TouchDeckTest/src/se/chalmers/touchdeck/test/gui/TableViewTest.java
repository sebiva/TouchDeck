package se.chalmers.touchdeck.test.gui;

import se.chalmers.touchdeck.R;
import se.chalmers.touchdeck.gui.TableView;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;


public class TableViewTest extends ActivityInstrumentationTestCase2<TableView> {

	private TableView view;

	public TableViewTest(){
		super(TableView.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false); // Turn off touch mode in the emulator.
		view = getActivity();				// Get the Activity under test, starting it if necessary.
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		setActivityInitialTouchMode(true); // Turn on touch mode in the emulator.
	}
	
	public void testButtonPressed(){
		TouchUtils.clickView(this, view.findViewById(view.getResources().getInteger(R.integer.initial_pile_id)));
		view = getActivity();
		TouchUtils.clickView(this, view.findViewById(0));
	}
}
