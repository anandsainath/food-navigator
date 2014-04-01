package edu.gatech.hci.foodnavigator.menu;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.hci.foodnavigator.R;

public class AppMenuConstants {

	public static final String MODE_SIMPLE_CARD = "SIMPLE_CARD_MODE";
	public static final String MODE_FLIP_CARD = "FLIP_CARD_MODE";
	public static final String MODE_LIST = "LIST_MODE";
	public static final String MENU_STAR_PREFIX = "STARRED_";

	public static enum Menu {
		// Main Menu items..
		SETTINGS("Settings", "Settings"), ABOUT("Info", "info"), HOME("Home", "wp_home");
		
		public String tag, value;

		private Menu(String tag, String value) {
			this.tag = tag;
			this.value = value;
		}
	};

	public static List<MenuListItem> AppMainMenu;

	static {

		AppMainMenu = new ArrayList<MenuListItem>(7);
		AppMainMenu.add(new MenuListItem(R.drawable.ic_action_home, Menu.HOME.tag, Menu.HOME.value, false,
				MenuListItem.MenuName.APP_MENU.getValue()));
		AppMainMenu.add(new MenuListItem(R.drawable.ic_action_settings, Menu.SETTINGS.tag, Menu.SETTINGS.value, false,
				MenuListItem.MenuName.APP_MENU.getValue()));
		AppMainMenu.add(new MenuListItem(R.drawable.ic_action_about, Menu.ABOUT.tag, Menu.ABOUT.value, false,
				MenuListItem.MenuName.APP_MENU.getValue()));
	}
}
