/**
 Copyright (c) 2013 Karl Engström, Sebastian Ivarsson, Jacob Lundberg, Joakim Karlsson, Alexander Persson and Fredrik Westling
 */

/**
 This file is part of TouchDeck.

 TouchDeck is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 2 of the License, or
 (at your option) any later version.

 TouchDeck is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with TouchDeck.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.chalmers.touchdeck.game.client;

import java.util.HashSet;
import java.util.LinkedList;

import se.chalmers.touchdeck.R;
import se.chalmers.touchdeck.game.server.Card;
import se.chalmers.touchdeck.game.server.Operation;
import se.chalmers.touchdeck.game.server.Operation.Op;
import se.chalmers.touchdeck.game.server.Pile;
import se.chalmers.touchdeck.misc.Constant;
import se.chalmers.touchdeck.misc.enums.TableState;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
	private final HashSet<Card>			mPeekedCards	= new HashSet<Card>();
	private Pile						mCurrentPile;
	private String						mMyGameIp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pile_view);
		mGuiController = GuiController.getInstance();

		mPileId = getIntent().getExtras().getInt(Constant.IntentPileViewPileId);
		mMyGameIp = getIntent().getExtras().getString(Constant.IntentPileViewIp);
		mCurrentPile = mGuiController.getGameState().getPiles().get(mPileId);

		setupButtons();
		mGuiController.setPileView(this);
	}

	/**
	 * Create the context menus that appear when clicking a card.
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		mCard = mCurrentPile.getCard(v.getId());
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.card_menu, menu);

		// Peek options
		if (mPeekedCards.contains(mCard)) {
			menu.findItem(R.id.menu_item_peek).setVisible(false);
		} else {
			menu.findItem(R.id.menu_item_unpeek).setVisible(false);
		}
		if (mPeekedCards.size() == 0) {
			menu.findItem(R.id.menu_item_unpeek_all).setVisible(false);
		}
		if (mPeekedCards.size() == mCurrentPile.getSize()) {
			menu.findItem(R.id.menu_item_peek_all).setVisible(false);
		}
	}

	/**
	 * Called when an option in the context menu is chosen
	 * 
	 * @param item The item that was chosen
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_flip:
			mGuiController.sendOperation(new Operation(Op.flip, mPileId, mCard));
			break;
		case R.id.menu_item_move:
			// Launch the TableView in move state
			mGuiController.setTableState(TableState.move);
			mGuiController.setMoveOp(new Operation(Op.move, mPileId, -1, mCard));
			Intent table = new Intent(this, TableView.class);
			// Don't start a new tableView, use the one already running.
			table.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(table);
			finish();
			break;

		case R.id.menu_item_peek:
			mPeekedCards.add(mCard);
			setupButtons();
			break;
		case R.id.menu_item_unpeek:
			mPeekedCards.remove(mCard);
			setupButtons();
			break;

		case R.id.menu_item_peek_all:
			mPeekedCards.addAll(mCurrentPile.getCards());
			setupButtons();
			break;

		case R.id.menu_item_unpeek_all:
			mPeekedCards.clear();
			setupButtons();
			break;
		default:
		}
		return true;
	}

	/**
	 * Creates buttons that represents the cards in the pile.
	 */
	public void setupButtons() {
		mCurrentPile = mGuiController.getGameState().getPiles().get(mPileId);
		LinearLayout layout = (LinearLayout) findViewById(R.id.pileLinear);
		layout.removeAllViewsInLayout();
		layout.invalidate();

		TextView pileViewText = (TextView) findViewById(R.id.pileViewText);
		if (mCurrentPile == null) {
			pileViewText.setText("No Pile");
			return;
		} else if (mCurrentPile.getOwner().equals(Constant.PileHasNoOwner)) {
			pileViewText.setText("[" + mCurrentPile.getSize() + "] " + mCurrentPile.getName());
		} else if (mCurrentPile.getOwner().equals(mMyGameIp)) {
			pileViewText.setText("[" + mCurrentPile.getSize() + "] " + mCurrentPile.getName() + " - Protected by you");
		} else {
			pileViewText.setText("[" + mCurrentPile.getSize() + "] " + mCurrentPile.getName() + " - Protected by someone else");
			return;
		}

		LinkedList<Card> cards = mCurrentPile.getCards();
		for (int i = 0; i < mCurrentPile.getSize(); i++) {

			Button btn = new Button(this);
			LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
			btnParams.setMargins(Constant.PileViewCardMargin, 0, Constant.PileViewCardMargin, 0);

			btn.setId(i);
			btn.setTag("Card " + i);

			// Calculate the size of the button
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);

			int y = (int) (size.y / Constant.PileViewCardYFactor);
			int x = (int) (y * Constant.PileViewCardXFactor);

			Card card = cards.get(i);
			// Sets the peek image if the card is peeked
			if (mPeekedCards.contains(card)) {
				int faceUpImage = getResources().getIdentifier(card.getFaceUpImageName(), "drawable", getPackageName());
				Drawable peekedCard = getResources().getDrawable(faceUpImage);
				peekedCard.setBounds(0, 0, (int) (x * Constant.PileViewPeekFactor), (int) (y * Constant.PileViewPeekFactor));
				btn.setCompoundDrawables(peekedCard, null, null, null);

			}

			int image = getResources().getIdentifier(card.getImageName(), "drawable", getPackageName());
			btn.setBackgroundResource(image);

			btn.setHeight(y);
			btn.setWidth(x);

			layout.addView(btn);
			mButtons.add(btn);
			btn.setOnClickListener(this);
			btn.setOnLongClickListener(this);
			registerForContextMenu(btn);

			btn.setLayoutParams(btnParams);
		}
	}

	/**
	 * Called when one of the buttons is clicked
	 * 
	 * @param The view(in this case, button) that was clicked
	 */
	@Override
	public void onClick(View v) {
		try {
			openContextMenu(v);
		} catch (NullPointerException e) {
			Log.e("PileView", "Could not find View");
		}
	}

	/**
	 * Called when one of the buttons is longclicked
	 */
	@Override
	public boolean onLongClick(View v) {
		this.closeContextMenu();
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
