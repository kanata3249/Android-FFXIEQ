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
import com.github.kanata3249.ffxieq.JobTrait;

import android.database.Cursor;
import android.database.sqlite.*;

public class MeritPointTable {
	//
	static final String TABLE_NAME = "MeritPoint";
	//
	public static final String C_Id = "_id";
	public static final String C_Title = "Title";
	public static final String C_Job = "Job";
	public static final String C_Description1 = "Description1";
	public static final String C_Description2 = "Description2";
	public static final String C_Description3 = "Description3";
	public static final String C_Description4 = "Description4";
	public static final String C_Description5 = "Description5";
	
	public MeritPointTable() { };

	// DA methods
	public JobTrait newInstance(FFXIDAO dao, SQLiteDatabase db, long id, int level) {
		Cursor cursor;
		JobTrait newInstance;
		String []columns = { C_Id, C_Title, C_Job, C_Description1, C_Description2, C_Description3, C_Description4, C_Description5 };

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
		newInstance = new JobTrait(cursor.getLong(cursor.getColumnIndex(C_Id)), cursor.getString(cursor.getColumnIndex(C_Title)),
									cursor.getString(cursor.getColumnIndex(C_Job)), 75,
									cursor.getString(cursor.getColumnIndex(C_Description1) + (level - 1)));
		cursor.close();
		
		return newInstance;
	}
	
	public String[] getJobSpecificMeritPointItems(FFXIDAO dao, SQLiteDatabase db, String job, int category) {
		Cursor cursor;
		String list[], result[];
		String columns[] = { C_Id, C_Title, C_Job };
		int max, count;

		try {
			cursor = db.query(TABLE_NAME, columns, C_Job + " = '" + job + "'", null, null, null, "_id");
		} catch (SQLiteException e) {
			return null;
		}
		max = cursor.getCount(); 
		if (max < 1) {
			// no match
			cursor.close();
			return null;
		}
		cursor.moveToFirst();
		list = new String[max];
		count = 0;
		for (int i = 0; i < cursor.getCount(); i++) {
			int item_category;
			
			item_category = (int)(cursor.getLong(cursor.getColumnIndex(C_Id)) % 100) < 10 ? 1 : 2;
			if (category == item_category) {
				list[count++] = cursor.getString(cursor.getColumnIndex(C_Title));
			}
			cursor.moveToNext();
		}
		if (count == 0) {
			cursor.close();
			return null;
		}
		result = new String[count];
		for (int i = 0; i < count; i++) {
			result[i] = list[i];
		}
		cursor.close();

		return result;
	}

	public long[] getJobSpecificMeritPointItemIds(FFXIDAO dao, SQLiteDatabase db, String job, int category) {
		Cursor cursor;
		long list[], result[];
		String columns[] = { C_Id, C_Title, C_Job };
		int max, count;

		try {
			cursor = db.query(TABLE_NAME, columns, C_Job + " = '" + job + "'", null, null, null, "_id");
		max = cursor.getCount(); 
		} catch (SQLiteException e) {
			return null;
		}
		if (max < 1) {
			// no match
			cursor.close();
			return null;
		}
		cursor.moveToFirst();
		list = new long[max];
		count = 0;
		for (int i = 0; i < cursor.getCount(); i++) {
			int item_category;
			
			item_category = (int)(cursor.getLong(cursor.getColumnIndex(C_Id)) % 100) < 10 ? 1 : 2;
			if (category == item_category) {
				list[count++] = cursor.getLong(cursor.getColumnIndex(C_Id));
			}
			cursor.moveToNext();
		}
		if (count == 0) {
			cursor.close();
			return null;
		}
		result = new long[count];
		for (int i = 0; i < count; i++) {
			result[i] = list[i];
		}
		cursor.close();

		return result;
	}
}

