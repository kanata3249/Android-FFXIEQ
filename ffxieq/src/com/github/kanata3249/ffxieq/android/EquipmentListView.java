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
import com.github.kanata3249.ffxieq.R;
import com.github.kanata3249.ffxieq.android.db.EquipmentTable;
import com.github.kanata3249.ffxieq.android.db.FFXIDatabase;

import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
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

	final String [] columns = { EquipmentTable.C_Id, EquipmentTable.C_Name, EquipmentTable.C_Level, EquipmentTable.C_Description, EquipmentTable.C_Job, EquipmentTable.C_Race };
	final int []views = { 0, R.id.Name, R.id.Level, R.id.Description, R.id.Job, R.id.Race };

	public EquipmentListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mFilter = "";
		mFilterID = -1;
		mOrderBy = EquipmentTable.C_Level + " DESC, " + EquipmentTable.C_Name + " ASC";
	}

	public boolean setParam(FFXIDAO dao, int part, int race, int job, int level) {
		EquipmentListViewAdapter adapter;
		Cursor cursor;

		mPart = part;
		mRace = race;
		mJob = job;
		mLevel = level;
		mDao = (FFXIDatabase)dao;
		if (mFilterID != -1) {
			mFilter = ((FFXIEQBaseActivity)getContext()).getSettings().getFilter(mFilterID);
		}
		cursor = mDao.getEquipmentCursor(part, race, job, level, columns, mOrderBy, mFilter);
		adapter = new EquipmentListViewAdapter(getContext(), R.layout.equipmentlistview, cursor, columns, views);
		setAdapter(adapter);
		
		return true;
	}
	
	public String getFilter() {
		return mFilter;
	}

	public void setFilter(String filter) {
		EquipmentListViewAdapter adapter;

		mFilter = filter;
		adapter = (EquipmentListViewAdapter)getAdapter();
		if (adapter != null) {
			Cursor cursor = mDao.getEquipmentCursor(mPart, mRace, mJob, mLevel, columns, mOrderBy, mFilter);
			adapter.changeCursor(cursor);
		}
	}
	public void setFilterByID(long filterid) {
		mFilterID = filterid;
	}

	private class EquipmentListViewAdapter extends SimpleCursorAdapter {

		public EquipmentListViewAdapter(Context context,
				int textViewResourceId, Cursor c, String[] from, int[] to) {
			super(context, textViewResourceId, c, from, to);
		}
	}


}
