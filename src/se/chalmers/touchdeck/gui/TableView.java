package se.chalmers.touchdeck.gui;

import java.io.Serializable;
import java.util.ArrayList;

import se.chalmers.touchdeck.R;
import se.chalmers.touchdeck.gamecontroller.GameController;
import se.chalmers.touchdeck.gamecontroller.GameState;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * The Activity for the table view, contains a grid with pile positions represented as buttons.
 * 
 * @author group17
 */
public class TableView extends Activity implements OnClickListener {

	private TableLayout				tl;

	private final ArrayList<Button>	buttons	= new ArrayList<Button>();
	private GuiController			gc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table_view);
		// Create Buttons in the tableview
		setupButtons();
		Serializable s = getIntent().getExtras().getSerializable("state");
		GameState gs = (GameState) s;
		gc = GuiController.getInstance();
		gc.updateGameState(gs);
		gc.updateTableViewReferences(this, buttons);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Creates the buttons in a grid on the "table". The number of rows and columns are specified by NUM_ROWS and
	 * NUM_COLUMNS.
	 */
	public void setupButtons() {
		tl = (TableLayout) findViewById(R.id.tableTable);
		// Create a number of rows in the table
		for (int i = 0; i < GameController.NUM_ROWS; i++) {
			TableRow tr = new TableRow(this);
			tr.setTag("row" + i);
			// Create the layout parameters for the table row, all rows should be the same size
			LayoutParams tp = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1.0f);
			for (int j = 0; j < GameController.NUM_COLUMNS; j++) {
				Button b = new Button(this);
				// Create the layout parameters for the button, all buttons should be the same size
				LayoutParams bp = new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
				b.setId(GameController.NUM_COLUMNS * i + j);
				b.setTag("Pile " + (GameController.NUM_COLUMNS * i + j));
				// b.setText("Pile " + (num_columns*i + j));
				b.setPadding(5, 5, 5, 5);
				tr.addView(b);
				buttons.add(b);
				// Set this interface as the listener to the button
				b.setOnClickListener(this);
				// Apply the layout parameters
				b.setLayoutParams(bp);
			}
			// Add the row to the table and apply the layout
			tl.addView(tr);
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
		gc.tableButtonPressed(v);
	}
}
