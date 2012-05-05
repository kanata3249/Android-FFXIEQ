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
import com.github.kanata3249.ffxieq.BlueMagic;
import android.database.Cursor;
import android.database.sqlite.*;

public class BlueMagicTable {
	//
	static final String TABLE_NAME = "BlueMagic";
	//
	public static final String C_Id = "_id";
	public static final String C_Name = "Name";
	public static final String C_Level = "Lv";
	public static final String C_BP = "BP";
	public static final String C_Description = "Description";
	public static final String C_SetBonusPoint = "SetBonusPoint";
	
	public BlueMagicTable() { };

	// DA methods
	public BlueMagic newInstance(FFXIDAO dao, SQLiteDatabase db, long id) {
		Cursor cursor;
		BlueMagic newInstance;
		String []columns = { C_Id, C_Name, C_Description, C_Level, C_BP };

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
		newInstance = new BlueMagic(cursor.getLong(cursor.getColumnIndex(C_Id)),
									cursor.getLong(cursor.getColumnIndex(C_Level)),
									cursor.getLong(cursor.getColumnIndex(C_BP)),
									cursor.getString(cursor.getColumnIndex(C_Name)),
									cursor.getString(cursor.getColumnIndex(C_Description)));
		cursor.close();
		
		return newInstance;
	}
	
	public Cursor getCursor(FFXIDAO dao, SQLiteDatabase db, String[] columns, String orderBy) {
		Cursor cursor;
		
		try {
			cursor = db.query(TABLE_NAME, columns, null, null, null, null, orderBy);
		} catch (SQLiteException e) {
			cursor = null;
		}

		return cursor;
	}
}

