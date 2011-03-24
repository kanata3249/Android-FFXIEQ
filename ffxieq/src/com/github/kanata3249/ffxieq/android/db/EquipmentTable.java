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
import com.github.kanata3249.ffxi.FFXIString;
import com.github.kanata3249.ffxieq.Equipment;

import android.database.Cursor;
import android.database.sqlite.*;

public class EquipmentTable {
	//
	static final String TABLE_NAME = "Equipment";
	static final String TABLE_NAME_AUGMENT = "Augment";
	//
	public static final String C_Id = "_id";
	public static final String C_BaseId = "BaseEquipmentID";
	public static final String C_Name = "Name";
	public static final String C_Part = "Part";
	public static final String C_Weapon = "Weapon";
	public static final String C_Job = "Job";
	public static final String C_Race = "Race";
	public static final String C_Level = "Lv";
	public static final String C_Rare = "Rare";
	public static final String C_Ex = "Ex";
	public static final String C_OriginalDescription = "DescriptionOrg";
	public static final String C_Description = "Description";

	public EquipmentTable() { };

	// DA methods
	public Equipment newInstance(FFXIDAO dao, SQLiteDatabase db, long id, int augId) {
		Cursor cursor;
		Equipment newInstance;
		String []columns = { C_Id, C_Name, C_Part, C_Weapon, C_Job, C_Race, C_Level, C_Rare, C_Ex, C_Description };

		cursor = db.query(TABLE_NAME, columns, C_Id + " = '" + id + "'", null, null, null, null, null);
		if (cursor.getCount() < 1) {
			// no match
			return null;
		}
		cursor.moveToFirst();
		newInstance = new Equipment(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6), cursor.getInt(7) != 0, cursor.getInt(8) != 0, cursor.getString(9));
		cursor.close();
		
		if (augId != -1) {
			cursor = db.query(TABLE_NAME_AUGMENT, columns, C_Id + " = '" + augId + "'", null, null, null, null, null);
			if (cursor.getCount() < 1) {
				// no match
				return null;
			}
			cursor.moveToFirst();
			newInstance.setAugment(augId, cursor.getString(0));
			cursor.close();
		}

		return newInstance;
	}

	public Cursor getCursor(FFXIDAO dao, SQLiteDatabase db, int part, int race, int job, int level, String[] columns, String orderBy) {
		Cursor cursor;
		String partStr, jobStr, alljobStr;
		
		partStr = dao.getString(FFXIString.PART_DB_MAIN + part);
		jobStr = dao.getString(FFXIString.JOB_DB_WAR + job);
		alljobStr = dao.getString(FFXIString.JOB_DB_ALL);
		
		cursor = db.query(TABLE_NAME, columns,
				C_Part + " LIKE '%" + partStr + "%' AND " +
				C_Level + " <= '" + level + "' AND " +
				"(" + C_Job + " LIKE '%" + jobStr + "%' OR " + C_Job + " = '" + alljobStr + "')",
				null, null, null, orderBy);

		return cursor;
	}
}

