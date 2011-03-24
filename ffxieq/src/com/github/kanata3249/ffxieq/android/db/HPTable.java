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

import android.database.Cursor;
import android.database.sqlite.*;

public class HPTable {
	//
	static final String TABLE_NAME = "HP";
	static final String TABLE_NAME_SUB = "HPsub";
	//
	static final String C_Id = "ID";
	static final String C_RaceRank = "RaceRank";
	static final String C_JobRank = "JobRank";
	static final String C_JobLevel = "Lv";
	static final String C_HP = "HP";

	public HPTable() { };

	// DA methods
	public int getHP(SQLiteDatabase db, String racerank, String jobrank, int joblevel, String subjobrank, int subjoblevel) {
		Cursor cursor;
		int value;
		String []columns = { C_HP };

		cursor = db.query(TABLE_NAME, columns,
							C_RaceRank + " = '" + racerank + "' AND " + C_JobRank + " = '" + jobrank + "' AND " + C_JobLevel + " = '" + joblevel + "'",
							null, null, null, null, null);
		
		if (cursor.getCount() < 1) {
			// no matched row in table
			return 0;
		}
		cursor.moveToFirst();
		value = cursor.getInt(0);
		cursor.close();

		if (subjoblevel > 0) {
			cursor = db.query(TABLE_NAME_SUB, columns,
					C_JobRank + " = '" + subjobrank + "' AND " + C_JobLevel + " = '" + subjoblevel + "'",
					null, null, null, null, null);
	
			if (cursor.getCount() < 1) {
				// no matched row in table
				return 0;
			}
			cursor.moveToFirst();
			value += cursor.getInt(0);
			cursor.close();
		}

		return value;
	}
}

