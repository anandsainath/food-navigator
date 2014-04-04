package edu.gatech.hci.foodnavigator.ui;

import android.content.Intent;
import android.os.Bundle;
import edu.gatech.hci.foodnavigator.BaseActivity;
import edu.gatech.hci.foodnavigator.R;
import edu.gatech.hci.foodnavigator.menu.AppMainMenu;
import edu.gatech.hci.foodnavigator.widget.CustomTextView;

public class MealActivity extends BaseActivity implements
		MealFragment.Callbacks {

	CustomTextView TV_PageTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meal_layout);
		TV_PageTitle = (CustomTextView) findViewById(R.id.TV_PageHeader);
		TV_PageTitle.setText(R.string.meal_title);

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
		case R.id.LL_Meal_breakfast:
		case R.id.LL_Meal_lunch:
		case R.id.LL_Meal_dinner:
		case R.id.LL_Meal_dessert:
			startActivity(new Intent(this, FoodTrayActivity.class));
			break;
		}
	}

}
