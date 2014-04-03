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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import edu.gatech.hci.foodnavigator.R;

public class FoodDetailsActivity extends Activity implements OnInitListener {

	protected TextToSpeech TTSEngine;
	protected int TTS_AVAILABLE;
	public static final String PREFS_NAME = "TTS_INSTALLER_PREFERENCE";
	private ImageView IV_TTS;

	/* views to be updated */
	private TextView tvEnName, tvEnPronunciation, tvOwnName, tvOwnDesc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.food_details);
		IV_TTS = (ImageView) findViewById(R.id.IV_TTS);

		tvEnName = (TextView) findViewById(R.id.TV_FoodName);
		tvEnPronunciation = (TextView) findViewById(R.id.TV_FoodPronunciation);
		tvOwnName = (TextView) findViewById(R.id.TV_FoodMeaning1);
		tvOwnDesc = (TextView) findViewById(R.id.TV_FoodMeaning2);

		/* grab foodId passed in the bundle */
		String foodId = getIntent().getStringExtra("foodId");

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
					TTSEngine.speak("Mimosa", TextToSpeech.QUEUE_FLUSH, null);
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
}
