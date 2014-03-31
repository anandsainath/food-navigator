package edu.gatech.hci.foodnavigator.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import edu.gatech.hci.foodnavigator.ui.model.Food;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String TAG = " ~~~~ DatabaseHelper";

	private static DatabaseHelper sInstance;
	private final Context context;
	private SQLiteDatabase db;

	private static final int DATABASE_VERSION = 8;

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
	private static final String KEY_NAME = "name";

	/* FOOD_COUNTRY COLUMN NAMES */

	/* COUNTRY COLUMN NAMES */
	private static final String KEY_ID_COUNTRY = "id_country";
	private static final String KEY_COUNTRY_NAME = "country_name";

	/* DESCRIPTION (en, ko, hi, ...) COLUMN NAMES */
	private static final String KEY_PRONUNCIATION = "pronunciation";
	private static final String KEY_DESCRIPTION = "description";

	/* CREATE TABLE STATEMENTS */
	private static final String CREATE_TBL_FOOD_INDEX = "CREATE TABLE "
			+ TBL_FOOD_INDEX + " (" + KEY_ID + " INTEGER PRIMARY KEY, "
			+ KEY_NAME + " TEXT" + ")";

	private static final String CREATE_TBL_FOOD_COUNTRY = "CREATE TABLE "
			+ TBL_FOOD_COUNTRY + " (" + KEY_ID_FOOD + " INTEGER PRIMARY KEY, "
			+ KEY_ID_COUNTRY + " INTEGER" + ")";

	private static final String CREATE_TBL_COUNTRY = "CREATE TABLE "
			+ TBL_COUNTRY + " (" + KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_COUNTRY_NAME + " TEXT" + ")";

	private static final String CREATE_TBL_DESC_EN = "CREATE TABLE "
			+ TBL_DESC_EN + " (" + KEY_ID_FOOD + " INTEGER PRIMARY KEY,"
			+ KEY_PRONUNCIATION + " TEXT, " + KEY_DESCRIPTION + " TEXT " + ")";

	private static final String CREATE_TBL_DESC_KO = "CREATE TABLE "
			+ TBL_DESC_KO + " (" + KEY_ID_FOOD + " INTEGER PRIMARY KEY,"
			+ KEY_PRONUNCIATION + " TEXT, " + KEY_DESCRIPTION + " TEXT " + ")";

	private static final String CREATE_TBL_DESC_HI = "CREATE TABLE "
			+ TBL_DESC_HI + " (" + KEY_ID_FOOD + " INTEGER PRIMARY KEY,"
			+ KEY_PRONUNCIATION + " TEXT, " + KEY_DESCRIPTION + " TEXT " + ")";

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
			Log.d(TAG, " ~~~ Creating DB - In getInstance");
		} else {
			Log.d(TAG, " ~~~~~ DB instance exists already");
		}

		// System.out.println("###############");
		// System.out.println(CREATE_TBL_FOOD_INDEX);
		// System.out.println(CREATE_TBL_COUNTRY);
		// System.out.println(CREATE_TBL_FOOD_COUNTRY);
		// System.out.println(CREATE_TBL_DESC_EN);
		// System.out.println(CREATE_TBL_DESC_HI);
		// System.out.println(CREATE_TBL_DESC_KO);

		return sInstance;
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

		// db.execSQL(CREATE_TBL_FOOD_INDEX);
		// db.execSQL(CREATE_TBL_FOOD_COUNTRY);
		// db.execSQL(CREATE_TBL_COUNTRY);
		// db.execSQL(CREATE_TBL_DESC_EN);
		// db.execSQL(CREATE_TBL_DESC_KO);
		// db.execSQL(CREATE_TBL_DESC_HI);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// db.execSQL("DROP TABLE IF EXISTS " + TBL_FOOD_INDEX);
		// db.execSQL("DROP TABLE IF EXISTS " + TBL_FOOD_COUNTRY);
		// db.execSQL("DROP TABLE IF EXISTS " + TBL_COUNTRY);
		// db.execSQL("DROP TABLE IF EXISTS " + TBL_DESC_EN);
		// db.execSQL("DROP TABLE IF EXISTS " + TBL_DESC_KO);
		// db.execSQL("DROP TABLE IF EXISTS " + TBL_DESC_HI);
		//
		// sInstance = null;
		// this.getInstance(context.getApplicationContext());

		// create new tables
		// onCreate(db);

	}

	/* ~~~~~~~~~ DB METHODS ~~~~~~~~~~~ */
	public Food getFood(int id, int country_id) {
		Food food = new Food();
		SQLiteDatabase db = this.getReadableDatabase();

		String query = "SELECT _id, name, pronunciation, description FROM "
				+ TBL_FOOD_INDEX + " NATURAL JOIN " + TBL_DESC_KO + ";";

		Cursor cursor = db.rawQuery(query, null);

		if (cursor.moveToFirst()) {
			do {
				food.setId(Integer.parseInt(cursor.getString(0)));
				food.setName(cursor.getString(1));
				food.setPronunciation(cursor.getString(2));
				food.setDescription(cursor.getString(3));

			} while (cursor.moveToNext());
		}
		db.close();

		return food;
	}
}
