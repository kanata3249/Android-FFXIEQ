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

import com.github.kanata3249.ffxi.status.JobLevelAndRace;
import com.github.kanata3249.ffxi.status.StatusType;

import android.database.Cursor;
import android.database.sqlite.*;

public class JobRankTable {
	//
	static final String TABLE_NAME = "JobRank";
	//
	static final String C_Id = "_id";

	public JobRankTable() { };

	// DA methods
	public String[][] buildJobRankTable(SQLiteDatabase db) {
		Cursor cursor;
		String []columns = {
				StatusType.HP.name(),
				StatusType.MP.name(),
				StatusType.STR.name(),
				StatusType.DEX.name(),
				StatusType.VIT.name(),
				StatusType.AGI.name(),
				StatusType.INT.name(),
				StatusType.MND.name(),
				StatusType.CHR.name(),

				StatusType.SKILL_HANDTOHAND.name(),
				StatusType.SKILL_DAGGER.name(),
				StatusType.SKILL_SWORD.name(),
				StatusType.SKILL_GREATSWORD.name(),
				StatusType.SKILL_AXE.name(),
				StatusType.SKILL_GREATAXE.name(),
				StatusType.SKILL_SCYTH.name(),
				StatusType.SKILL_POLEARM.name(),
				StatusType.SKILL_KATANA.name(),
				StatusType.SKILL_GREATKATANA.name(),
				StatusType.SKILL_CLUB.name(),
				StatusType.SKILL_STAFF.name(),
				StatusType.SKILL_ARCHERY.name(),
				StatusType.SKILL_MARKSMANSHIP.name(),
				StatusType.SKILL_THROWING.name(),
				StatusType.SKILL_GUARDING.name(),
				StatusType.SKILL_EVASION.name(),
				StatusType.SKILL_SHIELD.name(),
				StatusType.SKILL_PARRYING.name(),

				StatusType.SKILL_DIVINE_MAGIC.name(),
				StatusType.SKILL_HEALING_MAGIC.name(),
				StatusType.SKILL_ENCHANCING_MAGIC.name(),
				StatusType.SKILL_ENFEEBLING_MAGIC.name(),
				StatusType.SKILL_ELEMENTAL_MAGIC.name(),
				StatusType.SKILL_DARK_MAGIC.name(),
				StatusType.SKILL_SINGING.name(),
				StatusType.SKILL_STRING_INSTRUMENT.name(),
				StatusType.SKILL_WIND_INSTRUMENT.name(),
				StatusType.SKILL_NINJUTSU.name(),
				StatusType.SKILL_SUMMONING.name(),
				StatusType.SKILL_BLUE_MAGIC.name(),
		};

		try {
			cursor = db.query(TABLE_NAME, columns, null, null, null, null, C_Id, null);
		} catch (SQLiteException e) {
			return null;
		}

		if (cursor.getCount() != JobLevelAndRace.JOB_MAX) {
			return null;
		}
		cursor.moveToFirst();

		String [][] result = new String[JobLevelAndRace.JOB_MAX][];
		
		for (int job = 0; job < JobLevelAndRace.JOB_MAX; job++) {
			result[job] = new String[columns.length];
			
			for (int i = 0; i < columns.length; i++) {
				result[job][i] = cursor.getString(i);
			}
			cursor.moveToNext();
		}
		cursor.close();

		return result;
	}
}

