package edu.gatech.hci.foodnavigator.search;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

import edu.gatech.hci.foodnavigator.R;
import edu.gatech.hci.foodnavigator.utilities.Functions;

public class Search extends SherlockActivity {
	private TextView mTextView;
	private ListView mListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_home);
		mTextView = (TextView) findViewById(R.id.TV_PageHeader);
		mListView = (ListView) findViewById(R.id.list);
		mListView.setDivider(null);
		mListView.setDividerHeight(0);
		mListView.setCacheColorHint(Color.TRANSPARENT);
		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// Because this activity has set launchMode="singleTop", the system
		// calls this method to deliver the intent if this activity is currently
		// the foreground activity when invoked again (when the user executes a
		// search from this activity, we don't create
		// a new instance of this activity, so the system delivers the search
		// intent here)
		handleIntent(intent);
	}

	private void goToCard(Uri uri) {
		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(uri, null, null, null, null);
		if (cursor == null) {
			finish();
		} else {
			cursor.moveToFirst();
			Functions.d("Go to the innermost activity here..");
			Toast.makeText(getApplicationContext(), cursor.getColumnIndexOrThrow(BaseColumns._ID), Toast.LENGTH_LONG)
					.show();
			// int index = cursor.getColumnIndexOrThrow(BaseColumns._ID);
			// Intent I = new Intent(getApplicationContext(), CardHome.class);
			// startActivity(I);
		}
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			// handles a click on a search suggestion; launches activity to show
			// word
			goToCard(intent.getData());
		} else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			// handles a search query
			showResults(intent.getStringExtra(SearchManager.QUERY));
		}
	}

	/**
	 * Searches the dictionary and displays results for the given query.
	 * 
	 * @param query
	 *            The search query
	 */
	private void showResults(String query) {
		Functions.d("SHOWING RESULTS FOR QUERY: " + query);
		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(SuggestionProvider.CONTENT_URI, null, null, new String[] { query }, null);

		if (cursor == null) {
			// There are no results
			mTextView.setText(getString(R.string.no_results, new Object[] { query }));
		} else {
			// Display the number of results
			int count = cursor.getCount();
			String countString = getResources().getQuantityString(R.plurals.search_results, count,
					new Object[] { count, query });
			mTextView.setText(countString);

			// Specify the columns we want to display in the result
			String[] from = new String[] { SearchManager.SUGGEST_COLUMN_TEXT_1 };

			// Specify the corresponding layout elements where we want the
			// columns to go
			int[] to = new int[] { R.id.word };

			// Create a simple cursor adapter for the definitions and apply them
			// to the ListView
			@SuppressWarnings("deprecation")
			SimpleCursorAdapter words = new SimpleCursorAdapter(this, R.layout.search_result, cursor, from, to);
			mListView.setAdapter(words);

			// Define the on-click listener for the list items
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Functions.d(String.valueOf(id));
					Uri uri = Uri.withAppendedPath(SuggestionProvider.CONTENT_URI, String.valueOf(id));
					goToCard(uri);
				}
			});
		}
	}
}
