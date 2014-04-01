package edu.gatech.hci.foodnavigator.menu;

import edu.gatech.hci.foodnavigator.R;

public class MenuListItem {
	public String tag;
	public String value;
	public int iconRes;
	public int menuType;
	public boolean isSelected;
	public boolean isAvailable = true;

	public static enum MenuName {
		APP_MENU(1), LEFT_CONTEXT_MENU(R.drawable.menu_selected_left), RIGHT_CONTEXT_MENU(
				R.drawable.menu_selected_right), NONE(-1);
		private int value;

		private MenuName(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public MenuListItem(int iconRes, String tag, String value) {
		this.value = value;
		this.tag = tag;
		this.iconRes = iconRes;
		this.isSelected = false;
		this.menuType = MenuName.NONE.getValue();
	}

	public MenuListItem(int iconRes, String tag, String value, Boolean isSelected) {
		this.tag = tag;
		this.value = value;
		this.iconRes = iconRes;
		this.isSelected = isSelected;
		this.menuType = MenuName.APP_MENU.getValue();
	}

	public MenuListItem(int iconRes, String tag, String value, Boolean isSelected, int menuType) {
		this.tag = tag;
		this.value = value;
		this.iconRes = iconRes;
		this.isSelected = isSelected;
		this.menuType = menuType;
	}

	public MenuListItem(int iconRes, String tag) {
		this.value = null;
		this.tag = tag;
		this.iconRes = iconRes;
		this.isSelected = false;
		this.menuType = MenuName.NONE.getValue();
	}

}