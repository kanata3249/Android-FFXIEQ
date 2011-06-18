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
package com.github.kanata3249.ffxieq.android.db;

import com.github.kanata3249.ffxi.FFXIDAO;
import com.github.kanata3249.ffxieq.Magic;

import android.database.Cursor;
import android.database.sqlite.*;

public class MagicTable {
	//
	static final String TABLE_NAME = "Magic";
	//
	public static final String C_Id = "_id";
	public static final String C_Type = "Type";
	public static final String C_Name = "Name";
	public static final String C_SubId = "SubId";
	public static final String C_SubName = "SubName";
	public static final String C_Description = "Description";
	public static final String C_Memo = "Memo";
	
	public MagicTable() { };

	// DA methods
	public Magic newInstance(FFXIDAO dao, SQLiteDatabase db, long id) {
		Cursor cursor;
		Magic newInstance;
		String []columns = { C_Id, C_Name, C_Description, C_SubName, C_SubId, C_Memo };

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
		String name = cursor.getString(cursor.getColumnIndex(C_Name));
		String subname =  cursor.getString(cursor.getColumnIndex(C_SubName));
		if (subname != null) {
			name = name + subname;
			if (subname.length() > 0 && subname.startsWith("#")) {
				name = subname.substring(1);
			}
		}
		newInstance = new Magic(cursor.getLong(cursor.getColumnIndex(C_Id)), cursor.getLong(cursor.getColumnIndex(C_SubId)),
									name,
									cursor.getString(cursor.getColumnIndex(C_Description)), cursor.getString(cursor.getColumnIndex(C_Memo)));
		cursor.close();
		
		return newInstance;
	}
	
	public Cursor getCursor(FFXIDAO dao, SQLiteDatabase db, long subId, String[] columns, String orderBy, String type) {
		Cursor cursor;
		
		try {
			if (subId < 0) {
				String typeexp = null;
				if (type != null && type.length() > 0) {
					typeexp = "(" + C_Type + " LIKE '%" + type + "%')";
				}
				
				cursor = db.query(TABLE_NAME, columns,
									typeexp,
									null, C_SubId, null, orderBy);
			} else {
				cursor = db.query(TABLE_NAME, columns,
									C_SubId + " = '" + subId + "'",
									null, null, null, orderBy);
			}
		} catch (SQLiteException e) {
			cursor = null;
		}

		return cursor;
	}
}

