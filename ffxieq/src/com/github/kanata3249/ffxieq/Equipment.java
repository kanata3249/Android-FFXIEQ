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
import java.util.Hashtable;

public class Equipment extends StatusModifier implements Serializable  {
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
	private String mDescription;
	private String mAugment;
	
	private transient String mUnknownTokens;
	private transient boolean mNeedParseDescription;
	
	abstract class DescriptionTokenHandler {
		abstract boolean handleToken(String token, String parameter);
	}
	
	private transient Hashtable<String, DescriptionTokenHandler> fTokenHandler;

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
	
	public boolean parseDescription() {
		boolean updated = false;
		String tokens[];
		StringBuilder unknownTokens;

		if (fTokenHandler == null) {
			setupTokenHandler();
		} else {
			if (!mNeedParseDescription)
				return updated;
		}

		loadDefaultValues();
		unknownTokens = new StringBuilder();
		tokens = mDescription.split(Dao.getString(FFXIString.TOKEN_Latent_Effect));
		if (tokens.length > 1) {
			unknownTokens.append(Dao.getString(FFXIString.TOKEN_Latent_Effect) + tokens[1]);
			unknownTokens.append('\n');
		}
		tokens = tokens[0].split(Dao.getString(FFXIString.TOKEN_Assult));
		if (tokens.length > 1) {
			unknownTokens.append(Dao.getString(FFXIString.TOKEN_Assult) + tokens[1]);
			unknownTokens.append('\n');
		}
		tokens = tokens[0].split(Dao.getString(FFXIString.TOKEN_Besieged));
		if (tokens.length > 1) {
			unknownTokens.append(Dao.getString(FFXIString.TOKEN_Besieged) + tokens[1]);
			unknownTokens.append('\n');
		}
		tokens = tokens[0].split(Dao.getString(FFXIString.TOKEN_Campaign));
		if (tokens.length > 1) {
			unknownTokens.append(Dao.getString(FFXIString.TOKEN_Campaign) + tokens[1]);
			unknownTokens.append('\n');
		}
		tokens = tokens[0].split(Dao.getString(FFXIString.TOKEN_Dynamis));
		if (tokens.length > 1) {
			unknownTokens.append(Dao.getString(FFXIString.TOKEN_Dynamis) + tokens[1]);
			unknownTokens.append('\n');
		}
		tokens = tokens[0].split(Dao.getString(FFXIString.ItemDescriptionTokenSeparator));
		for (int i = 0; i < tokens.length; i++) {
			String token, parameter;
			DescriptionTokenHandler handler;
			
			token = (tokens[i].split("[\\+\\-0-9]"))[0];
			parameter = tokens[i].substring(token.length());

			handler = fTokenHandler.get(token);
			if (handler != null) {
				if (handler.handleToken(token, parameter)) {
					updated = true;
				} else {
					unknownTokens.append(tokens[i]);
					unknownTokens.append('\n');
				}
			} else {
				unknownTokens.append(tokens[i]);
				unknownTokens.append('\n');
			}
		}
		mUnknownTokens = unknownTokens.toString();
		mNeedParseDescription = false;
		return updated;
	}
	
	public String canonicalizeDescription(String string) {
		StringBuilder newString = new StringBuilder();
		String tmpString = new String(string);
		// TODO Convert SPC to UNDERLINE for some multi-word token.  (for English/German version?)
		
		// Canonicalize some error characters in japanese database
		final String SIGN_CHARS = "！＃＄％＆（）＊＋，－．／：；＜＝＞？＠［］＾＿｛｜｝";
		for (int i =0; i < tmpString.length(); i++) {
			char ch = tmpString.charAt(i);
			if ((ch >= 'Ａ' && ch <= 'Ｚ')
				|| (ch >= '０' && ch <= '９')
				|| SIGN_CHARS.indexOf(ch) >= 0) {
				ch = (char) ('A' + (ch - 'Ａ'));
			} else if (ch == '～') {
				ch = '-';
			} else if (ch == '　') {
				ch = ' ';
			}
			newString.append(ch);
		}

		return newString.toString();
	}
	
	private StatusValue handleCommonToken(StatusValue base, String parameter) {
		StatusValue newValue = new StatusValue();
		int modifier, start, end, value;
		boolean additional, percent;

		additional = true;
		percent = false;
		start = 0;
		end = parameter.length();
		modifier = 1;
		if (parameter.startsWith("-")) {
			modifier = -1;
			additional = true;
			start++;
		} else if (parameter.startsWith("+")) {
			modifier = 1;
			additional = true;
			start++;
		}
		if (parameter.endsWith("%")) {
			additional = true;
			percent = true;
			end--;
		}
// TODO range value		
		try {
			value = Integer.parseInt(parameter.substring(start, end));
		} catch (NumberFormatException e) {
			value = 0;
		}
		value *= modifier;

		if (additional) {
			if (percent) {
				newValue.setAdditionalPercent(value);
			} else {
				newValue.setAdditional(value);
			}
		} else {
			newValue.setValue(value);
		}
		
		newValue.add(base);
		
		return newValue;
	}
	
	private void setupTokenHandler() {
		if (fTokenHandler != null) {
			return;
		}
		fTokenHandler = new Hashtable<String, DescriptionTokenHandler>();
		setupCommonTokenHandler(FFXIString.TOKEN_STR, StatusType.STR);
		setupCommonTokenHandler(FFXIString.TOKEN_VIT, StatusType.VIT);
		setupCommonTokenHandler(FFXIString.TOKEN_DEX, StatusType.DEX);
		setupCommonTokenHandler(FFXIString.TOKEN_AGI, StatusType.AGI);
		setupCommonTokenHandler(FFXIString.TOKEN_INT, StatusType.INT);
		setupCommonTokenHandler(FFXIString.TOKEN_MND, StatusType.MND);
		setupCommonTokenHandler(FFXIString.TOKEN_CHR, StatusType.CHR);
		setupCommonTokenHandler(FFXIString.TOKEN_HP, StatusType.HP);
		setupCommonTokenHandler(FFXIString.TOKEN_MP, StatusType.MP);
		setupCommonTokenHandler(FFXIString.TOKEN_DMP, StatusType.D);
		setupCommonTokenHandler(FFXIString.TOKEN_DELAY, StatusType.Delay);
		setupCommonTokenHandler(FFXIString.TOKEN_ATTACK, StatusType.Attack);
		setupCommonTokenHandler(FFXIString.TOKEN_ATTACK_RANGE, StatusType.AttackRange);
		setupCommonTokenHandler(FFXIString.TOKEN_DEF, StatusType.Defence);
		setupCommonTokenHandler(FFXIString.TOKEN_HASTE, StatusType.Haste);
		setupCommonTokenHandler(FFXIString.TOKEN_SLOW, StatusType.Slow);
		setupCommonTokenHandler(FFXIString.TOKEN_SUBTLE_BLOW, StatusType.SubtleBlow);
		setupCommonTokenHandler(FFXIString.TOKEN_STORE_TP, StatusType.StoreTP);
		setupCommonTokenHandler(FFXIString.TOKEN_EVASION, StatusType.Evasion);
		setupCommonTokenHandler(FFXIString.TOKEN_ACCURACY, StatusType.Accuracy);
		setupCommonTokenHandler(FFXIString.TOKEN_ACCURACY_RANGE, StatusType.AccuracyRange);
		setupCommonTokenHandler(FFXIString.TOKEN_DOUBLE_ATTACK, StatusType.DoubleAttack);
		setupCommonTokenHandler(FFXIString.TOKEN_TRIPPLE_ATTACK, StatusType.TrippleAttack);
		setupCommonTokenHandler(FFXIString.TOKEN_QUAD_ATTACK, StatusType.QuadAttack);
		setupCommonTokenHandler(FFXIString.TOKEN_ENMITY, StatusType.Enmity);
		setupCommonTokenHandler(FFXIString.TOKEN_CRITICAL_RATE, StatusType.CriticalRate);
		setupCommonTokenHandler(FFXIString.TOKEN_CRITICAL_DAMAGE, StatusType.CriticalDamage);
		setupCommonTokenHandler(FFXIString.TOKEN_ACCURACY_MAGIC, StatusType.AccuracyMagic);
		setupCommonTokenHandler(FFXIString.TOKEN_ATTACK_MAGIC, StatusType.AttackMagic);
		setupCommonTokenHandler(FFXIString.TOKEN_ATTACK_MAGIC2, StatusType.AttackMagic);
		setupCommonTokenHandler(FFXIString.TOKEN_DEFENCE, StatusType.Defence);
		setupCommonTokenHandler(FFXIString.TOKEN_DEFENCE_MAGIC, StatusType.DefenceMagic);
		setupCommonTokenHandler(FFXIString.TOKEN_DEFENCE_MAGIC2, StatusType.DefenceMagic);
		setupCommonTokenHandler(FFXIString.TOKEN_DAMAGECUT, StatusType.DamageCut);
		setupCommonTokenHandler(FFXIString.TOKEN_DAMAGECUT_PHYSICAL, StatusType.DamageCutPhysical);
		setupCommonTokenHandler(FFXIString.TOKEN_DAMAGECUT_MAGIC, StatusType.DamageCutMagic);
		setupCommonTokenHandler(FFXIString.TOKEN_DAMAGECUT_BREATH, StatusType.DamageCutBreath);

		setupCommonTokenHandler(FFXIString.TOKEN_REGIST_FIRE, StatusType.Regist_Fire);
		setupCommonTokenHandler(FFXIString.TOKEN_REGIST_ICE, StatusType.Regist_Ice);
		setupCommonTokenHandler(FFXIString.TOKEN_REGIST_WIND, StatusType.Regist_Wind);
		setupCommonTokenHandler(FFXIString.TOKEN_REGIST_EARTH, StatusType.Regist_Earth);
		setupCommonTokenHandler(FFXIString.TOKEN_REGIST_LIGHTNING, StatusType.Regist_Lightning);
		setupCommonTokenHandler(FFXIString.TOKEN_REGIST_WATER, StatusType.Regist_Water);
		setupCommonTokenHandler(FFXIString.TOKEN_REGIST_LIGHT, StatusType.Regist_Light);
		setupCommonTokenHandler(FFXIString.TOKEN_REGIST_DARK, StatusType.Regist_Dark);

		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_HANDTOHAND, StatusType.SKILL_HANDTOHAND);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_DAGGER, StatusType.SKILL_DAGGER);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_SWORD, StatusType.SKILL_SWORD);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_GREATSWORD, StatusType.SKILL_GREATSWORD);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_AXE, StatusType.SKILL_AXE);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_GREATEAXE, StatusType.SKILL_GREATAXE);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_SCYTH, StatusType.SKILL_SCYTH);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_POLEARM, StatusType.SKILL_POLEARM);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_KATANA, StatusType.SKILL_KATANA);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_GREATKATANA, StatusType.SKILL_GREATKATANA);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_CLUB, StatusType.SKILL_CLUB);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_STAFF, StatusType.SKILL_STAFF);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_ARCHERY, StatusType.SKILL_ARCHERY);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_MARKSMANSHIP, StatusType.SKILL_MARKSMANSHIP);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_THROWING, StatusType.SKILL_THROWING);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_GUARDING, StatusType.SKILL_GUARDING);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_EVASION, StatusType.SKILL_EVASION);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_SHIELD, StatusType.SKILL_SHIELD);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_PARRYING, StatusType.SKILL_PARRYING);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_DIVINE_MAGIC, StatusType.SKILL_DIVINE_MAGIC);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_HEALING_MAGIC, StatusType.SKILL_HEALING_MAGIC);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_ENCHANCING_MAGIC, StatusType.SKILL_ENCHANCING_MAGIC);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_ENFEEBLING_MAGIC, StatusType.SKILL_ENFEEBLING_MAGIC);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_ELEMENTAL_MAGIC, StatusType.SKILL_ELEMENTAL_MAGIC);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_DARK_MAGIC, StatusType.SKILL_DARK_MAGIC);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_SINGING, StatusType.SKILL_SINGING);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_STRING_INSTRUMENT, StatusType.SKILL_STRING_INSTRUMENT);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_WIND_INSTRUMENT, StatusType.SKILL_WIND_INSTRUMENT);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_NINJUTSU, StatusType.SKILL_NINJUTSU);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_SUMMONING, StatusType.SKILL_SUMMONING);
		setupCommonTokenHandler(FFXIString.TOKEN_SKILL_BLUE_MAGIC, StatusType.SKILL_BLUE_MAGIC);
		
	}
	
	public void setupCommonTokenHandler(int token, final StatusType type) {
		fTokenHandler.put(Dao.getString(token), new DescriptionTokenHandler() {
			boolean handleToken(String token, String parameter) {
				StatusValue v = handleCommonToken(getStatus(type), parameter);
				if (v != null) {
					setStatus(v, type);
					return true;
				}
				return false;
			}
		});
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
	
	public String getUnknownTokens() {
		return mUnknownTokens;
	}
}
