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

public class EquipmentSet extends StatusModifier implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int MAINWEAPON = 0;
	public static final int SUBWEAPON = 1;
	public static final int HEAD = 2;
	public static final int BODY = 3;
	public static final int HANDS = 4;
	public static final int FEET = 5;
	public static final int LEGS = 6;
	public static final int BACK = 7;
	public static final int NECK = 8;
	public static final int WAIST = 9;
	public static final int RING1 = 10;
	public static final int RING2 = 11;
	public static final int EAR1 = 12;
	public static final int EAR2 = 13;
	public static final int RANGE = 14;
	public static final int ANMO = 15;
	public static final int EQUIPMENT_NUM = 16;
	
	private Equipment[] mEquipments;
	
	public EquipmentSet () {
		super();
		
		mEquipments = new Equipment[EQUIPMENT_NUM];
	}

	// IStatus
	public StatusValue getStatus(JobLevelAndRace level, StatusType type) {
		StatusValue total = new StatusValue(0, 0);

		for (int i = 0; i < mEquipments.length; i++) {
			if (mEquipments[i] != null) {
				mEquipments[i].parseDescription();
			}
		}
		if (mEquipments[HEAD] != null)
			total.add(mEquipments[HEAD].getStatus(level, type));
		if (mEquipments[BODY] != null)
			total.add(mEquipments[BODY].getStatus(level, type));
		if (mEquipments[HANDS] != null)
			total.add(mEquipments[HANDS].getStatus(level, type));
		if (mEquipments[FEET] != null)
			total.add(mEquipments[FEET].getStatus(level, type));
		if (mEquipments[LEGS] != null)
			total.add(mEquipments[LEGS].getStatus(level, type));
		if (mEquipments[BACK] != null)
			total.add(mEquipments[BACK].getStatus(level, type));
		if (mEquipments[NECK] != null)
			total.add(mEquipments[NECK].getStatus(level, type));
		if (mEquipments[WAIST] != null)
			total.add(mEquipments[WAIST].getStatus(level, type));
		if (mEquipments[RING1] != null)
			total.add(mEquipments[RING1].getStatus(level, type));
		if (mEquipments[RING2] != null)
			total.add(mEquipments[RING2].getStatus(level, type));
		if (mEquipments[EAR1] != null)
			total.add(mEquipments[EAR1].getStatus(level, type));
		if (mEquipments[EAR2] != null)
			total.add(mEquipments[EAR2].getStatus(level, type));
		switch(type) {
		case D:
		case Delay:
			if (mEquipments[MAINWEAPON] != null) {
				if (mEquipments[MAINWEAPON].getWeaponType() == StatusType.SKILL_HANDTOHAND) {
					total.setAdditional(mEquipments[MAINWEAPON].getStatus(level, type).getAdditional());
				} else {
					total.setValue(mEquipments[MAINWEAPON].getStatus(level, type).getAdditional());
				}
			}
			break;

		case DSub:
			if (mEquipments[SUBWEAPON] != null) {
				total.setValue(mEquipments[SUBWEAPON].getStatus(level, StatusType.D).getAdditional());
			}
			break;
		case DelaySub:
			if (mEquipments[SUBWEAPON] != null) {
				total.setValue(mEquipments[SUBWEAPON].getStatus(level, StatusType.Delay).getAdditional());
			}
			break;

		case DRange:
			if (mEquipments[RANGE] != null) {
				total.setValue(mEquipments[RANGE].getStatus(level, StatusType.D).getAdditional());
				if (mEquipments[ANMO] != null)
					total.setAdditional(mEquipments[ANMO].getStatus(level, StatusType.D).getAdditional());
			} else if (mEquipments[ANMO] != null)
				total.setValue(mEquipments[ANMO].getStatus(level, StatusType.D).getAdditional());
			break;
		case DelayRange:
			if (mEquipments[RANGE] != null) {
				total.setValue(mEquipments[RANGE].getStatus(level, StatusType.Delay).getAdditional());
				if (mEquipments[ANMO] != null)
					total.setAdditional(mEquipments[ANMO].getStatus(level, StatusType.Delay).getAdditional());
			} else if (mEquipments[ANMO] != null)
				total.setValue(mEquipments[ANMO].getStatus(level, StatusType.Delay).getAdditional());
			break;
		default:
			if (mEquipments[MAINWEAPON] != null)
				total.add(mEquipments[MAINWEAPON].getStatus(level, type));
			if (mEquipments[SUBWEAPON] != null)
				total.add(mEquipments[SUBWEAPON].getStatus(level, type));
			if (mEquipments[RANGE] != null)
				total.add(mEquipments[RANGE].getStatus(level, type));
			if (mEquipments[ANMO] != null)
				total.add(mEquipments[ANMO].getStatus(level, type));
		}
		return total;
	}

	public Equipment getEquipment(int part) {
		return mEquipments[part];
	}
	
	public void setEquipment(int part, long id) {
		mEquipments[part] = Dao.instantiateEquipment(id);
	}
	
	public String getUnknownTokens() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mEquipments.length; i++) {
			if (mEquipments[i] != null) {
				sb.append(mEquipments[i].getUnknownTokens());
			}
		}
		return sb.toString();
	}
}
