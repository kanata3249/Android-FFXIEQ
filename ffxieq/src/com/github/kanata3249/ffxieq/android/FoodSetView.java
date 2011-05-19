/*
   Copyright 2011 kanata3249

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.github.kanata3249.ffxieq.android;

import com.github.kanata3249.ffxi.FFXIDAO;
import com.github.kanata3249.ffxieq.FFXICharacter;
import com.github.kanata3249.ffxieq.Food;
import com.github.kanata3249.ffxieq.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FoodSetView extends ListView {
	FoodInfo mFoods[];
	FFXIDAO mDao;
	
	public FoodSetView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public boolean bindFFXICharacter(FFXICharacter charinfo) {
		FoodSelectorAdapter adapter;

		mDao = FFXICharacter.getDao();
		mFoods = new FoodInfo[1];
		for (int i = 0; i < mFoods.length; i++) {
			Food eq = charinfo.getFood(i);
			if (eq == null) {
				mFoods[i] = new FoodInfo(-1, "");
			} else {
				mFoods[i] = new FoodInfo(eq.getId(), eq.getName());
			}
		}
		adapter = new FoodSelectorAdapter(getContext(), R.layout.foodsetview, mFoods);
		setAdapter(adapter);
		
		return true;
	}
	
	private class FoodInfo {
		long mID;
		String mName;

		public FoodInfo(long id, String name) { mID = id; mName = name;};

		public String toString() {
			if (mID > 0) {
				return mName;
			}
			return "";
		}
	}
	private class FoodSelectorAdapter extends ArrayAdapter<FoodInfo> {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return super.getView(position, convertView, parent);
		}

		public FoodSelectorAdapter(Context context,
				int textViewResourceId, FoodInfo[] objects) {
			super(context, textViewResourceId, objects);
		}


	}
	
	public long getItemId(int position) {
		return mFoods[position].mID;
	}
}
