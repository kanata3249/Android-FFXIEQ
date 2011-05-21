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

import com.github.kanata3249.ffxi.FFXIString;
import com.github.kanata3249.ffxi.status.*;

import java.io.Serializable;

public class Equipment extends StatusModifierWithDescription implements Serializable  {
	private static final long serialVersionUID = 1L;

	private long mId;
	private long mAugId;
	private String mName;
	private String mPart;
	private String mWeapon;
	private String mJob;
	private String mRace;
	private int mLevel;
	private boolean mRare;
	private boolean mEx;
	private String mAugment;
	private long mCombinationID;
	
	private transient SortedStringList mRangedTokens;
	
	public Equipment(long id, String name, String part, String weapon, String job, String race, int level, boolean rare, boolean ex, String description) {
		this(id, -1, name, part, weapon, job, race, level, rare, ex, description, "");
	}

	public Equipment(long id, long augId, String name, String part, String weapon, String job, String race, int level, boolean rare, boolean ex, String description, String augment) {
		super();
		
		mId = id;
		mAugId = augId;
		mName = name;
		mPart = part;
		mWeapon = weapon;
		mJob = job;
		mRace = race;
		mLevel = level;
		mRare = rare;
		mEx = ex;
		mDescription = canonicalizeDescription(description);
		mAugment = augment;
		mCombinationID = 0;
		
		mNeedParseDescription = true;
	}

	public void setAugment(int augId, String augment) {
		mAugId = augId;
		mAugment = augment;
	}
	
	public long getId() {
		return mId;
	}

	public void setId(long mId) {
		this.mId = mId;
	}

	public long getAugId() {
		return mAugId;
	}

	public void setAugId(int mAugId) {
		this.mAugId = mAugId;
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getPart() {
		return mPart;
	}

	public void setPart(String mPart) {
		this.mPart = mPart;
	}

	public String getWeapon() {
		return mWeapon;
	}

	public StatusType getWeaponType() {
		if (Dao.getString(FFXIString.TOKEN_SKILL_HANDTOHAND).startsWith(mWeapon))
			return StatusType.SKILL_HANDTOHAND;
		if (Dao.getString(FFXIString.TOKEN_SKILL_DAGGER).startsWith(mWeapon))
			return StatusType.SKILL_DAGGER;
		if (Dao.getString(FFXIString.TOKEN_SKILL_SWORD).startsWith(mWeapon))
			return StatusType.SKILL_SWORD;
		if (Dao.getString(FFXIString.TOKEN_SKILL_GREATSWORD).startsWith(mWeapon))
			return StatusType.SKILL_GREATSWORD;
		if (Dao.getString(FFXIString.TOKEN_SKILL_AXE).startsWith(mWeapon))
			return StatusType.SKILL_AXE;
		if (Dao.getString(FFXIString.TOKEN_SKILL_GREATEAXE).startsWith(mWeapon))
			return StatusType.SKILL_GREATAXE;
		if (Dao.getString(FFXIString.TOKEN_SKILL_SCYTH).startsWith(mWeapon))
			return StatusType.SKILL_SCYTH;
		if (Dao.getString(FFXIString.TOKEN_SKILL_POLEARM).startsWith(mWeapon))
			return StatusType.SKILL_POLEARM;
		if (Dao.getString(FFXIString.TOKEN_SKILL_KATANA).startsWith(mWeapon))
			return StatusType.SKILL_KATANA;
		if (Dao.getString(FFXIString.TOKEN_SKILL_GREATKATANA).startsWith(mWeapon))
			return StatusType.SKILL_GREATKATANA;
		if (Dao.getString(FFXIString.TOKEN_SKILL_CLUB).startsWith(mWeapon))
			return StatusType.SKILL_CLUB;
		if (Dao.getString(FFXIString.TOKEN_SKILL_STAFF).startsWith(mWeapon))
			return StatusType.SKILL_STAFF;
		if (Dao.getString(FFXIString.TOKEN_SKILL_ARCHERY).startsWith(mWeapon))
			return StatusType.SKILL_ARCHERY;
		if (Dao.getString(FFXIString.TOKEN_SKILL_MARKSMANSHIP).startsWith(mWeapon))
			return StatusType.SKILL_MARKSMANSHIP;
		if (Dao.getString(FFXIString.TOKEN_SKILL_THROWING).startsWith(mWeapon))
			return StatusType.SKILL_THROWING;
		if (Dao.getString(FFXIString.TOKEN_SKILL_GUARDING).startsWith(mWeapon))
			return StatusType.SKILL_GUARDING;
		if (Dao.getString(FFXIString.TOKEN_SKILL_EVASION).startsWith(mWeapon))
			return StatusType.SKILL_EVASION;
		if (Dao.getString(FFXIString.TOKEN_SKILL_SHIELD).startsWith(mWeapon))
			return StatusType.SKILL_SHIELD;
		if (Dao.getString(FFXIString.TOKEN_SKILL_PARRYING).startsWith(mWeapon))
			return StatusType.SKILL_PARRYING;		
		return null;
	}

	public void setWeapon(String mWeapon) {
		this.mWeapon = mWeapon;
	}

	public String getJob() {
		return mJob;
	}

	public void setJob(String mJob) {
		this.mJob = mJob;
	}

	public String getRace() {
		return mRace;
	}

	public void setRace(String mRace) {
		this.mRace = mRace;
	}

	public int getLevel() {
		return mLevel;
	}

	public void setLevel(int mLevel) {
		this.mLevel = mLevel;
	}

	public boolean isRare() {
		return mRare;
	}

	public void setRare(boolean rare) {
		this.mRare = rare;
	}

	public boolean isEx() {
		return mEx;
	}

	public void setEx(boolean ex) {
		this.mEx = ex;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String mDescription) {
		this.mDescription = mDescription;
	}

	public String getAugment() {
		return mAugment;
	}

	public void setAugment(String mAugment) {
		this.mAugment = mAugment;
	}
	
	public long getCombinationID() {
		return mCombinationID;
	}
	public void setCombinationID(long id) {
		mCombinationID = id;
	}

	@Override
	public boolean parseDescription() {
		boolean updated = super.parseDescription();
		
		if (!updated)
			return updated;

		// Heuristic for ranged-value modifier.
		if (getLevel() == 30 && getPart().equals(Dao.getString(FFXIString.PART_DB_RING1))) {
			//for Rajas/Sattva/Tamas
			SortedStringList unknownTokens = getUnknownTokens();
			if (unknownTokens == null) {
				return updated;
			}
			
			for (int i = 0; i < unknownTokens.size(); i++) {
				String str = unknownTokens.get(i);
				if (isRangedValueFor(StatusType.STR, str, 2, 5)
					|| isRangedValueFor(StatusType.DEX, str, 2, 5)
					|| isRangedValueFor(StatusType.VIT, str, 2, 5)
					|| isRangedValueFor(StatusType.AGI, str, 2, 5)
					|| isRangedValueFor(StatusType.INT, str, 2, 5)
					|| isRangedValueFor(StatusType.MND, str, 2, 5)
					|| isRangedValueFor(StatusType.CHR, str, 2, 5)
					|| isRangedValueFor(StatusType.HP, str, 15, 30)
					|| isRangedValueFor(StatusType.MP, str, 15, 30)) {
					/* This one must be the one of tree rings. */
					if (mRangedTokens == null)
						mRangedTokens = new SortedStringList();
					mRangedTokens.addString(unknownTokens.get(i));
					unknownTokens.remove(i);
					i--;
				}
			}
		} else if (getLevel() == 20 && getPart().equals(Dao.getString(FFXIString.PART_DB_HEAD))) {
			// for Trump Crown
			SortedStringList unknownTokens = getUnknownTokens();
			if (unknownTokens == null) {
				return updated;
			}
			
			for (int i = 0; i < unknownTokens.size(); i++) {
				String str = unknownTokens.get(i);
				if (isRangedValueFor(StatusType.HP, str, 14, 0)
					|| isRangedValueFor(StatusType.MP, str, 14, 0)) {
					/* This one must be trump crown. */
					if (mRangedTokens == null)
						mRangedTokens = new SortedStringList();
					mRangedTokens.addString(unknownTokens.get(i));
					unknownTokens.remove(i);
					i--;
				}
			}
		}

		
		return updated;
	}
	@Override
	public StatusValue getStatus(JobLevelAndRace level, StatusType type) {
		switch (type) {
		case STR:
		case DEX:
		case VIT:
		case AGI:
		case INT:
		case MND:
		case CHR:
		case HP:
		case MP:
			SortedStringList rangedTokens = mRangedTokens;
			if (mRangedTokens == null) {
				break;
			}
				
			for (int i = 0; i < rangedTokens.size(); i++) {
				String str = rangedTokens.get(i);
				if (str.startsWith(type.name())) {
					str = str.substring(type.name().length() + 1);
					return getRangedStatusValue(level, type, str);
				}
				
			}
			break;
		}
		return super.getStatus(level, type);
	}
	
	private boolean isRangedValueFor(StatusType type, String token, int expectedLowValue, int expectedHighValue) {
		if (token.startsWith(type.name())) {
			int low, high;
			String params[];
			String str;
			
			str = token.substring(type.name().length() + 1);
			if (str.endsWith("-")) {
				str = str + "0";
			}
			params = str.split("-");
			if (params.length == 2) {
				try {
					low = Integer.valueOf(params[0]);
					if (expectedHighValue == 0 && params[1].length() == 0)
						high = 0;
					else
						high = Integer.valueOf(params[1]);
					
					if (low == expectedLowValue && high == expectedHighValue) {
						return true;
					}
				} catch (NumberFormatException e) {
				}
			}
		}
		return false;
	}
	
	private StatusValue getRangedStatusValue(JobLevelAndRace level, StatusType type, String param) {
		String [] params;

		if (param.endsWith("-")) {
			param = param + "0";
		}
		params = param.split("-");
		if (params.length == 2) {
			int low, high;

			low = Integer.valueOf(params[0]);
			high = 0;
			if (params[1].length() > 0) {
				high = Integer.valueOf(params[1]);
			}
				
			if ((low == 2 && high == 5) || (low == 15 && high == 30)) {
				//for Rajas/Sattva/Tamas
				int v = 0;
				if (low == 2) {
					int modlevel;
					
					modlevel = (Math.min(75, level.getLevel()) - 30) / 15;
					if (modlevel >= 0) {
						v = low + modlevel;
					}
				} else if (low == 15) {
					int modlevel;
					
					modlevel = (Math.min(75, level.getLevel()) - 30) / 3;
					if (modlevel >= 0) {
						v = low + modlevel;
					}
				}
				return new StatusValue(0, v, 0);
			}
			if (low == 14 && high == 0) {
				int v;
				int [][]modvalues = { {75, 27}, {72, 26}, {68, 25}, {65, 24}, {61, 23}, {58, 22}, {54, 21}, {50, 20},
									  {47, 19}, {44, 18}, {40, 17}, {35, 16}, {30, 15}, {17, 14} };

				v = 0;
				for (int i = 0; i < modvalues.length; i++) {
					if (modvalues[i][0] <= level.getLevel()) {
						v = modvalues[i][1];
						break;
					}
				}

				return new StatusValue(0, v, 0);				
			}
		}
		/* not happen */
		return null;
	}

	public void removeCombinationToken() {
		if (mUnknownTokens == null)
			return;
		String combination = Dao.getString(FFXIString.TOKEN_SetBonus);
		for (int i = 0; i < mUnknownTokens.size(); i++) {
			String str = mUnknownTokens.get(i);
			if (str.startsWith(combination)) {
				mUnknownTokens.remove(i);
				return;
			}
			
		}
	}
}
