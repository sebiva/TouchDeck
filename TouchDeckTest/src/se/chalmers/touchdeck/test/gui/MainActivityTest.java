//package se.chalmers.touchdeck.test.gui;
//
//import se.chalmers.touchdeck.StartScreen;
//import se.chalmers.touchdeck.R;
//
//import android.test.ActivityInstrumentationTestCase2;
//import android.test.TouchUtils;
//
//public class MainActivityTest extends
//		ActivityInstrumentationTestCase2<MainActivity> {
//	private MainActivity deck;
//
//	public MainActivityTest(){
//		super(MainActivity.class);
//	}
//	
//	protected void setUp() throws Exception {
//		super.setUp();
//		setActivityInitialTouchMode(false); //Turn off touch mode in the emulator.
//		deck = getActivity();
//	}
//	
//	public void testButtonPressed(){
//		TouchUtils.clickView(this, deck.findViewById(R.id.tableTable));
//	}
//}
