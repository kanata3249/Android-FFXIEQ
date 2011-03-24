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
import com.github.kanata3249.ffxieq.android.db.AtmaTable;
import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class AtmaListView extends ListView {
	FFXIDAO mDao;
	String mOrderBy;

	public AtmaListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mOrderBy = null;
	}

	public boolean setParam(FFXIDAO dao) {
		AtmaListViewAdapter adapter;
		String [] columns = { AtmaTable.C_Id, AtmaTable.C_Name, AtmaTable.C_Description };
		int []views = { 0, R.id.Name, R.id.Description };
		Cursor cursor;

		mDao = dao;
		cursor = dao.getAtmaCursor(columns, mOrderBy);
		adapter = new AtmaListViewAdapter(getContext(), R.layout.atmalistview, cursor, columns, views);
		setAdapter(adapter);
		
		return true;
	}
	
	private class AtmaListViewAdapter extends SimpleCursorAdapter {

		public AtmaListViewAdapter(Context context,
				int textViewResourceId, Cursor c, String[] from, int[] to) {
			super(context, textViewResourceId, c, from, to);
		}
	}

}
