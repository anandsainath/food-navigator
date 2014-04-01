package edu.gatech.hci.foodnavigator.menu;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockListFragment;

import edu.gatech.hci.foodnavigator.R;

public class MenuListFragment extends SherlockListFragment implements OnItemClickListener {

	private Callbacks mCallbacks = _this;
	protected int mSelectedPosition = 1;

	public MenuListFragment() {
		setRetainInstance(true);
	}

	public interface Callbacks {
		public void menuClick(String value, String tag);

		public void subMenuClick(String value, String tag);
	}

	private static Callbacks _this = new Callbacks() {
		public void menuClick(String value, String tag) {
		}

		public void subMenuClick(String value, String tag) {

		}
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}
		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = _this;
	}

	protected void onItemClicked(MenuListItem item) {
		if (item.menuType == MenuListItem.MenuName.APP_MENU.getValue()) {
			mCallbacks.menuClick(item.value, item.tag);
		} else {
			mCallbacks.subMenuClick(item.value, item.tag);
		}
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ListView lView = getListView();
		if (lView != null) {
			lView.setOnItemClickListener(this);
			lView.setDivider(null);
			lView.setDividerHeight(0);
			lView.setBackgroundResource(R.color.swatch_10);
			lView.setCacheColorHint(Color.TRANSPARENT);
		}
	}

	@Override
	public synchronized void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		MenuListItem item = (MenuListItem) parent.getItemAtPosition(position);
		if (item.iconRes != MenuListItemAdapter.HEADER) {
			if (item.isAvailable) {
				/**
				 * Continue with the intended menu action only if the menu item
				 * is available.
				 */
				onItemClicked(item);
				if (item.menuType == MenuListItem.MenuName.LEFT_CONTEXT_MENU.getValue()
						|| item.menuType == MenuListItem.MenuName.RIGHT_CONTEXT_MENU.getValue()) {
					if (mSelectedPosition != ListView.INVALID_POSITION) {
						View prevSelectedView = (View) getListView().getChildAt(mSelectedPosition);
						if (prevSelectedView != null) {
							RelativeLayout rootLayout = (RelativeLayout) prevSelectedView
									.findViewById(R.id.RL_MenuItemRoot);
							if (rootLayout != null) {
								rootLayout.setBackgroundColor(Color.TRANSPARENT);
							}
						}
					}
					RelativeLayout rootLayout = (RelativeLayout) view.findViewById(R.id.RL_MenuItemRoot);
					if (item.menuType == MenuListItem.MenuName.LEFT_CONTEXT_MENU.getValue()) {
						rootLayout.setBackgroundResource(R.drawable.menu_selected_left);
					} else {
						rootLayout.setBackgroundResource(R.drawable.menu_selected_right);
					}
					mSelectedPosition = position;
				}
			} else {
				/**
				 * Show a dialog asking the user if he wants to purchase the
				 * same via in-app purchase
				 */
			}
		}
	}

}
