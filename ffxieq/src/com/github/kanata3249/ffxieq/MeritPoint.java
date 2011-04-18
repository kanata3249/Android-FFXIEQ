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
package com.github.kanata3249.ffxieq	;

import java.io.Serializable;

import com.github.kanata3249.ffxi.status.*;

public class MeritPoint extends StatusModifier implements Serializable  {
	private static final long serialVersionUID = 1L;

	//
	int mMerits[];
	
	public MeritPoint() {
		super();
		
		loadDefaultValues();
	}


	@Override
	protected void loadDefaultValues() {
		super.loadDefaultValues();
		if (mMerits == null) {
			mMerits = new int[StatusType.MODIFIER_NUM.ordinal()];
		} else if (mMerits.length != StatusType.MODIFIER_NUM.ordinal()) {
			int merits[] = new int[StatusType.MODIFIER_NUM.ordinal()]; 
			for (int i = 0; i < Math.min(merits.length, mMerits.length); i++) {
				merits[i] = mMerits[i];
			}
			mMerits = merits;
		}
	}


	@Override
	public StatusValue getStatus(JobLevelAndRace level, StatusType type) {
		if (mMerits == null) // quick hack
			mMerits = new int[StatusType.MODIFIER_NUM.ordinal()];

		StatusValue v = new StatusValue(0, 0, 0);
		int merit = getMeritPoint(type);
		int meritcap = level.getLevel() / 10;
		
		switch (type) {
		case HP:
		case MP:
			v.setValue(Math.min(meritcap, merit) * 10);
			break;
		case STR:
		case DEX:
		case VIT:
		case AGI:
		case INT:
		case MND:
		case CHR:
			v.setValue(Math.min(meritcap, merit));
			break;
			
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
			v.setValue(Math.min(meritcap, merit) * 2);
			break;
		case SKILL_GUARDING:
		case SKILL_EVASION:
		case SKILL_SHIELD:
		case SKILL_PARRYING:
			v.setValue(Math.min(meritcap, merit) * 2);
			break;
			
		case Enmity:
			if (merit >= 0) {
				v.setValue(Math.min(meritcap, merit));
			} else {
				v.setValue(Math.max(-meritcap, merit));
			}
			break;
		case CriticalRate:
		case CriticalRateDefence:	        	
		case SpellInterruptionRate:
			v.setValue(Math.min(meritcap, merit));
			break;
		}

		return v;
	}
	
	public int getMeritPoint(StatusType type) {
		loadDefaultValues();  // quick hack for StatusType length change...
		return mMerits[type.ordinal()];
	}
	public void setMeritPoint(StatusType type, int value) {
		loadDefaultValues();  // quick hack for StatusType length change...
		mMerits[type.ordinal()] = value;
	}
}
