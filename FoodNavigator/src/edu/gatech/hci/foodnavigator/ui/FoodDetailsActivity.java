package edu.gatech.hci.foodnavigator.ui;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import edu.gatech.hci.foodnavigator.BaseActivity;
import edu.gatech.hci.foodnavigator.R;
import edu.gatech.hci.foodnavigator.db.DatabaseHelper;
import edu.gatech.hci.foodnavigator.menu.AppMainMenu;
import edu.gatech.hci.foodnavigator.model.Food;

public class FoodDetailsActivity extends BaseActivity implements OnInitListener {

	private static final String TAG = " @@@ FoodDetail";

	protected TextToSpeech TTSEngine;
	protected int TTS_AVAILABLE;
	public static final String PREFS_NAME = "TTS_INSTALLER_PREFERENCE";
	private ImageView IV_TTS;

	private DatabaseHelper db;

	/* views to be updated */
	private TextView tvEnName, tvEnPronunciation, tvLocalName, tvLocalDesc;
	private ImageView ivFavorite;
	private Food food;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.food_details);
		setMainMenu(new AppMainMenu());

		IV_TTS = (ImageView) findViewById(R.id.IV_TTS);

		tvEnName = (TextView) findViewById(R.id.TV_FoodName);
		tvEnPronunciation = (TextView) findViewById(R.id.TV_FoodPronunciation);
		tvLocalName = (TextView) findViewById(R.id.TV_FoodMeaning1);
		tvLocalDesc = (TextView) findViewById(R.id.TV_FoodMeaning2);
		ivFavorite = (ImageView) findViewById(R.id.IV_Favorite);

		/* grab foodId passed in the bundle */
		// TODO: handle exception
		int foodId = Integer.parseInt(getIntent().getStringExtra("foodId"));

		// hardcoding countryId for testing
		int countryId = 2; // 2 for Korean

		db = DatabaseHelper.getInstance(this.getApplicationContext());
		food = db.getFood(foodId, countryId);
		// TODO update db table to reflect favorite? or create a separate table
		// for favorite

		ivFavorite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toggleFavorite(food);
			}

		});
		// update contents displayed based on retrieved food info
		updateFoodInfoViews(food);

		try {
			Intent checkTTSIntent = new Intent();
			checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
			startActivityForResult(checkTTSIntent, TTS_AVAILABLE);
		} catch (Exception E) {
			Toast.makeText(getApplicationContext(),
					"Speech Engine is not supported on your device",
					Toast.LENGTH_LONG).show();
		}

		IV_TTS.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					String name = "Mimosa";
					if (!food.getEnName().equals("")) {
						name = food.getEnName();
					}
					TTSEngine.speak(name, TextToSpeech.QUEUE_FLUSH, null);
				} catch (Exception E) {
					Log.e("Food", "Error", E);
				}
			}
		});
	}

	private void commitTTSPreference(boolean flag) {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("VoicePurchase", flag);
		editor.commit();
	}

	private boolean readTTSPreference(boolean flag) {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,
				Activity.MODE_PRIVATE);
		return settings.getBoolean("VoicePurchase", flag);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		final CheckBox dontShowAgain;
		try {
			Context context = getApplicationContext();
			if (requestCode == TTS_AVAILABLE) {
				if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
					TTSEngine = new TextToSpeech(context, this);
				} else {
					if (readTTSPreference(false)) {
						AlertDialog.Builder ADB = new AlertDialog.Builder(
								context);
						LayoutInflater adbInflater = LayoutInflater
								.from(context);
						View eulaLayout = adbInflater.inflate(
								R.layout.dialog_checkbox, null);
						dontShowAgain = (CheckBox) eulaLayout
								.findViewById(R.id.skip);
						ADB.setView(eulaLayout);
						ADB.setTitle("Missing TTS Engine");
						ADB.setMessage(R.string.TTS_NA_MSG);
						ADB.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										Intent InstallTTS = new Intent();
										InstallTTS
												.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
										startActivity(InstallTTS);
										return;
									}
								});
						ADB.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										boolean flag = (dontShowAgain
												.isChecked()) ? true : false;
										commitTTSPreference(flag);
										return;
									}
								});
						ADB.show();
					}
				}
			}
		} catch (Exception E) {
			// TODO: handle exception
		}
	}

	@Override
	public void onInit(int initStatus) {
		if (initStatus == TextToSpeech.SUCCESS) {
			if (TTSEngine.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE) {
				TTSEngine.setLanguage(Locale.US);
			}
		} else if (initStatus == TextToSpeech.ERROR) {
			Toast.makeText(getApplicationContext(),
					"Speech Engine is unsupported by your device",
					Toast.LENGTH_LONG).show();
		}
	}

	// update contents displayed based on retrieved food info
	private void updateFoodInfoViews(Food food) {
		tvEnName.setText(food.getEnName());
		tvEnPronunciation.setText(food.getEnPronunciation());
		tvLocalName.setText(food.getLocalName());
		tvLocalDesc.setText(food.getLocalDescription());
		if (food.getFavorite()) {
			ivFavorite.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_favorite_on));
		} else {
			ivFavorite.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_favorite_off));
		}
	}

	private void toggleFavorite(Food food) {
		if (food.getFavorite()) {
			// currently on --> turn it off
			ivFavorite.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_favorite_off));
			food.setFavorite(false);
			// TODO UPDATE IN DB
			Toast.makeText(getApplicationContext(), "Removed from favorite",
					Toast.LENGTH_SHORT).show();
		} else {
			// currently off --> turn it on
			ivFavorite.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_favorite_on));
			food.setFavorite(true);
			// TODO UPDATE IN DB
			Toast.makeText(getApplicationContext(), "Added to favorite",
					Toast.LENGTH_SHORT).show();
		}
	}
}
