package se.chalmers.touchdeck.gui;

import java.util.ArrayList;
import java.util.LinkedList;

import se.chalmers.touchdeck.R;
import se.chalmers.touchdeck.enums.Face;
import se.chalmers.touchdeck.models.Card;
import se.chalmers.touchdeck.models.Pile;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Activity for inspecting a pile.
 * 
 * @author Group 17
 */
public class PileView extends Activity implements OnClickListener {

	private GuiController			gc;
	private final ArrayList<Button>	buttons	= new ArrayList<Button>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pile_view);
		gc = GuiController.getInstance();
		setupButtons();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Creates buttons that represents the cards in the pile.
	 */
	public void setupButtons() {
		// Get the pile id
		int pileId = getIntent().getExtras().getInt("pileId");
		Pile pile = gc.getPile(pileId);
		LinkedList<Card> cards = pile.getCards();
		LinearLayout layout = (LinearLayout) findViewById(R.id.pileLinear);
		for (int i = 0; i < pile.getSize(); i++) {

			Button b = new Button(this);
			LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
			bp.setMargins(3, 0, 3, 0);

			b.setId(i);
			b.setTag("Card " + i);

			Card card = cards.get(i);

			int image;

			// Setting the buttons picture based on the face up/down state of the card
			if (card.getFaceState() == Face.down) {
				image = R.drawable.b2fv;
			} else {
				String pic = card.getSuit() + "_" + card.getRank();
				image = getResources().getIdentifier(pic, "drawable", getPackageName());
			}

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
		// gc.buttonPressed(v);
	}
}
