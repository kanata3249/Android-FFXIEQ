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

public class JobTraitTable {
	//
	static final String TABLE_NAME = "JobTrait";
	//
	public static final String C_Id = "_id";
	public static final String C_Name = "Name";
	public static final String C_Job = "Job";
	public static final String C_Level = "Level";
	public static final String C_OriginalDescription = "DescriptionOrg";
	public static final String C_Description = "Description";

	public JobTraitTable() { };

	// DA methods
	public JobTrait newInstance(FFXIDAO dao, SQLiteDatabase db, long id) {
		Cursor cursor;
		JobTrait newInstance;
		String []columns = { C_Id, C_Name, C_Job, C_Level, C_Description };

		cursor = db.query(TABLE_NAME, columns, C_Id + " = '" + id + "'", null, null, null, null, null);
		if (cursor.getCount() < 1) {
			// no match
			return null;
		}
		cursor.moveToFirst();
		newInstance = new JobTrait(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4));
		cursor.close();
		
		return newInstance;
	}

	public JobTrait[] getJobTraits(FFXIDAO dao, SQLiteDatabase db, String job, int level) {
		Cursor cursor;
		String []columns = { C_Id, C_Name, C_Job, C_Level, C_Description };
		JobTrait [] result;
		
		try {
			cursor = db.query(TABLE_NAME, columns, C_Job + " = '" + job + "' AND " + C_Level + " <= '" + level + "'", null, null, null, null, null);
		} catch (SQLiteException e) {
			return new JobTrait[0];
		}
		if (cursor.getCount() < 1) {
			// no match
			cursor.close();
			return new JobTrait[0];
		}
		cursor.moveToFirst();
		result = new JobTrait[cursor.getCount()];
		for (int i = 0; i < result.length; i++) {
			result[i] = new JobTrait(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4));
			cursor.moveToNext();
		}
		cursor.close();

		return result;
	}

	public Cursor getCursor(FFXIDAO dao, SQLiteDatabase db, String[] columns, String orderBy) {
		Cursor cursor;
		
		cursor = db.query(TABLE_NAME, columns,
				null,
				null, null, null, orderBy);

		return cursor;
	}
}

