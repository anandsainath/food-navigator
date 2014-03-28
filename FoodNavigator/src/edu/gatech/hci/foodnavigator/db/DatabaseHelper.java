package edu.gatech.hci.foodnavigator.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String LOG = "DatabaseHelper";

	private static DatabaseHelper sInstance;

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "food_navigator.db";

	/* TABLE NAMES */
	private static final String TBL_FOOD_INDEX = "tbl_food_index";
	private static final String TBL_COUNTRY = "tbl_country";
	private static final String TBL_FOOD_COUNTRY = "tbl_food_country";
	// I am using ISO 639-1 two-letter codes for language
	// http://en.wikipedia.org/wiki/List_of_ISO_639-1_codes
	private static final String TBL_DESC_EN = "tbl_description_en";
	private static final String TBL_DESC_KO = "tbl_description_ko";
	private static final String TBL_DESC_HI = "tbl_description_hi";

	private static final String KEY_ID_FOOD = "id_food";

	/* FOOD_INDEX COLUMN NAMES */
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
			+ TBL_FOOD_INDEX + "(" + KEY_ID_FOOD + " INTEGER PRIMARY KEY,"
			+ KEY_NAME + " TEXT" + ")";

	private static final String CREATE_TBL_FOOD_COUNTRY = "CREATE TABLE "
			+ TBL_COUNTRY + "(" + KEY_ID_FOOD + " INTEGER PRIMARY KEY,"
			+ KEY_ID_COUNTRY + " INTEGER" + ")";

	private static final String CREATE_TBL_COUNTRY = "CREATE TABLE "
			+ TBL_COUNTRY + "(" + KEY_ID_FOOD + " INTEGER PRIMARY KEY,"
			+ KEY_COUNTRY_NAME + " TEXT" + ")";

	private static final String CREATE_TBL_DESC_EN = "CREATE TABLE "
			+ TBL_DESC_EN + "(" + KEY_ID_FOOD + " INTEGER PRIMARY KEY,"
			+ KEY_PRONUNCIATION + " TEXT" + ")";

	private static final String CREATE_TBL_DESC_KO = "CREATE TABLE "
			+ TBL_DESC_KO + "(" + KEY_ID_FOOD + " INTEGER PRIMARY KEY,"
			+ KEY_PRONUNCIATION + " TEXT" + ")";

	private static final String CREATE_TBL_DESC_HI = "CREATE TABLE "
			+ TBL_DESC_HI + "(" + KEY_ID_FOOD + " INTEGER PRIMARY KEY,"
			+ KEY_PRONUNCIATION + " TEXT" + ")";

	public static DatabaseHelper getInstance(Context context) {

		// Use the application context, which will ensure that you
		// don't accidentally leak an Activity's context.
		// See this article for more information: http://bit.ly/6LRzfx
		if (sInstance == null) {
			sInstance = new DatabaseHelper(context.getApplicationContext());
		}
		return sInstance;
	}

	/**
	 * Constructor should be private to prevent direct instantiation. make call
	 * to static factory method "getInstance()" instead.
	 */
	private DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TBL_FOOD_INDEX);
		db.execSQL(CREATE_TBL_FOOD_COUNTRY);
		db.execSQL(CREATE_TBL_COUNTRY);
		db.execSQL(CREATE_TBL_DESC_EN);
		db.execSQL(CREATE_TBL_DESC_KO);
		db.execSQL(CREATE_TBL_DESC_HI);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TBL_FOOD_INDEX);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TBL_FOOD_COUNTRY);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TBL_COUNTRY);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TBL_DESC_EN);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TBL_DESC_KO);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TBL_DESC_HI);

		// create new tables
		onCreate(db);

	}
}
