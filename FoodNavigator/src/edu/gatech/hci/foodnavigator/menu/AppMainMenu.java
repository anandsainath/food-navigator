package edu.gatech.hci.foodnavigator.menu;

import android.os.Bundle;

public class AppMainMenu extends MenuListFragment {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MenuListItemAdapter adapter = new MenuListItemAdapter(getActivity().getApplicationContext());
		for (MenuListItem item : AppMenuConstants.AppMainMenu) {
			adapter.add(item);
		}
		setListAdapter(adapter);
	}

}
