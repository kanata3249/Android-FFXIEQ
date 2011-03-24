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
package com.github.kanata3249.ffxieq;

import java.io.Serializable;

import com.github.kanata3249.ffxi.status.*;

// These status calculation fomula is based on the information from http://www.geocities.jp/lc7385/ffxieq/statuscalc.html
public class JobAndRace extends StatusModifier implements Serializable  {
	private static final long serialVersionUID = 1L;

	// These values should be equal to StatusType.??
	static final int HP = 0;
	static final int MP = 1;
	static final int STR = 2;
	static final int DEX = 3;
	static final int VIT = 4;
	static final int AGI = 5;
	static final int INT = 6;
	static final int MND = 7;
	static final int CHR = 8;
	

	public JobAndRace() {
		super();
		
		loadDefaultValues();
	}
	
	@Override
	protected void loadDefaultValues() {
		super.loadDefaultValues();
		// fill all skill value to 999
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_HANDTOHAND);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_DAGGER);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_SWORD);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_GREATSWORD);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_AXE);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_GREATAXE);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_SCYTH);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_POLEARM);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_KATANA);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_GREATKATANA);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_CLUB);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_STAFF);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_ARCHERY);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_MARKSMANSHIP);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_THROWING);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_GUARDING);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_EVASION);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_SHIELD);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_PARRYING);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_DIVINE_MAGIC);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_HEALING_MAGIC);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_ENCHANCING_MAGIC);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_ENFEEBLING_MAGIC);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_ELEMENTAL_MAGIC);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_DARK_MAGIC);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_SINGING);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_STRING_INSTRUMENT);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_WIND_INSTRUMENT);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_NINJUTSU);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_SUMMONING);
		super.setStatus(new StatusValue(999, 0, 0), StatusType.SKILL_BLUE_MAGIC);
	}

	// IStatus
	public StatusValue getStatus(JobLevelAndRace level, StatusType type) {
		switch (type) {
		case HP:
			return calcHP(level);
		case MP:
			return calcMP(level);
		case STR:
		case DEX:
		case VIT:
		case AGI:
		case INT:
		case MND:
		case CHR:
			return calcStatus(level, type);
		case SKILL_HANDTOHAND:
		case SKILL_DAGGER:
		case SKILL_SWORD:
		case SKILL_GREATSWORD:
		case SKILL_AXE:
		case SKILL_GREATAXE:
		case SKILL_SCYTH:
		case SKILL_POLEARM:
		case SKILL_KATANA:
		case SKILL_GREATKATANA:
		case SKILL_CLUB:
		case SKILL_STAFF:
		case SKILL_ARCHERY:
		case SKILL_MARKSMANSHIP:
		case SKILL_THROWING:
		case SKILL_GUARDING:
		case SKILL_EVASION:
		case SKILL_SHIELD:
		case SKILL_PARRYING:

		case SKILL_DIVINE_MAGIC:
		case SKILL_HEALING_MAGIC:
		case SKILL_ENCHANCING_MAGIC:
		case SKILL_ENFEEBLING_MAGIC:
		case SKILL_ELEMENTAL_MAGIC:
		case SKILL_DARK_MAGIC:
		case SKILL_SINGING:
		case SKILL_STRING_INSTRUMENT:
		case SKILL_WIND_INSTRUMENT:
		case SKILL_NINJUTSU:
		case SKILL_SUMMONING:
		case SKILL_BLUE_MAGIC:
			return calcSkill(level, type);
		default:
			return super.getStatus(level, type);
		}
	}

	// util
	private StatusValue calcHP(JobLevelAndRace level) {
		StatusValue value;
		value = new StatusValue(Dao.getHP(level.getRace(), level.getJob(), level.getLevel(), level.getSubJob(), level.getSubLevel()), 0);
		return value;
	}

	private StatusValue calcMP(JobLevelAndRace level) {
		StatusValue value;
		value = new StatusValue(Dao.getMP(level.getRace(), level.getJob(), level.getLevel(), level.getSubJob(), level.getSubLevel()), 0);
		return value;
	}

	private StatusValue calcStatus(JobLevelAndRace level, StatusType type) {
		StatusValue value;
		value = new StatusValue(Dao.getStatus(type, level.getRace(), level.getJob(), level.getLevel(), level.getSubJob(), level.getSubLevel()), 0);

		return value;
	}
	
	private StatusValue calcSkill(JobLevelAndRace level, StatusType type) {
		int cap;
		StatusValue v;
		
		cap = Dao.getSkillCap(type, level.getJob(), level.getLevel(), level.getSubJob(), level.getSubLevel());
		v = super.getStatus(level, type);
		if (v.getValue() > cap) {
			v.setValue(cap);
		}
		return v;
	}
}
