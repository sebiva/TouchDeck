package se.chalmers.touchdeck.gui;

import java.util.LinkedList;

import se.chalmers.touchdeck.R;
import se.chalmers.touchdeck.models.Card;
import se.chalmers.touchdeck.models.Pile;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Activity for inspecting a pile.
 * 
 * @author Group 17
 */
public class PileView extends Activity implements OnClickListener, OnLongClickListener {

	private GuiController				gc;
	private final LinkedList<Button>	buttons	= new LinkedList<Button>();

	private int							pileId;
	private int							cardId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pile_view);
		gc = GuiController.getInstance();

		pileId = getIntent().getExtras().getInt("pileId");

		setupButtons();

		gc.updatePileViewReferences(this, buttons);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		cardId = v.getId();
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.card_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_flip:
			gc.flip(pileId, cardId);
			break;
		case R.id.menu_item_move:
			break;
		}

		return true;
	}

	/**
	 * Creates buttons that represents the cards in the pile.
	 */
	public void setupButtons() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.pileLinear);
		layout.removeAllViewsInLayout();
		// Get the pile id
		Pile pile = gc.getPile(pileId);
		LinkedList<Card> cards = pile.getCards();
		for (int i = 0; i < pile.getSize(); i++) {

			Button b = new Button(this);
			LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
			bp.setMargins(3, 0, 3, 0);

			b.setId(i);
			b.setTag("Card " + i);

			Card card = cards.get(i);

			int image = getResources().getIdentifier(card.getImageName(), "drawable", getPackageName());
			b.setBackgroundResource(image);

			// Calculate the size of the button
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);

			int y = size.y / 2;
			int x = (int) (y * 0.73);

			b.setHeight(y);
			b.setWidth(x);

			layout.addView(b);
			buttons.add(b);
			b.setOnClickListener(this);
			b.setOnLongClickListener(this);
			registerForContextMenu(b);

			b.setLayoutParams(bp);
		}
	}

	/**
	 * Called when one of the buttons is clicked
	 * 
	 * @param The view(in this case, button) that was clicked
	 */
	@Override
	public void onClick(View v) {
		openContextMenu(v);

	}

	/**
	 * Called when one of the buttons is long clicked
	 */
	@Override
	public boolean onLongClick(View v) {
		this.closeContextMenu();
		return true;
	}
}
