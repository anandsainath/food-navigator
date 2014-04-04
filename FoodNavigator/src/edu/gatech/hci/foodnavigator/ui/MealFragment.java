package edu.gatech.hci.foodnavigator.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

import edu.gatech.hci.foodnavigator.R;
import edu.gatech.hci.foodnavigator.utilities.Functions;

public class MealFragment extends SherlockFragment implements OnClickListener {

	private Callbacks mCallbacks = _this;

	/** Start of callback implementation **/
	public interface Callbacks {
		public void homeButtonClick(int resID);
	}

	private static Callbacks _this = new Callbacks() {
		public void homeButtonClick(int resID) {
		}
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}
		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = _this;
	}

	/** END of callback implementation **/

	public MealFragment() {
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		int[] iButtons = { R.id.LL_Meal_breakfast, R.id.LL_Meal_lunch,
				R.id.LL_Meal_dinner, R.id.LL_Meal_dessert };
		View view = inflater.inflate(R.layout._meal_list, null);
		for (int id : iButtons) {
			try {
				view.findViewById(id).setOnClickListener(this);
			} catch (Exception E) {
				Functions.d("Exception Occured on " + id);
				Functions.d(E.getLocalizedMessage());
			}
		}
		return view;
	}

	@Override
	public void onClick(View v) {
		mCallbacks.homeButtonClick(v.getId());
	}
}
