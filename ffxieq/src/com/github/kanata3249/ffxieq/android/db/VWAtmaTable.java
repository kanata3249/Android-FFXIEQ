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
package com.github.kanata3249.ffxieq.android.db;

import com.github.kanata3249.ffxi.FFXIDAO;
import com.github.kanata3249.ffxieq.Atma;
import android.database.Cursor;
import android.database.sqlite.*;

public class VWAtmaTable {
	//
	static final String TABLE_NAME = "VWAtma";
	//
	public static final String C_Id = "_id";
	public static final String C_SubId = "subId";
	public static final String C_Level = "Lv";
	public static final String C_Name = "Name";
	public static final String C_Description = "Description";

	public VWAtmaTable() { };

	// DA methods
	public Atma newInstance(FFXIDAO dao, SQLiteDatabase db, long id) {
		Cursor cursor;
		Atma newInstance;
		String []columns = { C_Id, C_SubId, C_Name, C_Description };

		try {
			cursor = db.query(TABLE_NAME, columns, C_Id + " = '" + id + "'", null, null, null, null, null);
		} catch (SQLiteException e) {
			return null;
		}
		if (cursor.getCount() < 1) {
			// no match
			cursor.close();
			return null;
		}
		cursor.moveToFirst();
		newInstance = new Atma(cursor.getLong(0), cursor.getLong(1), cursor.getString(2), cursor.getString(3));
		cursor.close();
		
		return newInstance;
	}

	public Cursor getCursor(FFXIDAO dao, SQLiteDatabase db, long subID, String[] columns, String orderBy, String filter) {
		Cursor cursor;
		String filterexp;

		filterexp = "";
		if (subID != -1) {
			filterexp = "(" + C_SubId + " = " + subID+ ")";
		} else {
			filterexp = "(" + C_Level + " = " + 15 + ")";
		}
		if (filter.length() > 0) {
			filterexp += " AND (" + C_Name + " LIKE '%" + filter + "%' OR " + C_Description + " LIKE '%" + filter + "%')";
		}

		try {
			cursor = db.query(TABLE_NAME, columns,
					filterexp,
					null, null, null, orderBy);
		} catch (SQLiteException e) {
			return null;
		}

		return cursor;
	}
}

