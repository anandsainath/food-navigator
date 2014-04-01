package edu.gatech.hci.foodnavigator.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import edu.gatech.hci.foodnavigator.R;

public class MenuListItemAdapter extends ArrayAdapter<MenuListItem> {

	public static final int HEADER = 0;
	public static final int ONLY_TEXT = 1;

	public MenuListItemAdapter(Context context) {
		super(context, 0);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		TextView title = null;
		ImageView icon = null;
		MenuListItem item = getItem(position);
		switch (item.iconRes) {
		case HEADER:
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_head, null);
			TextView head = (TextView) convertView.findViewById(R.id.TV_MenuHeader);
			head.setText(item.tag);
			break;
		case ONLY_TEXT:
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_row, null);
			checkIfItemSelected(position, convertView);
			icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setVisibility(View.GONE);
			title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(item.tag);
			if (!item.isAvailable) {
				convertView.findViewById(R.id.IV_PurchaseIndicator).setVisibility(View.VISIBLE);
			}
			break;
		default:
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_row, null);
			checkIfItemSelected(position, convertView);
			icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(item.iconRes);
			title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(item.tag);
		}
		return convertView;
	}

	private void checkIfItemSelected(int position, View convertView) {
		MenuListItem item = getItem(position);
		if (item.isSelected && item.menuType != MenuListItem.MenuName.NONE.getValue()) {
			RelativeLayout rootLayout = (RelativeLayout) convertView.findViewById(R.id.RL_MenuItemRoot);
			rootLayout.setBackgroundResource(getItem(position).menuType);
		}
	}
}