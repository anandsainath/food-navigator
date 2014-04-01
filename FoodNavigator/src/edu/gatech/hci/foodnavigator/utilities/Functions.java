package edu.gatech.hci.foodnavigator.utilities;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import edu.gatech.hci.foodnavigator.R;
import edu.gatech.hci.foodnavigator.widget.TypefaceSpan;

public class Functions {

	public static final String LOG_TAG = "FoodNavigator";

	public static void d(String msg) {
		Log.d(LOG_TAG, msg);
	}

	public static void d(int val) {
		Log.d(LOG_TAG, val + "");
	}

	public static SpannableString getStyledErrorMessage(Context C, int resID) {
		SpannableString label = new SpannableString(C.getString(resID));
		label.setSpan(new TypefaceSpan(C), 0, label.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		label.setSpan(new StyleSpan(Typeface.BOLD), 0, label.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
		label.setSpan(new ForegroundColorSpan(R.color.swatch_10), 0, label.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return label;
	}

	public static int getVersionCode(Context C) {
		try {
			return C.getPackageManager().getPackageInfo(C.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			return 0;
		}
	}

	public static String getVersionName(Context C) {
		try {
			return C.getPackageManager().getPackageInfo(C.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			return null;
		}
	}

}
