/*
   Copyright 2012 kanata3249

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
import com.github.kanata3249.ffxieq.android.db.FFXIDatabase;
import com.github.kanata3249.ffxieq.android.db.VWAtmaTable;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class VWAtmaListView extends ListView {
	FFXIDatabase mDao;
	String mOrderBy;
	String mFilter;
	long mFilterID;
	long mSubID;

	final String [] columns = { VWAtmaTable.C_Id, VWAtmaTable.C_Name, VWAtmaTable.C_Description };
	final int []views = { 0, R.id.Name, R.id.Description };

	public VWAtmaListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mOrderBy = null;
		mFilter = "";
		mFilterID = -1;
		mSubID = -1;
	}

	public boolean setParam(FFXIDAO dao, long subID) {
		VWAtmaListViewAdapter adapter;
		Cursor cursor;
		Parcelable state = onSaveInstanceState();

		mDao = (FFXIDatabase)dao;
		mSubID = subID;
		if (mFilterID != -1) {
			mFilter = ((FFXIEQBaseActivity)getContext()).getSettings().getFilter(mFilterID);
		}
		cursor = mDao.getVWAtmaCursor(mSubID, columns, mOrderBy, mFilter);
		if (getAdapter() != null) {
			((VWAtmaListViewAdapter)getAdapter()).changeCursor(cursor);
		} else {
			adapter = new VWAtmaListViewAdapter(getContext(), R.layout.vwatmalistview, cursor, columns, views);
			setAdapter(adapter);
		}
		
		onRestoreInstanceState(state);
		return true;
	}
	
	private class VWAtmaListViewAdapter extends SimpleCursorAdapter {

		public VWAtmaListViewAdapter(Context context,
				int textViewResourceId, Cursor c, String[] from, int[] to) {
			super(context, textViewResourceId, c, from, to);
		}
	}

	public String getFilter() {
		return mFilter;
	}

	public void setFilter(String filter) {
		VWAtmaListViewAdapter adapter;

		mFilter = filter;
		adapter = (VWAtmaListViewAdapter)getAdapter();
		if (adapter != null) {
			Cursor cursor = mDao.getVWAtmaCursor(mSubID, columns, mOrderBy, mFilter);
			adapter.changeCursor(cursor);
		}
	}
	public void setFilterByID(long filterid) {
		mFilterID = filterid;
	}

	@Override
	protected void onDetachedFromWindow() {
		VWAtmaListViewAdapter adapter;

		adapter = ((VWAtmaListViewAdapter)getAdapter());
		if (adapter != null) {
			adapter.changeCursor(null);
		}

		super.onDetachedFromWindow();
	}
}
