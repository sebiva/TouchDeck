package com.example.fortytwo;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private static int NUM_ROWS = 3;
	private static int NUM_COLUMNS = 7;
	private TableLayout tl;
	private ArrayList<Button> buttons = new ArrayList<Button>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		setupButtons();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	public void setupButtons() {
		tl = (TableLayout) findViewById(R.id.tableTable);
		for (int i=0 ; i<NUM_ROWS ; i++) {
			TableRow tr = new TableRow(this);
			tr.setTag("row" + i);
			
			LayoutParams tp = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT, 1.0f);
			for (int j=0; j<NUM_COLUMNS; j++) {
				Button b = new Button(this);
				LayoutParams bp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT, 1.0f);

				b.setTag("button" + (NUM_COLUMNS*i + j));
				b.setText("Pile " + (NUM_COLUMNS*i + j));
				b.setPadding(5, 5, 5, 5);
				tr.addView(b);
				buttons.add(b);
				b.callOnClick();
				b.setOnClickListener(this);
				b.setTextColor(0xffffffff);
				b.setBackgroundResource(R.drawable.ic_launcher);
				b.setLayoutParams(bp);
			}
			tl.addView(tr);
			tr.setLayoutParams(tp);
		}
		
	}


	@Override
	public void onClick(View v) {
		String tag = (String) v.getTag();
		Toast toast = Toast.makeText(this, tag + " pressed!", Toast.LENGTH_SHORT);
		toast.show();
	}
}
