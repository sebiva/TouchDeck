package se.chalmers.touchdeck.gui;

import java.io.Serializable;
import java.util.ArrayList;

import se.chalmers.touchdeck.R;
import se.chalmers.touchdeck.gamecontroller.GameController;
import se.chalmers.touchdeck.gamecontroller.GameState;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The Activity for the table view, contains a grid with pile positions represented as buttons.
 * 
 * @author group17
 */
public class TableView extends Activity implements OnClickListener {

	private TableLayout						mTableLayout;

	private final ArrayList<LinearLayout>	mLayouts	= new ArrayList<LinearLayout>();
	private GuiController					mGuiController;

	private int								mPileId;
	private boolean							mIsBackPressedBefore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table_view);
		// Create Buttons in the tableview
		setupButtons();
		Serializable s = getIntent().getExtras().getSerializable("state");
		String ipAddr = getIntent().getExtras().getString("IPaddr");
		GameState gs = (GameState) s;

		mGuiController = GuiController.getInstance();
		mGuiController.setIP(ipAddr);
		mGuiController.updateGameState(gs);
		mGuiController.updateTableViewReferences(this, mLayouts);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Create the context menus that appear when long-pressing a pile.
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		mPileId = v.getId();
		MenuInflater inflater = getMenuInflater();
		if (mGuiController.getPile(mPileId) != null) {
			if (mGuiController.getPile(mPileId).getSize() > 0) {
				inflater.inflate(R.menu.pile_menu, menu);
			} else {
				inflater.inflate(R.menu.empty_pile_menu, menu);
			}
		}
	}

	/**
	 * Called when an option in the context menu is chosen.
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_shuffle:
			mGuiController.shufflePile(mPileId);
			break;
		case R.id.menu_item_delete:
			mGuiController.deletePile(mPileId);
			break;
		default:
			//
		}
		return true;
	}

	/**
	 * Creates the buttons in a grid on the "table". The number of rows and columns are specified by NUM_ROWS and
	 * NUM_COLUMNS.
	 */
	public void setupButtons() {
		mTableLayout = (TableLayout) findViewById(R.id.tableTable);
		// Create a number of rows in the table
		for (int i = 0; i < GameController.NUM_ROWS; i++) {
			TableRow tr = new TableRow(this);
			tr.setTag("row" + i);
			// Create the layout parameters for the table row, all rows should be the same size
			LayoutParams tp = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1.0f);
			for (int j = 0; j < GameController.NUM_COLUMNS; j++) {

				LinearLayout ll = new LinearLayout(this);
				ll.setOrientation(LinearLayout.VERTICAL);
				ll.setPadding(5, 5, 5, 5);
				LayoutParams lp = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);

				Button b = new Button(this);
				// Create the layout parameters for the button, all buttons should be the same size
				LayoutParams bp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 9);

				b.setId(GameController.NUM_COLUMNS * i + j);
				b.setTag("Pile " + (GameController.NUM_COLUMNS * i + j));
				// b.setText("Pile " + (num_columns*i + j));

				// Set this interface as the listener to the button
				b.setOnClickListener(this);
				registerForContextMenu(b);

				TextView tv = new TextView(this);
				tv.setTextSize(12);

				// Set this interface as the listener to the text view
				tv.setOnClickListener(this);
				registerForContextMenu(tv);
				LayoutParams ba = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 2);

				tv.setId(GameController.NUM_COLUMNS * i + j);
				tv.setTag("Pile " + (GameController.NUM_COLUMNS * i + j));

				ll.addView(b);
				b.setLayoutParams(bp);

				ll.addView(tv);
				tv.setLayoutParams(ba);

				tr.addView(ll);
				ll.setLayoutParams(lp);

				mLayouts.add(ll);
			}
			// Add the row to the table and apply the layout
			mTableLayout.addView(tr);
			tr.setLayoutParams(tp);
		}
	}

	/**
	 * Called when one of the buttons is clicked
	 * 
	 * @param The view(in this case, button) that was clicked
	 */
	@Override
	public void onClick(View v) {
		mGuiController.tableButtonPressed(v);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	/**
	 * Makes the user have to press the back-button twice to exit the app
	 */
	@Override
	public void onBackPressed() {
		if (mIsBackPressedBefore) {
			super.onBackPressed();
			// System.exit(0); // Ajajaj
			finish();
			return;
		}
		this.mIsBackPressedBefore = true;
		Toast.makeText(this, "Click back again to exit", Toast.LENGTH_SHORT).show();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				mIsBackPressedBefore = false;

			}
		}, 2000);
	}
}
