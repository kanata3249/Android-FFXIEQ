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

	public boolean ismRare() {
		return mRare;
	}

	public void setRare(boolean mRare) {
		this.mRare = mRare;
	}

	public boolean ismEx() {
		return mEx;
	}

	public void setEx(boolean mEx) {
		this.mEx = mEx;
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
}
