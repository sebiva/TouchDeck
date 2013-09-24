package se.chalmers.touchdeck.test.gui;

import se.chalmers.touchdeck.gui.*;
import se.chalmers.touchdeck.R;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
//import android.view.View;

public class TableViewTest extends ActivityInstrumentationTestCase2<TableView> {

	private TableView view;

	public TableViewTest(){
		super(TableView.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false); //Turn off touch mode in the emulator.
		view = getActivity();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testButtonPressed(){
		TouchUtils.clickView(this, view.findViewById(R.id.tableTable).findViewWithTag("Pile 5"));
	}
}
