package se.chalmers.touchdeck.gui;

import java.util.LinkedList;

import se.chalmers.touchdeck.R;
import se.chalmers.touchdeck.gamecontroller.GameController;
import se.chalmers.touchdeck.gamecontroller.Operation;
import se.chalmers.touchdeck.gamecontroller.Operation.Op;
import se.chalmers.touchdeck.models.Card;
import se.chalmers.touchdeck.models.Pile;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
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

	private GuiController				mGuiController;
	private final LinkedList<Button>	mButtons		= new LinkedList<Button>();

	private int							mPileId;
	private Card						mCard;
	private static final int			INDEX_OF_MOVE	= 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pile_view);
		mGuiController = GuiController.getInstance();

		mPileId = getIntent().getExtras().getInt("pileId");

		setupButtons();

		mGuiController.updatePileViewReferences(this, mButtons);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Create the context menus that appear when clicking a card.
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		mCard = mGuiController.getPile(mPileId).getCard(v.getId());
		Log.d("kort", mCard.toString());
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.card_menu, menu);

		// Get the position of the move button and its submenu
		MenuItem item = menu.getItem(INDEX_OF_MOVE);
		SubMenu subMenu = item.getSubMenu();

		// Create a submenu entry for each pile on the table
		for (int i = 0; i < GameController.MAX_NUMBER_OF_PILES; i++) {
			Pile p = mGuiController.getPile(i);
			if (p != null) {
				subMenu.add(Menu.NONE, i, Menu.NONE, p.getName());
			}
		}

	}

	/**
	 * Called when an option in the context menu is chosen
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_flip:
			mGuiController.sendUpdate(new Operation(Op.flip, mPileId, mCard.getRank(), mCard.getSuit()));
			break;
		case R.id.menu_item_move:
			break;
		// Move destination
		default:
			Log.d("aou", mPileId + "");
			mGuiController.sendUpdate(new Operation(Op.move, mPileId, item.getItemId(), mCard.getRank(), mCard.getSuit()));
		}
		return true;
	}

	/**
	 * Creates buttons that represents the cards in the pile.
	 */
	public void setupButtons() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.pileLinear);
		layout.removeAllViewsInLayout();
		layout.invalidate();
		// Get the pile id
		Pile pile = mGuiController.getPile(mPileId);
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
			mButtons.add(b);
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

	public LinkedList<Button> getButtons() {
		return mButtons;

	}
}
