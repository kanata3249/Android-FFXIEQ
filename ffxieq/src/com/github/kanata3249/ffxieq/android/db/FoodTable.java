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
import com.github.kanata3249.ffxieq.Food;

import android.database.Cursor;
import android.database.sqlite.*;

public class FoodTable {
	//
	static final String TABLE_NAME = "Food";
	//
	public static final String C_Id = "_id";
	public static final String C_Name = "Name";
	public static final String C_Type = "Type";
	public static final String C_Description = "Description";
	
	public FoodTable() { };

	// DA methods
	public Food newInstance(FFXIDAO dao, SQLiteDatabase db, long id) {
		Cursor cursor;
		Food newInstance;
		String []columns = { C_Id, C_Name, C_Description};

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
		newInstance = new Food(cursor.getLong(cursor.getColumnIndex(C_Id)), cursor.getString(cursor.getColumnIndex(C_Name)),
									cursor.getString(cursor.getColumnIndex(C_Description)));
		cursor.close();
		
		return newInstance;
	}

	public Cursor getCursor(FFXIDAO dao, SQLiteDatabase db, String[] columns, String orderBy, String filter, String foodType) {
		Cursor cursor;
		String filterexp, typeexp, exp;
		
		filterexp = "";
		if (filter.length() > 0) {
			filterexp = "(" + C_Name + " LIKE '%" + filter + "%' OR " + C_Description + " LIKE '%" + filter + "%')";
		}
		typeexp = "";
		if (foodType.length() > 0) {
			typeexp = "(" + C_Type + " LIKE '%" + foodType + "%')";
		}
		exp = "";
		if (typeexp.length() > 0) {
			exp = typeexp;
		}
		if (filterexp.length() > 0) {
			if (exp.length() > 0)
				exp = exp + " AND ";
			exp = exp + filterexp;
		}
		if (exp.length() == 0) {
			exp = null;
		}
		
		
		try {
			cursor = db.query(TABLE_NAME, columns, exp, null, null, null, orderBy);
		} catch (SQLiteException e) {
			cursor = null;
		}

		return cursor;
	}
	
	public String []getAvailableFoodTypes(FFXIDAO dao, SQLiteDatabase db, String filter) {
		Cursor cursor;
		String filterexp;
		String result[];
		String columns[] = { C_Type };
		
		filterexp = null;
		if (filter.length() > 0) {
			filterexp = "(" + C_Name + " LIKE '%" + filter + "%' OR " + C_Description + " LIKE '%" + filter + "%')";
		}
		
		result = null;
		try {
			cursor = db.query(TABLE_NAME, columns, filterexp, null, C_Type, null, C_Type);
			
			if (cursor.getCount() > 0) {
				result = new String[cursor.getCount()];
				
				cursor.moveToFirst();
				for (int i = 0; i < result.length; i++) {
					result[i] = cursor.getString(cursor.getColumnIndex(C_Type));
					cursor.moveToNext();
				}
			}
			
			cursor.close();
		} catch (SQLiteException e) {
		}

		return result;
	}
}

