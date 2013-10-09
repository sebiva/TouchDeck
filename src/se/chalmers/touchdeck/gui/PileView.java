/**
 Copyright (c) 2013 Karl Engstr�m, Sebastian Ivarsson, Jacob Lundberg, Joakim Karlsson, Alexander Persson and Fredrik Westling
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

package se.chalmers.touchdeck.gui;

import java.util.HashSet;
import java.util.LinkedList;

import se.chalmers.touchdeck.R;
import se.chalmers.touchdeck.enums.TableState;
import se.chalmers.touchdeck.gamecontroller.Operation;
import se.chalmers.touchdeck.gamecontroller.Operation.Op;
import se.chalmers.touchdeck.models.Card;
import se.chalmers.touchdeck.models.Pile;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Activity for inspecting a pile.
 * 
 * @author Group 17
 */
public class PileView extends Activity implements OnTouchListener {

	private float mDownYPos;
	private float mDownXPos;
	private boolean alreadyClicked;
	private GuiController mGuiController;
	private final LinkedList<Button> mButtons = new LinkedList<Button>();

	private int mPileId;
	private Card mCard;
	private String mIpAddr;
	private final HashSet<Card> mPeekedCards = new HashSet<Card>();
	private int mHeight;
	private int mWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pile_view);
		mGuiController = GuiController.getInstance();
		mPileId = getIntent().getExtras().getInt("pileId");
		mIpAddr = getIntent().getExtras().getString("ipAddr");
		setupButtons();
		mGuiController.setPileView(this);
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
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		Pile currentPile = mGuiController.getGameState().getPiles()
				.get(mPileId);
		mCard = currentPile.getCard(v.getId());
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
		if (mPeekedCards.size() == currentPile.getSize()) {
			menu.findItem(R.id.menu_item_peek_all).setVisible(false);
		}

	}

	/**
	 * Called when an option in the context menu is chosen
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_flip:
			mGuiController
					.sendOperation(new Operation(Op.flip, mPileId, mCard));
			break;
		case R.id.menu_item_move:
			// Launch the TableView in move state
			mGuiController.setTableState(TableState.move);
			mGuiController
					.setMoveOp(new Operation(Op.move, mPileId, -1, mCard));
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
			Pile currentPile = mGuiController.getGameState().getPiles()
					.get(mPileId);
			mPeekedCards.addAll(currentPile.getCards());
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
		LinearLayout layout = (LinearLayout) findViewById(R.id.pileLinear);
		layout.removeAllViewsInLayout();
		layout.invalidate();
		Pile currentPile = mGuiController.getGameState().getPiles()
				.get(mPileId);

		if (currentPile == null
				|| (!currentPile.getOwner().equals("noOwner") && !mIpAddr
						.equals(currentPile.getOwner()))) {
			// If the pile is deleted or protected, the user shouldn't see any
			// cards
			// TODO Fix something nicer
			return;
		}
		LinkedList<Card> cards = currentPile.getCards();

		// Calculate the size of the button
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		mHeight = size.y / 2;
		mWidth = (int) (mHeight * 0.73);

		for (int i = 0; i < currentPile.getSize(); i++) {

			Button b = new Button(this);
			LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
			bp.setMargins(3, 0, 3, 0);

			b.setId(i);
			b.setTag("Card " + i);

			Card card = cards.get(i);

			if (mPeekedCards.contains(card)) {
				int faceUpImage = getResources()
						.getIdentifier(card.getFaceUpImageName(), "drawable",
								getPackageName());
				Drawable peekedCard = getResources().getDrawable(faceUpImage);
				peekedCard.setBounds(0, 0, (int) (mWidth * 0.8),
						(int) (mHeight * 0.8));
				b.setCompoundDrawables(peekedCard, null, null, null);

			}

			int image = getResources().getIdentifier(card.getImageName(),
					"drawable", getPackageName());
			b.setBackgroundResource(image);

			b.setHeight(mHeight);
			b.setWidth(mWidth);

			layout.addView(b);
			mButtons.add(b);
			b.setOnTouchListener(this);
			registerForContextMenu(b);

			b.setLayoutParams(bp);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	/**
	 * Catches what type of gesture that was performed on the card and performs
	 * an operation on the card depending on which gesture was performed.
	 */

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		int maxWidth = 60;
		int minHeight = 20;

		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {

			mDownXPos = event.getX();
			mDownYPos = event.getY();
			return true;

		} else if (action == MotionEvent.ACTION_UP
				|| action == MotionEvent.ACTION_CANCEL) {
			float upX = event.getX();
			float upY = event.getY();

			// Length of swipe in vertical and horizontal axises.
			float deltaY = mDownYPos - upY;
			float deltaX = mDownXPos - upX;

			Pile currentPile = mGuiController.getGameState().getPiles()
					.get(mPileId);
			mCard = currentPile.getCard(v.getId());
			Log.e("Swipe", "dX: " + deltaX + "dY: " + deltaY + "minY: "
					+ minHeight + "maxX: " + maxWidth);
			// What happens when swiping up or down
			if (Math.abs(deltaY) > 20 && Math.abs(deltaX) < maxWidth) {
				Log.e("Swipe", "Swipe down!");
				if (deltaY < 0) {
					if (mPeekedCards.contains(mCard)) {
						mPeekedCards.remove(mCard);
						setupButtons();
					} else {
						mPeekedCards.add(mCard);
						setupButtons();
						return false;
					}
				}
				if (deltaY > 0) {
					Log.e("Swipe", "Swipe up!");
					mGuiController.setTableState(TableState.move);
					mGuiController.setMoveOp(new Operation(Op.move, mPileId,
							-1, mCard));
					Intent table = new Intent(this, TableView.class);
					// Don't start a new tableView, use the one already running.
					table.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(table);
					finish();
					return false;
				}
			}

			// What happens on double tap.
			if (Math.abs(deltaX) < 40) {
				Log.e("Swipe", "Clicked!");
				if (alreadyClicked) {
					mGuiController.sendOperation(new Operation(Op.flip,
							mPileId, mCard));
				}
				alreadyClicked = true;
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						alreadyClicked = false;
					}
				}, 500);

			}
			return false;
		} else if (action == MotionEvent.ACTION_MOVE) {
			return true;
		}
		return false;
	}
}
