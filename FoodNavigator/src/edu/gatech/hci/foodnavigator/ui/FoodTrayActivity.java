/*
 * Copyright (C) 2011 Scott Lund
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.gatech.hci.foodnavigator.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import edu.gatech.hci.foodnavigator.BaseActivity;
import edu.gatech.hci.foodnavigator.R;
import edu.gatech.hci.foodnavigator.menu.AppMainMenu;
import edu.gatech.hci.foodnavigator.utilities.ImageMap;

public class FoodTrayActivity extends BaseActivity {
	ImageMap mImageMap;
	String TAG = " @@@ FoodTrayActivity @@@ ";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.food_activity);
		setMainMenu(new AppMainMenu());

		// find the image map in the view
		mImageMap = (ImageMap) findViewById(R.id.map);
		mImageMap.setImageResource(R.drawable.american_table);

		// add a click handler to react when areas are tapped
		mImageMap
				.addOnImageMapClickedHandler(new ImageMap.OnImageMapClickedHandler() {
					@Override
					public void onImageMapClicked(int id, ImageMap imageMap) {
						// when the area is tapped, show the name in a
						// text bubble
						if (mImageMap.isBubbleShown(id)) {
							Intent in = new Intent(FoodTrayActivity.this,
									FoodDetailsActivity.class);

							/* grab the foodId defined as in database */
							String foodId = grabFoodIdByResId(id);
							String resName = getResources().getResourceName(id);

							if (foodId.equals("")) {
								String current = resName.replace(
										getPackageName() + ":id/food_", "");
								Toast.makeText(
										getApplicationContext(),
										"This content is coming soon: "
												+ current, Toast.LENGTH_SHORT)
										.show();

							} else {
								// attach the foodId string in bundle
								in.putExtra("foodId", foodId);
								startActivity(in);
							}
						} else {
							mImageMap.showBubble(id);
						}

					}

					@Override
					public void onBubbleClicked(int id) {
						// react to info bubble for area being tappe
					}
				});
	}

	/*
	 * grab the resource id tied with touch image map and find the corresponding
	 * foodId (entered in db)
	 */
	private String grabFoodIdByResId(int resId) {
		Resources res = getResources();
		String resName = res.getResourceName(resId);
		String val = resName.replace(getPackageName() + ":id/", "");
		int identifier = res.getIdentifier(val, "string", getPackageName());

		String foodId = "";

		try {
			foodId = res.getString(identifier);
		} catch (Exception e) {
			Log.d(TAG,
					" !!! Watch out !!! FoodId not defined in food_id_mapping.xml!");

		}

		Log.d(TAG, "In grabFoodIdByResId - name: " + val + "   index: "
				+ foodId);

		return foodId;
	}
}