package edu.gatech.hci.foodnavigator.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import edu.gatech.hci.foodnavigator.BaseActivity;
import edu.gatech.hci.foodnavigator.R;
import edu.gatech.hci.foodnavigator.menu.AppMainMenu;

public class HomeActivity extends BaseActivity implements
		AppHomeFragment.Callbacks {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_home);
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
					Toast.LENGTH_LONG).show();
			startActivity(new Intent(this, FoodTrayActivity.class));

			break;
		case R.id.home_indian:
			Toast.makeText(getApplicationContext(),
					"Will be available soon at only $1.99!", Toast.LENGTH_LONG)
					.show();
			// Toast.makeText(getApplicationContext(), R.string.inTitle,
			// Toast.LENGTH_LONG).show();
			break;
		case R.id.home_korean:
			Toast.makeText(getApplicationContext(),
					"Will be available soon at only $1.99!", Toast.LENGTH_LONG)
					.show();
			// Toast.makeText(getApplicationContext(), R.string.krTitle,
			// Toast.LENGTH_LONG).show();
			break;
		}

	}
}
