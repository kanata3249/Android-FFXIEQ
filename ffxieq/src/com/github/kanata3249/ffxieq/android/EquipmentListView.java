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

import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class EquipmentListView extends ListView {
	FFXIDAO mDao;
	String mOrderBy;

	public EquipmentListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mOrderBy = EquipmentTable.C_Level + " DESC, " + EquipmentTable.C_Name + " ASC";
	}

	public boolean setParam(FFXIDAO dao, int part, int race, int job, int level) {
		EquipmentListViewAdapter adapter;
		String [] columns = { EquipmentTable.C_Id, EquipmentTable.C_Name, EquipmentTable.C_Level, EquipmentTable.C_Description, EquipmentTable.C_Job, EquipmentTable.C_Race };
		int []views = { 0, R.id.Name, R.id.Level, R.id.Description, R.id.Job, R.id.Race };
		Cursor cursor;

		mDao = dao;
		cursor = dao.getEquipmentCursor(part, race, job, level, columns, mOrderBy);
		adapter = new EquipmentListViewAdapter(getContext(), R.layout.equipmentlistview, cursor, columns, views);
		setAdapter(adapter);
		
		return true;
	}
	
	private class EquipmentListViewAdapter extends SimpleCursorAdapter {

		public EquipmentListViewAdapter(Context context,
				int textViewResourceId, Cursor c, String[] from, int[] to) {
			super(context, textViewResourceId, c, from, to);
		}
	}

}
