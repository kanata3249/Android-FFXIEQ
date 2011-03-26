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
package com.github.kanata3249.ffxi;

import android.database.Cursor;

import com.github.kanata3249.ffxi.status.StatusType;
import com.github.kanata3249.ffxieq.Atma;
import com.github.kanata3249.ffxieq.Equipment;
import com.github.kanata3249.ffxieq.JobTrait;

public interface FFXIDAO {
	// DA methods
	public int getHP(int race, int job, int joblevel, int subjob, int subjoblevel);
	public int getMP(int race, int job, int joblevel, int subjob, int subjoblevel);
	public int getStatus(StatusType type, int race, int job, int joblevel, int subjob, int subjoblevel);
	public int getSkillCap(StatusType type, int job, int joblevel, int subjob, int subjoblevel);

	public String getString(int id);		// ids are defined in FFXIString class
	
	public Equipment instanciateEquipment(long id);
	public Atma instanciateAtma(long id);
	public JobTrait[] getJobTraits(int job, int level);
	public Cursor getEquipmentCursor(int part, int race, int job, int level, String[] columns, String orderby);		// TODO android depenent
	public Cursor getAtmaCursor(String[] columns, String orderby);		// TODO android depenent
}
