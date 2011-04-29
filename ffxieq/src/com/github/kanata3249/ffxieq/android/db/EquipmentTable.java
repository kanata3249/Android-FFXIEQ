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
import com.github.kanata3249.ffxieq.Combination;
import com.github.kanata3249.ffxieq.Equipment;

import android.database.Cursor;
import android.database.sqlite.*;

public class EquipmentTable {
	//
	static final String TABLE_NAME = "Equipment";
	static final String TABLE_NAME_AUGMENT = "Augment";
	static final String TABLE_NAME_COMBINATION = "Combination";
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
	
	public static final String C_Combi_ID = "_id";
	public static final String C_Combi_CombinationID = "CombinationID";
	public static final String C_Combi_Equipments = "Equipments";
	public static final String C_Combi_NumMatches = "NumMatch";
	public static final String C_Combi_Description = "Description";

	public EquipmentTable() { };

	// DA methods
	public Equipment newInstance(FFXIDAO dao, SQLiteDatabase db, long id, int augId) {
		Cursor cursor;
		Equipment newInstance;
		String []columns = { C_Id, C_Name, C_Part, C_Weapon, C_Job, C_Race, C_Level, C_Rare, C_Ex, C_Description};

		cursor = db.query(TABLE_NAME, columns, C_Id + " = '" + id + "'", null, null, null, null, null);
		if (cursor.getCount() < 1) {
			// no match
			cursor.close();
			return null;
		}
		cursor.moveToFirst();
		newInstance = new Equipment(cursor.getLong(cursor.getColumnIndex(C_Id)), cursor.getString(cursor.getColumnIndex(C_Name)),
									cursor.getString(cursor.getColumnIndex(C_Part)), cursor.getString(cursor.getColumnIndex(C_Weapon)),
									cursor.getString(cursor.getColumnIndex(C_Job)), cursor.getString(cursor.getColumnIndex(C_Race)),
									cursor.getInt(cursor.getColumnIndex(C_Level)), cursor.getInt(cursor.getColumnIndex(C_Rare)) != 0,
									cursor.getInt(cursor.getColumnIndex(C_Ex)) != 0, cursor.getString(cursor.getColumnIndex(C_Description)));
		cursor.close();
		
		if (augId != -1) {
			cursor = db.query(TABLE_NAME_AUGMENT, columns, C_Id + " = '" + augId + "'", null, null, null, null, null);
			if (cursor.getCount() < 1) {
				// no match
				cursor.close();
				return null;
			}
			cursor.moveToFirst();
			newInstance.setAugment(augId, cursor.getString(0));
			cursor.close();
		}
		
		String []combi_columns = { C_Combi_CombinationID };
		cursor = db.query(TABLE_NAME_COMBINATION, combi_columns, C_Combi_Equipments + " LIKE '%" + newInstance.getName() + "%'", null, null, null, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			newInstance.setCombinationID(cursor.getLong(cursor.getColumnIndex(C_Combi_CombinationID)));
		}
		cursor.close();

		return newInstance;
	}

	public Cursor getCursor(FFXIDAO dao, SQLiteDatabase db, int part, int race, int job, int level, String[] columns, String orderBy, String filter) {
		Cursor cursor;
		String partStr, jobStr, alljobStr;
		String filterexp;
		
		partStr = dao.getString(FFXIString.PART_DB_MAIN + part);
		jobStr = dao.getString(FFXIString.JOB_DB_WAR + job);
		alljobStr = dao.getString(FFXIString.JOB_DB_ALL);
		filterexp = "";
		if (filter.length() > 0) {
			filterexp = " AND (" + C_Name + " LIKE '%" + filter + "%' OR " + C_Description + " LIKE '%" + filter + "%')";
		}
		
		cursor = db.query(TABLE_NAME, columns,
				C_Part + " LIKE '%" + partStr + "%' AND " +
				C_Level + " <= '" + level + "' AND " +
				"(" + C_Job + " LIKE '%" + jobStr + "%' OR " + C_Job + " = '" + alljobStr + "')" + filterexp,
				null, null, null, orderBy);

		return cursor;
	}

	public Combination newCombinationInstance(FFXIDAO dao, SQLiteDatabase db, long combiId, int numMatches) {
		Cursor cursor;
		Combination newInstance;
		String []columns = { C_Combi_ID, C_Combi_CombinationID, C_Combi_Description };

		cursor = db.query(TABLE_NAME_COMBINATION, columns,
							C_Combi_CombinationID + " = '" + combiId + "' AND " + C_Combi_NumMatches + " = '" + numMatches + "'", null, null, null, null, null);
		if (cursor.getCount() < 1) {
			// no match
			cursor.close();
			return null;
		}
		cursor.moveToFirst();
		newInstance = new Combination(cursor.getLong(cursor.getColumnIndex(C_Combi_ID)), combiId, cursor.getString(cursor.getColumnIndex(C_Combi_Description)));
		cursor.close();

		return newInstance;
	}

	public Combination searchCombinationAndNewInstance(FFXIDAO dao, SQLiteDatabase db, String names[]) {
		Cursor cursor;
		Combination newInstance;
		String []columns = { C_Combi_ID, C_Combi_CombinationID, C_Combi_Description };
		StringBuilder where;

		where = new StringBuilder();
		for (int i = 0; i < names.length; i++) {
			if (i != 0)
				where.append(" AND ");
			where.append(C_Combi_Equipments);
			where.append(" LIKE '%");
			where.append(names[i]);
			where.append("%'");
		}

		cursor = db.query(TABLE_NAME_COMBINATION, columns,
							where.toString(), null, null, null, null, null);
		if (cursor.getCount() < 1) {
			// no match
			cursor.close();
			return null;
		}
		cursor.moveToFirst();
		newInstance = new Combination(cursor.getLong(cursor.getColumnIndex(C_Combi_ID)), cursor.getLong(cursor.getColumnIndex(C_Combi_CombinationID)), cursor.getString(cursor.getColumnIndex(C_Combi_Description)));
		cursor.close();

		return newInstance;
	}
}

