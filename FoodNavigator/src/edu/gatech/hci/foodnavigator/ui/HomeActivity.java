package edu.gatech.hci.foodnavigator.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import edu.gatech.hci.foodnavigator.BaseActivity;
import edu.gatech.hci.foodnavigator.R;
import edu.gatech.hci.foodnavigator.menu.AppMainMenu;
import edu.gatech.hci.foodnavigator.widget.CustomTextView;

public class HomeActivity extends BaseActivity implements
		AppHomeFragment.Callbacks {

	CustomTextView TV_PageTitle;
	public static final String PREFS_NAME = "LANGUAGE_SELECTION_PREFERENCE";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_home);

		if (!isLanguageSet()) {
			final Dialog dialog = new Dialog(this);
			dialog.setTitle(R.string.language_preference);
			dialog.setContentView(R.layout._preference_dialog);

			final Spinner spinner = (Spinner) dialog
					.findViewById(R.id.S_LanguagePreference);
			// Create an ArrayAdapter using the string array and a default
			// spinner layout
			ArrayAdapter<CharSequence> adapter = ArrayAdapter
					.createFromResource(this, R.array.language_preference,
							android.R.layout.simple_spinner_item);
			// Specify the layout to use when the list of choices appears
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// Apply the adapter to the spinner
			spinner.setAdapter(adapter);

			Button okButton = (Button) dialog.findViewById(R.id.Btn_Ok);
			okButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String language = getResources().getStringArray(
							R.array.language_preference)[spinner
							.getSelectedItemPosition()];
					String str = language
							+ " is set as the language preference!";
					Toast.makeText(getApplicationContext(), str,
							Toast.LENGTH_LONG).show();
					commitLanguagePreference(language);
					dialog.dismiss();
				}
			});

			dialog.show();
		}

		TV_PageTitle = (CustomTextView) findViewById(R.id.TV_PageHeader);
		TV_PageTitle.setText(R.string.home_page_title);
		// Check if the layout rendered is for a small screen and
		// add App Main Menu to the sliding menu frame.
		if (findViewById(R.id.HomeView).getTag() == getResources().getString(
				R.string.small_screen)) {
			setMainMenu(new AppMainMenu());
		}
	}

	@Override
	public void homeButtonClick(int resID) {
		switch (resID) {
		case R.id.home_american:
			Toast.makeText(getApplicationContext(), R.string.usTitle,
					Toast.LENGTH_SHORT).show();
			startActivity(new Intent(this, MealActivity.class));
			break;
		case R.id.home_indian:
		case R.id.home_korean:
			Toast.makeText(getApplicationContext(),
					"Will be available soon at only $1.99!", Toast.LENGTH_SHORT)
					.show();
			break;
		}

	}

	private void commitLanguagePreference(String language) {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("LanguageSet", language);
		editor.commit();
	}

	private boolean isLanguageSet() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,
				Activity.MODE_PRIVATE);
		return (settings.getString("Language", null) == null) ? false : true;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.app_home_menu,
				(com.actionbarsherlock.view.Menu) menu);
		this.menu = menu;
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search:
			onSearchRequested();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
