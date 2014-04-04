package edu.gatech.hci.foodnavigator.search;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import edu.gatech.hci.foodnavigator.db.DatabaseHelper;
import edu.gatech.hci.foodnavigator.utilities.Functions;

public class SuggestionProvider extends ContentProvider {

	private DatabaseHelper mDB;

	private static final String AUTHORITY = "edu.gatech.hci.foodnavigator.search.SuggestionProvider";
	private static final String FOOD_LIST_PATH = "food";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + FOOD_LIST_PATH);

	private static final int SEARCH_FOOD = 0;
	private static final int GET_FOOD = 1;
	private static final int SEARCH_SUGGEST = 2;
	private static final int REFRESH_SHORTCUT = 3;

	private static final UriMatcher sURIMatcher = buildUriMatcher();

	/**
	 * Builds up a UriMatcher for search suggestion and shortcut refresh
	 * queries.
	 */
	private static UriMatcher buildUriMatcher() {
		UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		// to get definitions...
		matcher.addURI(AUTHORITY, FOOD_LIST_PATH, SEARCH_FOOD);
		matcher.addURI(AUTHORITY, FOOD_LIST_PATH + "/#", GET_FOOD);
		// to get suggestions...
		matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
		matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);

		/*
		 * The following are unused in this implementation, but if we include
		 * {@link SearchManager#SUGGEST_COLUMN_SHORTCUT_ID} as a column in our
		 * suggestions table, we could expect to receive refresh queries when a
		 * shortcutted suggestion is displayed in Quick Search Box, in which
		 * case, the following Uris would be provided and we would return a
		 * cursor with a single item representing the refreshed suggestion data.
		 */
		matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_SHORTCUT, REFRESH_SHORTCUT);
		matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_SHORTCUT + "/*", REFRESH_SHORTCUT);
		return matcher;
	}

	@Override
	public boolean onCreate() {
		mDB = DatabaseHelper.getInstance(getContext());
		return true;
	}

	/**
	 * Handles all the dictionary searches and suggestion queries from the
	 * Search Manager. When requesting a specific word, the uri alone is
	 * required. When searching all of the dictionary for matches, the
	 * selectionArgs argument must carry the search query as the first element.
	 * All other arguments are ignored.
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		// Use the UriMatcher to see what kind of query we have and format the
		// db query accordingly
		switch (sURIMatcher.match(uri)) {
		case SEARCH_SUGGEST:
			if (selectionArgs == null) {
				throw new IllegalArgumentException("selectionArgs must be provided for the Uri: " + uri);
			}
			return getSuggestions(selectionArgs[0]);
		case SEARCH_FOOD:
			if (selectionArgs == null) {
				throw new IllegalArgumentException("selectionArgs must be provided for the Uri: " + uri);
			}
			return search(selectionArgs[0]);
		case GET_FOOD:
			return getWord(uri);
		case REFRESH_SHORTCUT:
			return refreshShortcut(uri);
		default:
			throw new IllegalArgumentException("Unknown Uri: " + uri);
		}
	}

	private Cursor getSuggestions(String query) {
		Functions.d(query + " getSuggestions");
		query = query.toLowerCase();
		String[] columns = new String[] { BaseColumns._ID, DatabaseHelper.COL_FOOD_NAME };
		return mDB.getFoodMatches(query, columns);
	}

	private Cursor search(String query) {
		Functions.d(query + " search");
		query = query.toLowerCase();
		String[] columns = new String[] { BaseColumns._ID, DatabaseHelper.COL_FOOD_NAME };
		return mDB.getFoodMatches(query, columns);
	}

	private Cursor getWord(Uri uri) {
		String rowId = uri.getLastPathSegment();
		String[] columns = new String[] { BaseColumns._ID, DatabaseHelper.COL_FOOD_NAME };
		return mDB.getFood(rowId, columns);
	}

	private Cursor refreshShortcut(Uri uri) {
		/*
		 * This won't be called with the current implementation, but if we
		 * include {@link SearchManager#SUGGEST_COLUMN_SHORTCUT_ID} as a column
		 * in our suggestions table, we could expect to receive refresh queries
		 * when a shortcutted suggestion is displayed in Quick Search Box. In
		 * which case, this method will query the table for the specific word,
		 * using the given item Uri and provide all the columns originally
		 * provided with the suggestion query.
		 */
		String rowId = uri.getLastPathSegment();
		String[] columns = new String[] { BaseColumns._ID, DatabaseHelper.COL_FOOD_NAME,
				SearchManager.SUGGEST_COLUMN_SHORTCUT_ID, SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID };

		return mDB.getFood(rowId, columns);
	}

	// Other required implementations...

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		throw new UnsupportedOperationException();
	}

}
