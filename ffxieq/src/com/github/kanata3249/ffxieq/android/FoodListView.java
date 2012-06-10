/*
   Copyright 2011-2012 kanata3249

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
import com.github.kanata3249.ffxieq.R;
import com.github.kanata3249.ffxieq.android.db.EquipmentTable;
import com.github.kanata3249.ffxieq.android.db.FFXIDatabase;
import com.github.kanata3249.ffxieq.android.db.FoodTable;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class FoodListView extends ListView {
	FFXIDatabase mDao;
	String mOrderBy;
	String mFilter;
	long mFilterID;
	String mFilterByType;

	final String [] columns = { FoodTable.C_Id, FoodTable.C_Name, FoodTable.C_Description };
	final int []views = { 0, R.id.Name, R.id.Description };

	public FoodListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mFilter = "";
		mFilterID = -1;
		mOrderBy = FoodTable.C_Name + " ASC";
		mFilterByType = "";
	}

	public boolean setParam(FFXIDAO dao) {
		FoodListViewAdapter adapter;
		Cursor cursor;
		Parcelable state = onSaveInstanceState();

		mDao = (FFXIDatabase)dao;
		if (mFilterID != -1) {
			mFilter = ((FFXIEQBaseActivity)getContext()).getSettings().getFilter(mFilterID);
		}
		cursor = mDao.getFoodsCursor(columns, mOrderBy, mFilter, mFilterByType);
		adapter = new FoodListViewAdapter(getContext(), R.layout.foodlistview, cursor, columns, views);
		setAdapter(adapter);
		
		onRestoreInstanceState(state);
		return true;
	}
	
	public String getFilter() {
		return mFilter;
	}

	public void setFilter(String filter) {
		if (mFilter.equals(filter))
			return;
		mFilter = filter;
		updateCursor();
	}
	public void setFilterByID(long filterid) {
		mFilterID = filterid;
	}
	
	public void setOrderByName(boolean orderByName) {
		String order;
		
		if (orderByName)
			order = EquipmentTable.C_Name + " ASC, " + EquipmentTable.C_Level;
		else
			order = EquipmentTable.C_Level + " DESC, " + EquipmentTable.C_Name + " ASC";
		if (!mOrderBy.equals(order)) {
			mOrderBy = order;

			updateCursor();
		}
	}

	public String []getAvailableFoodTypes() {
		return mDao.getAvailableFoodTypes(mFilter);
	}
	
	public void setFilterByType(String filterByType) {
		
		if (filterByType.equals(mFilterByType)) {
			return;
		}
		mFilterByType = filterByType;

		updateCursor();
	}

	private void updateCursor() {
		FoodListViewAdapter adapter;

		adapter = (FoodListViewAdapter)getAdapter();
		if (adapter != null) {
			Cursor cursor = mDao.getFoodsCursor(columns, mOrderBy, mFilter, mFilterByType);
			adapter.changeCursor(cursor);
		}
	}

	private class FoodListViewAdapter extends SimpleCursorAdapter {

		public FoodListViewAdapter(Context context,
				int textViewResourceId, Cursor c, String[] from, int[] to) {
			super(context, textViewResourceId, c, from, to);
		}
	}


	@Override
	public void onDetachedFromWindow() {
		FoodListViewAdapter adapter;

		adapter = ((FoodListViewAdapter)getAdapter());
		if (adapter != null) {
			adapter.changeCursor(null);
		}

		super.onDetachedFromWindow();
	}
}
