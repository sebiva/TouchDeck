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
/**
 * The main activity of the application, contains a grid with 
 * piles represented as buttons.  
 *
 */
public class MainActivity extends Activity implements OnClickListener {

	private static int NUM_ROWS = 3;
	private static int NUM_COLUMNS = 7;
	private TableLayout tl;
	//Contains all buttons of the table
	private ArrayList<Button> buttons = new ArrayList<Button>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Create Buttons in the tableview 
		setupButtons(NUM_ROWS,NUM_COLUMNS);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Creates the buttons in a grid on the "table". The number of rows and columns
	 * are specified by NUM_ROWS and NUM_COLUMNS. 
	 * 
	 */
	public void setupButtons(int num_rows, int num_columns) {
		tl = (TableLayout) findViewById(R.id.tableTable);
		//Create a number of rows in the table
		for (int i=0 ; i<num_rows ; i++) {
			TableRow tr = new TableRow(this);
			tr.setTag("row" + i);
			//Create the layout parameters for the table row, all rows should be the same size
			LayoutParams tp = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT, 1.0f);
			for (int j=0; j<num_columns; j++) {
				Button b = new Button(this);
				//Create the layout parameters for the button, all buttons should be the same size
				LayoutParams bp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT, 1.0f);

				b.setTag("button" + (num_columns*i + j));
				b.setText("Pile " + (num_columns*i + j));
				b.setPadding(5, 5, 5, 5);
				tr.addView(b);
				buttons.add(b);
				//Set this interface as the listener to the button
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
