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
import com.github.kanata3249.ffxieq.android.db.MagicTable;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MagicListView extends ListView {
	FFXIDatabase mDao;
	long mSubId;
	String mOrderBy;
	String mFilter;
	long mFilterID;
	String mFilterByType;

	final String [] columns = { MagicTable.C_Id, MagicTable.C_Name, MagicTable.C_Description, MagicTable.C_Memo, MagicTable.C_SubName };
	final int []views = { 0, R.id.Name, R.id.Description, R.id.Memo };

	public MagicListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mSubId = -1;
		mFilter = "";
		mFilterID = -1;
		mOrderBy = MagicTable.C_Name + " ASC";
		mFilterByType = "";
	}

	public boolean setParam(FFXIDAO dao, long subId) {
		MagicListViewAdapter adapter;
		Cursor cursor;
		Parcelable state = onSaveInstanceState();

		mDao = (FFXIDatabase)dao;
		mSubId = subId;
		if (mFilterID != -1) {
			mFilter = ((FFXIEQBaseActivity)getContext()).getSettings().getFilter(mFilterID);
		}
		cursor = mDao.getMagicCursor(subId, columns, mOrderBy, mFilterByType);
		adapter = new MagicListViewAdapter(getContext(), R.layout.magiclistview, cursor, columns, views);
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

	public void setFilterByType(String filterByType) {
		
		if (filterByType.equals(mFilterByType)) {
			return;
		}
		mFilterByType = filterByType;

		updateCursor();
	}

	private void updateCursor() {
		MagicListViewAdapter adapter;

		adapter = (MagicListViewAdapter)getAdapter();
		if (adapter != null) {
			Cursor cursor = mDao.getMagicCursor(mSubId, columns, mOrderBy, mFilterByType);
			adapter.changeCursor(cursor);
		}
	}

	private class MagicListViewAdapter extends SimpleCursorAdapter {

		public MagicListViewAdapter(Context context,
				int textViewResourceId, Cursor c, String[] from, int[] to) {
			super(context, textViewResourceId, c, from, to);

			setViewBinder(new SimpleCursorAdapter.ViewBinder() {
				public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
					int id = view.getId();
					switch (id) {
					case R.id.Name:
						{
							String name, subName;
							
							name = cursor.getString(columnIndex);
							subName = cursor.getString(cursor.getColumnIndex(MagicTable.C_SubName));
							if (subName != null) {
								if (subName.startsWith("#")) {
									((TextView)view).setText(subName.substring(1));
								} else {
									if (subName.length() > 0 && Character.isLetter(subName.charAt(0))) {
										((TextView)view).setText(name + " " + subName);
									} else {
										((TextView)view).setText(name + subName);
									}
								}
							} else {
								((TextView)view).setText(name);
							}
							return true;
						}
					}
	
					return false;
				}
				
			});
		}
	}

	@Override
	public void onDetachedFromWindow() {
		MagicListViewAdapter adapter;

		adapter = ((MagicListViewAdapter)getAdapter());
		if (adapter != null) {
			adapter.changeCursor(null);
		}

		super.onDetachedFromWindow();
	}
}
