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

import android.content.Context;
import android.database.Cursor;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class EquipmentListView extends ListView {
	FFXIDatabase mDao;
	String mOrderBy;
	String mFilter;
	int mPart;
	int mRace;
	int mJob;
	int mLevel;
	long mFilterID;
	String mFilterByType;

	final String [] columns = { EquipmentTable.C_Id, EquipmentTable.C_Name, EquipmentTable.C_Level, EquipmentTable.C_Description, EquipmentTable.C_Job, EquipmentTable.C_Race, EquipmentTable.C_Ex, EquipmentTable.C_Rare };
	final int []views = { 0, R.id.Name, R.id.Level, R.id.Description, R.id.Job, R.id.Race, R.id.Ex, R.id.Rare };

	public EquipmentListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mFilter = "";
		mFilterID = -1;
		mOrderBy = EquipmentTable.C_ItemLevel + " DESC, " + EquipmentTable.C_Name + " ASC";
		mFilterByType = "";
	}

	public boolean setParam(FFXIDAO dao, int part, int race, int job, int level) {
		EquipmentListViewAdapter adapter;
		Cursor cursor;
		Parcelable state = onSaveInstanceState();

		mPart = part;
		mRace = race;
		mJob = job;
		mLevel = level;
		mDao = (FFXIDatabase)dao;
		if (mFilterID != -1) {
			mFilter = ((FFXIEQBaseActivity)getContext()).getSettings().getFilter(mFilterID);
		}
		cursor = mDao.getEquipmentCursor(part, race, job, level, columns, mOrderBy, mFilter, mFilterByType);
		adapter = new EquipmentListViewAdapter(getContext(), R.layout.equipmentlistview, cursor, columns, views);
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
			order = EquipmentTable.C_Name + " ASC, " + EquipmentTable.C_ItemLevel;
		else
			order = EquipmentTable.C_ItemLevel + " DESC, " + EquipmentTable.C_Name + " ASC";
		if (!mOrderBy.equals(order)) {
			mOrderBy = order;

			updateCursor();
		}
	}

	public String []getAvailableWeaponTypes() {
		return mDao.getAvailableWeaponTypes(mPart, mRace, mJob, mLevel, mFilter);
	}
	
	public void setFilterByType(String filterByType) {
		
		if (filterByType.equals(mFilterByType)) {
			return;
		}
		mFilterByType = filterByType;

		updateCursor();
	}

	private void updateCursor() {
		EquipmentListViewAdapter adapter;

		adapter = (EquipmentListViewAdapter)getAdapter();
		if (adapter != null) {
			Cursor cursor = mDao.getEquipmentCursor(mPart, mRace, mJob, mLevel, columns, mOrderBy, mFilter, mFilterByType);
			adapter.changeCursor(cursor);
		}
	}

	private class EquipmentListViewAdapter extends SimpleCursorAdapter {

		public EquipmentListViewAdapter(Context context,
				int textViewResourceId, Cursor c, String[] from, int[] to) {
			super(context, textViewResourceId, c, from, to);
			
			setViewBinder(new SimpleCursorAdapter.ViewBinder() {
				public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
					int id = view.getId();
					switch (id) {
					case R.id.Ex:
					case R.id.Rare:
						if (cursor.getInt(columnIndex) == 0) {
							view.setVisibility(INVISIBLE);
						} else {
							view.setVisibility(VISIBLE);
						}
						return true;
					}

					return false;
				}
				
			});
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		EquipmentListViewAdapter adapter;

		adapter = ((EquipmentListViewAdapter)getAdapter());
		if (adapter != null) {
			adapter.changeCursor(null);
		}

		super.onDetachedFromWindow();
	}
}
