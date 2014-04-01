package edu.gatech.hci.foodnavigator;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import edu.gatech.hci.foodnavigator.R.integer;
import edu.gatech.hci.foodnavigator.menu.MenuListFragment;
import edu.gatech.hci.foodnavigator.widget.TypefaceSpan;

/**
 * Base Class that will implement all the required features prevalent across all
 * the UI activity screens
 * 
 * @author anandsainath
 * 
 */
public class BaseActivity extends SlidingFragmentActivity implements MenuListFragment.Callbacks {
	protected ActionBar titleBar;
	protected boolean isSlidingMenuEnabled = false;
	protected Menu menu;

	public final String TAG = "FOOD_NAVIGATOR";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SpannableString title = new SpannableString(this.getString(R.string.app_name));
		title.setSpan(new TypefaceSpan(getApplicationContext()), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		setTitle(title);
		
		titleBar = getSupportActionBar();
		setBehindContentView(getMenuFrame(R.id.main_menu));
		setSlidingActionBarEnabled(false);
	}

	/**
	 * Sets the main menu content as the sliding menu. Called only on small
	 * screens where the menu is not displayed as a part of the screen.
	 */
	protected void setMainMenu(SherlockListFragment menuFragment) {
		// SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setBehindOffset(getAppDrawerWidth());
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setSlidingEnabled(false);

		// Menu Frame
		getSupportFragmentManager().beginTransaction().replace(R.id.main_menu, menuFragment).commit();

		// Show the arrow indicator for the sliding menu
		int upId = Resources.getSystem().getIdentifier("up", "id", "android");
		if (upId > 0) {
			ImageView up = (ImageView) findViewById(upId);
			up.setImageResource(R.drawable.ic_navigation_drawer);
		}
		titleBar.setDisplayHomeAsUpEnabled(true);
		setSlidingActionBarEnabled(false);
		isSlidingMenuEnabled = true;
	}

	/**
	 * Returns a width in pixels denoting 25% of the screen width, which will be
	 * set as the width of the sliding menu frame
	 * 
	 * @return {@link integer}
	 */
	private int getAppDrawerWidth() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int width = displaymetrics.widthPixels;
		return isScreenLarge() ? ((int) (width - width * 0.35)) : ((int) (width - width * 0.75));
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	protected boolean isScreenLarge() {
		final int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			return screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE
					|| screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE;
		} else {
			return screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE;
		}
	}

	/**
	 * Inflates a ActionBar Button on the top right if there is a secondary
	 * (Context Menu)
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			if (getSlidingMenu().getMode() == SlidingMenu.LEFT_RIGHT || getSlidingMenu().getMode() == SlidingMenu.RIGHT) {
				com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
				inflater.inflate(R.menu.context_menu, (com.actionbarsherlock.view.Menu) menu);
				this.menu = menu;
			}
		} catch (Exception e) {
			Log.e(TAG, "Eror", e);
		}
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Creates a menu frame that can be used as a main or context based menu
	 * 
	 * @param ID
	 * @return {@link FrameLayout}
	 */
	protected FrameLayout getMenuFrame(int ID) {
		FrameLayout menuFrame = null;
		menuFrame = new FrameLayout(getApplicationContext());
		menuFrame.setId(ID);
		return menuFrame;
	}

	/**
	 * Handling common ActionBar events.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		case R.id.context_menu:
			if (getSlidingMenu().isMenuShowing()) {
				toggle();
			} else {
				showSecondaryMenu();
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Handling menu button click event..
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_MENU) && event.getRepeatCount() == 0) {
			if (getSlidingMenu().getMode() == SlidingMenu.LEFT_RIGHT && !getSlidingMenu().isMenuShowing()) {
				showSecondaryMenu();
			} else {
				getSlidingMenu().toggle(true);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void subMenuClick(String value, String tag) {
		// Will be Overridden by activities that have a Context menu
	}

	@Override
	public void menuClick(String value, String tag) {
		// Handling the main menu click events app wide..
		if (isSlidingMenuEnabled) {
			getSlidingMenu().toggle(true);
		}
		// Handle other events here.
	}
}
