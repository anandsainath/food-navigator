package edu.gatech.hci.foodnavigator.ui;

import edu.gatech.hci.foodnavigator.R;
import edu.gatech.hci.foodnavigator.db.DatabaseHelper;
import edu.gatech.hci.foodnavigator.ui.model.Food;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	private static final String TAG = " ~~~~ MainActivity";
	
	private DatabaseHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		db = DatabaseHelper.getInstance(this.getApplicationContext());
		// db.getWritableDatabase();
		Food food = db.getFood(1, 1);
		Log.d(TAG, food.getName() + " " + food.getDescription());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
