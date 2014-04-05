package edu.gatech.hci.foodnavigator.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import edu.gatech.hci.foodnavigator.model.Food;
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String TAG = " ~~~~ DatabaseHelper";

	private static DatabaseHelper sInstance;
	private final Context context;
	private SQLiteDatabase db;

	private static final int DATABASE_VERSION = 13;

	private static final String DATABASE_NAME = "food_navigator.db";
	private static final String DATABASE_PATH = "/data/data/edu.gatech.hci.foodnavigator/databases/";

	/* TABLE NAMES */
	private static final String TBL_FOOD_INDEX = "tbl_food_index";
	private static final String TBL_COUNTRY = "tbl_country";
	private static final String TBL_FOOD_COUNTRY = "tbl_food_country";
	// I am using ISO 639-1 two-letter codes for language
	// http://en.wikipedia.org/wiki/List_of_ISO_639-1_codes
	private static final String TBL_DESC_EN = "tbl_description_en";
	private static final String TBL_DESC_KO = "tbl_description_ko";
	private static final String TBL_DESC_HI = "tbl_description_hi";

	private static final String KEY_ID = "_id";

	/* FOOD_INDEX COLUMN NAMES */
	private static final String KEY_ID_FOOD = KEY_ID;
	public static final String COL_FOOD_NAME = "name";

	/* FOOD_COUNTRY COLUMN NAMES */

	/* COUNTRY COLUMN NAMES */
	private static final String KEY_ID_COUNTRY = "id_country";
	private static final String KEY_COUNTRY_NAME = "country_name";

	/* DESCRIPTION (en, ko, hi, ...) COLUMN NAMES */
	private static final String KEY_PRONUNCIATION = "pronunciation";
	private static final String KEY_DESCRIPTION = "description";
	private static final HashMap<String, String> mColumnMap = buildColumnMap();

	public static DatabaseHelper getInstance(Context context) {
		Log.d(TAG, "~~~ In getInstance");
		
		// Use the application context, which will ensure that you
		// don't accidentally leak an Activity's context.
		// See this article for more information: http://bit.ly/6LRzfx
		if (sInstance == null) {
			sInstance = new DatabaseHelper(context.getApplicationContext());

			/* check and copy from the db file in assets directory */
			try {
				sInstance.createDataBase();

			} catch (IOException ioe) {
				throw new Error("Unable to create database");
			}

			try {
				sInstance.openDataBase();

			} catch (SQLException sqle) {
				throw sqle;
			}
			Log.d(TAG, " ~~~ Creating  - In getInstance");
		} else {
			Log.d(TAG, " ~~~~~ DB instance exists already");
		}

		return sInstance;
	}

	/**
	 * Builds a map for all columns that may be requested, which will be given
	 * to the SQLiteQueryBuilder. This is a good way to define aliases for
	 * column names, but must include all columns, even if the value is the key.
	 * This allows the ContentProvider to request columns w/o the need to know
	 * real column names and create the alias itself.
	 */
	private static HashMap<String, String> buildColumnMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(COL_FOOD_NAME, COL_FOOD_NAME + " as "
				+ SearchManager.SUGGEST_COLUMN_TEXT_1);
		map.put(BaseColumns._ID, "rowid AS " + BaseColumns._ID);
		map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, "rowid AS "
				+ SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
		map.put(SearchManager.SUGGEST_COLUMN_SHORTCUT_ID, "rowid AS "
				+ SearchManager.SUGGEST_COLUMN_SHORTCUT_ID);
		return map;
	}

	/**
	 * Constructor should be private to prevent direct instantiation. make call
	 * to static factory method "getInstance()" instead.
	 */
	private DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
		Log.d(TAG, " ~~~~ IN PRIVATE CONSTRUCTOR TO CREATE DB");
	}

	/**
	 * Creates an empty database on the system and rewrites it with the external
	 * database.
	 * */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			// do nothing - database already exist
		} else {

			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();

			try {

				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DATABASE_PATH + DATABASE_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			// database does't exist yet.

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from local assets folder to the just created empty
	 * database in the system folder, from where it can be accessed and handled.
	 * This is done by transferring bytestream.
	 * */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = context.getAssets().open(DATABASE_NAME);

		// Path to the just created empty db
		String outFileName = DATABASE_PATH + DATABASE_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException {

		// Open the database
		String myPath = DATABASE_PATH + DATABASE_NAME;
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);

	}

	@Override
	public synchronized void close() {

		if (db != null)
			db.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "@@@ DB onUpgrade old v: " + oldVersion + "  @@ new v: "
				+ newVersion);
		
		sInstance.context.deleteDatabase(DATABASE_NAME);
		sInstance = null;
		this.getInstance(context);
		
	}

	/* ~~~~~~~~~ DB METHODS ~~~~~~~~~~~ */

	/*
	 * retrieve information about food identified with foodId and localized
	 * content in which country is identified with countryId
	 */
	public Food getFood(int foodId, int countryId) {
		Food food = new Food();
		SQLiteDatabase db = this.getReadableDatabase();

		String localTable = ""; // to store the localization table of selected
								// countryId
		switch (countryId) {
		default:
			localTable = TBL_DESC_EN;
			break;
		case 1:
			// English (USA)
			localTable = TBL_DESC_EN;
			break;
		case 2:
			// Korean
			localTable = TBL_DESC_KO;
			break;
		case 3:
			// Hindi
			localTable = TBL_DESC_HI;
			break;
		}

		String query = "SELECT F._id, E.name, E.pronunciation, E.description, L.name, L.description FROM "
				+ TBL_FOOD_INDEX
				+ " AS F JOIN "
				+ TBL_DESC_EN
				+ " AS E ON F._id=E._id JOIN "
				+ localTable
				+ " AS L ON F._id=L._id WHERE F._id = ?";

		Cursor cursor = db.rawQuery(query,
				new String[] { String.valueOf(foodId) });

		if (cursor.moveToFirst()) {
			do {
				food.setId(Integer.parseInt(cursor.getString(0)));
				food.setLocalFoodName(cursor.getString(1));
				food.setLocalPronun(cursor.getString(2));
				food.setLocalDescription(cursor.getString(3));
				food.setUserFoodName(cursor.getString(4));
				food.setUserDescription(cursor.getString(5));

			} while (cursor.moveToNext());
		}
		db.close();
		return food;
	}

	/**
	 * Returns a Cursor over all foods that match the given query
	 * 
	 * @param query
	 *            The string to search for
	 * @param columns
	 *            The columns to include, if null then all are included
	 * @return Cursor over all foods that match, or null if none found.
	 */
	public Cursor getFoodMatches(String query, String[] columns) {
		String selection = COL_FOOD_NAME + " LIKE ?";
		String[] selectionArgs = new String[] { query + "%" };
		// Functions.d("Before calling query function..");
		return query(selection, selectionArgs, columns);
	}

	/**
	 * Returns a Cursor positioned at the word specified by rowId
	 * 
	 * @param rowId
	 *            id of food to retrieve
	 * @param columns
	 *            The columns to include, if null then all are included
	 * @return Cursor positioned to matching food, or null if not found.
	 */
	public Cursor getFood(String rowId, String[] columns) {
		String selection = "rowid = ?";
		String[] selectionArgs = new String[] { rowId };
		return query(selection, selectionArgs, columns);
	}

	/**
	 * Performs a database query.
	 * 
	 * @param selection
	 *            The selection clause
	 * @param selectionArgs
	 *            Selection arguments for "?" components in the selection
	 * @param columns
	 *            The columns to return
	 * @return A Cursor over all rows matching the query
	 */
	private Cursor query(String selection, String[] selectionArgs,
			String[] columns) {
		/*
		 * The SQLiteBuilder provides a map for all possible columns requested
		 * to actual columns in the database, creating a simple column alias
		 * mechanism by which the ContentProvider does not need to know the real
		 * column names
		 */
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(TBL_FOOD_INDEX);
		builder.setProjectionMap(mColumnMap);

		Cursor cursor = builder.query(getReadableDatabase(), columns,
				selection, selectionArgs, null, null, null);

		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		return cursor;
	}
}
