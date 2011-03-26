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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.github.kanata3249.ffxi.*;
import com.github.kanata3249.ffxi.status.StatusType;
import com.github.kanata3249.ffxieq.Atma;
import com.github.kanata3249.ffxieq.Equipment;
import com.github.kanata3249.ffxieq.JobTrait;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.*;

public class FFXIDatabase extends SQLiteOpenHelper implements FFXIDAO {
	private static final String DB_NAME = "ffxieq";
	private static final String DB_NAME_ASSET = "db.zip";
	private static final String DB_PATH = "/data/data/com.github.kanata3249.ffxieq/databases/";

	static final Character[][] RaceToStatusRank = {
		// TODO This table should be read from DB.
		// HP MP STR DEX VIT AGI INT MND CHR  0:A 1:B 2:C 3:D 4:E 5:F 6:G
		{  'D', 'D', 'D', 'D', 'D', 'D', 'D', 'D', 'D' }, // Hum
		{  'C', 'E', 'B', 'E', 'C', 'F', 'F', 'B', 'D' }, // Elv
		{  'G', 'A', 'F', 'D', 'E', 'C', 'A', 'E', 'D' }, // Tar
		{  'D', 'D', 'E', 'A', 'E', 'B', 'D', 'E', 'F' }, // Mit
		{  'A', 'G', 'C', 'D', 'A', 'E', 'E', 'D', 'F' }, // Gal
	};


	String [][] JobToRank;

	Context mContext;
	HPTable mHpTable;
	MPTable mMpTable;
	JobRankTable mJobRankTable;
	StatusTable mStatusTable;
	SkillCapTable mSkillCapTable;
	EquipmentTable mEquipmentTable;
	AtmaTable mAtmaTable;
	JobTraitTable mJobTraitTable;

	// Constructor
	public FFXIDatabase(Context context) {
		super(context, DB_NAME, null, 1);
		
		mContext = context;
		mHpTable = new HPTable();
		mMpTable = new MPTable();
		mJobRankTable = new JobRankTable();
		mSkillCapTable = new SkillCapTable();
		mStatusTable = new StatusTable();
		mEquipmentTable = new EquipmentTable();
		mAtmaTable = new AtmaTable();
		mJobTraitTable = new JobTraitTable();

		try {
			copyDatabaseFromAssets();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JobToRank = mJobRankTable.buildJobRankTable(getReadableDatabase());
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
	public void copyDatabaseFromAssets() throws IOException {
		InputStream in = mContext.getAssets().open(DB_NAME_ASSET, AssetManager.ACCESS_STREAMING);
		ZipInputStream zipIn = new ZipInputStream(in);
		ZipEntry zipEntry = zipIn.getNextEntry();
		File outDir = new File(DB_PATH);
		
		outDir.mkdir();
		while (zipEntry != null) {
			byte[] buffer = new byte[4096];
			int size;
			
			OutputStream out = new FileOutputStream(DB_PATH + zipEntry.getName());
			while ((size = zipIn.read(buffer, 0, buffer.length)) > -1) {
				out.write(buffer, 0, size);
			}
			out.flush();
			out.close();
			zipIn.closeEntry();
			zipEntry = zipIn.getNextEntry();
		}
		
		zipIn.close();
		in.close();
	}

	// DA methods
	public String jobToRank(int job, StatusType type) {
		String ret;
		
		ret = JobToRank[job][type.ordinal()];
		if (ret == null) {
			ret = "-";
		}
		return ret;
	}
	public int getHP(int race, int job, int joblevel, int subjob, int subjoblevel) {
		return mHpTable.getHP(getReadableDatabase(), RaceToStatusRank[race][StatusType.HP.ordinal()].toString(), jobToRank(job, StatusType.HP), joblevel, jobToRank(subjob, StatusType.HP), subjoblevel);
	}
	public int getMP(int race, int job, int joblevel, int subjob, int subjoblevel) {
		return mMpTable.getMP(getReadableDatabase(), RaceToStatusRank[race][StatusType.MP.ordinal()].toString(), jobToRank(job, StatusType.MP), joblevel, jobToRank(subjob, StatusType.MP), subjoblevel);
	}
	public int getStatus(StatusType type, int race, int job, int joblevel, int subjob, int subjoblevel) {
		return mStatusTable.getStatus(getReadableDatabase(), RaceToStatusRank[race][type.ordinal()].toString(), jobToRank(job, type), joblevel, jobToRank(subjob, type), subjoblevel);
	}
	public int getSkillCap(StatusType type, int job, int joblevel, int subjob, int subjoblevel) {
		return mSkillCapTable.getSkillCap(getReadableDatabase(), jobToRank(job, type), joblevel, jobToRank(subjob, type), subjoblevel);
	}

	public String getString(int id) {
		switch (id) {
		case FFXIString.TOKEN_Latent_Effect:
			return "φέ\Ν:";
		case FFXIString.TOKEN_Assult:
			return "ATg:";
		case FFXIString.TOKEN_Besieged:
			return "rV[W:";
		case FFXIString.TOKEN_Campaign:
			return "JpjG:";
		case FFXIString.TOKEN_Dynamis:
			return "fi~X:";

		case FFXIString.ItemDescriptionTokenSeparator:
			return "\\s+";
		case FFXIString.TOKEN_STR:
			return "STR";
		case FFXIString.TOKEN_AGI:
			return "AGI";
		case FFXIString.TOKEN_DEX:
			return "DEX";
		case FFXIString.TOKEN_VIT:
			return "VIT";
		case FFXIString.TOKEN_INT:
			return "INT";
		case FFXIString.TOKEN_MND:
			return "MND";
		case FFXIString.TOKEN_CHR:
			return "CHR";
		case FFXIString.TOKEN_HP:
			return "HP";
		case FFXIString.TOKEN_MP:
			return "MP";
		case FFXIString.TOKEN_CONVERT_HPMP:
			return "HPπMPΙΟ·";
		case FFXIString.TOKEN_CONVERT_MPHP:
			return "MPπHPΙΟ·";
		case FFXIString.TOKEN_DMP:
			return "D";
		case FFXIString.TOKEN_DELAY:
			return "u";
		case FFXIString.TOKEN_ATTACK:
			return "U";
		case FFXIString.TOKEN_ATTACK_RANGE:
			return "U";
		case FFXIString.TOKEN_DEF:
			return "h";
		case FFXIString.TOKEN_HASTE:
			return "wCXg";
		case FFXIString.TOKEN_SLOW:
			return "XE";
		case FFXIString.TOKEN_SUBTLE_BLOW:
			return "NV";
		case FFXIString.TOKEN_STORE_TP:
			return "XgATP";
		case FFXIString.TOKEN_EVASION:
			return "ρπ";

		case FFXIString.TOKEN_ACCURACY:
			return "½";
		case FFXIString.TOKEN_ACCURACY_RANGE:
			return "ς½";
			
		case FFXIString.TOKEN_DOUBLE_ATTACK:
			return "_uA^bN";
		case FFXIString.TOKEN_TRIPPLE_ATTACK:
			return "gvA^bN";
		case FFXIString.TOKEN_QUAD_ATTACK:
			return "NbhA^bN";
		case FFXIString.TOKEN_ENMITY:
			return "GΞS";

		case FFXIString.TOKEN_CRITICAL_RATE:
			return "NeBJqbg";
		case FFXIString.TOKEN_CRITICAL_DAMAGE:
			return "NeBJqbg_[W";
		case FFXIString.TOKEN_ACCURACY_MAGIC:
			return "@½¦";
		case FFXIString.TOKEN_ATTACK_MAGIC:
			return "@UΝ";
		case FFXIString.TOKEN_ATTACK_MAGIC2:
			return "@UΝAbv";
		case FFXIString.TOKEN_DEFENCE:
			return "h";
		case FFXIString.TOKEN_DEFENCE_MAGIC:
			return "@hδΝ";
		case FFXIString.TOKEN_DEFENCE_MAGIC2:
			return "@hδΝAbv";
		case FFXIString.TOKEN_DAMAGECUT:
			return "ν_[W";
		case FFXIString.TOKEN_DAMAGECUT_PHYSICAL:
			return "ν¨_[W";
		case FFXIString.TOKEN_DAMAGECUT_MAGIC:
			return "ν@_[W";
		case FFXIString.TOKEN_DAMAGECUT_BREATH:
			return "νuX_[W";
			
		case FFXIString.TOKEN_SKILL_HANDTOHAND:
			return "i¬XL";
		case FFXIString.TOKEN_SKILL_DAGGER:
			return "ZXL";
		case FFXIString.TOKEN_SKILL_SWORD:
			return "ΠθXL";
		case FFXIString.TOKEN_SKILL_GREATSWORD:
			return "ΌθXL";
		case FFXIString.TOKEN_SKILL_AXE:
			return "ΠθXL";
		case FFXIString.TOKEN_SKILL_GREATEAXE:
			return "ΌθXL";
		case FFXIString.TOKEN_SKILL_SCYTH:
			return "ΌθXL";
		case FFXIString.TOKEN_SKILL_POLEARM:
			return "ΌθXL";
		case FFXIString.TOKEN_SKILL_KATANA:
			return "ΠθXL";
		case FFXIString.TOKEN_SKILL_GREATKATANA:
			return "ΌθXL";
		case FFXIString.TOKEN_SKILL_CLUB:
			return "ΠθXL";
		case FFXIString.TOKEN_SKILL_STAFF:
			return "ΌθXL";
		case FFXIString.TOKEN_SKILL_ARCHERY:
			return "|pXL";
		case FFXIString.TOKEN_SKILL_MARKSMANSHIP:
			return "ΛXL";
		case FFXIString.TOKEN_SKILL_THROWING:
			return "Δ«XL";
		case FFXIString.TOKEN_SKILL_SHIELD:
			return "XL";
		case FFXIString.TOKEN_SKILL_GUARDING:
			return "K[hXL";
		case FFXIString.TOKEN_SKILL_EVASION:
			return "ρπXL";
		case FFXIString.TOKEN_SKILL_PARRYING:
			return "σ―¬΅XL";

		case FFXIString.TOKEN_SKILL_HEALING_MAGIC:
			return "ρ@XL";
		case FFXIString.TOKEN_SKILL_ENCHANCING_MAGIC:
			return "­»@XL";
		case FFXIString.TOKEN_SKILL_ENFEEBLING_MAGIC:
			return "γΜ@XL";
		case FFXIString.TOKEN_SKILL_ELEMENTAL_MAGIC:
			return "Έμ@XL";
		case FFXIString.TOKEN_SKILL_DIVINE_MAGIC:
			return "_Ή@XL";
		case FFXIString.TOKEN_SKILL_SINGING:
			return "Μ₯XL";
		case FFXIString.TOKEN_SKILL_WIND_INSTRUMENT:
			return "ΗyνXL";
		case FFXIString.TOKEN_SKILL_STRING_INSTRUMENT:
			return "·yνXL";
		case FFXIString.TOKEN_SKILL_NINJUTSU:
			return "EpXL";
		case FFXIString.TOKEN_SKILL_SUMMONING:
			return "’«@XL";
		case FFXIString.TOKEN_SKILL_BLUE_MAGIC:
			return "Β@XL";
		case FFXIString.TOKEN_SKILL_DARK_MAGIC:
			return "Γ@XL";
			
		case FFXIString.TOKEN_REGIST_FIRE:
			return "ΟΞ";
		case FFXIString.TOKEN_REGIST_ICE:
			return "ΟX";
		case FFXIString.TOKEN_REGIST_WIND:
			return "Ο";
		case FFXIString.TOKEN_REGIST_EARTH:
			return "Οy";
		case FFXIString.TOKEN_REGIST_LIGHTNING:
			return "Ο";
		case FFXIString.TOKEN_REGIST_WATER:
			return "Ο";
		case FFXIString.TOKEN_REGIST_LIGHT:
			return "Ου";
		case FFXIString.TOKEN_REGIST_DARK:
			return "ΟΕ";
			
		case FFXIString.RACE_HUM:
			return "Hum";
		case FFXIString.RACE_ELV:
			return "Elv";
		case FFXIString.RACE_TAR:
			return "Tar";
		case FFXIString.RACE_MIT:
			return "Mit";
		case FFXIString.RACE_GAL:
			return "Gal";

		case FFXIString.RACE_DB_HUM:
			return "Hum";
		case FFXIString.RACE_DB_ELV:
			return "Elv";
		case FFXIString.RACE_DB_TAR:
			return "Tar";
		case FFXIString.RACE_DB_MIT:
			return "Mit";
		case FFXIString.RACE_DB_GAL:
			return "Gal";
		case FFXIString.RACE_DB_ALL:
			return "Sν";

		case FFXIString.JOB_WAR:
			return "WAR";
		case FFXIString.JOB_MNK:
			return "MNK";
		case FFXIString.JOB_WHM:
			return "WHM";
		case FFXIString.JOB_BLM:
			return "BLM";
		case FFXIString.JOB_RDM:
			return "RDM";
		case FFXIString.JOB_THF:
			return "THF";
		case FFXIString.JOB_PLD:
			return "PLD";
		case FFXIString.JOB_DRK:
			return "DRK";
		case FFXIString.JOB_BST:
			return "BST";
		case FFXIString.JOB_BRD:
			return "BRD";
		case FFXIString.JOB_RNG:
			return "RNG";
		case FFXIString.JOB_SAM:
			return "SAM";
		case FFXIString.JOB_NIN:
			return "NIN";
		case FFXIString.JOB_DRG:
			return "DRG";
		case FFXIString.JOB_SMN:
			return "SMN";
		case FFXIString.JOB_BLU:
			return "BLU";
		case FFXIString.JOB_COR:
			return "COR";
		case FFXIString.JOB_PUP:
			return "PUP";
		case FFXIString.JOB_DNC:
			return "DNC";
		case FFXIString.JOB_SCH:
			return "SCH";
			
		case FFXIString.JOB_DB_WAR:
			return "ν";
		case FFXIString.JOB_DB_MNK:
			return "";
		case FFXIString.JOB_DB_WHM:
			return "";
		case FFXIString.JOB_DB_BLM:
			return "";
		case FFXIString.JOB_DB_RDM:
			return "Τ";
		case FFXIString.JOB_DB_THF:
			return "V";
		case FFXIString.JOB_DB_PLD:
			return "i";
		case FFXIString.JOB_DB_DRK:
			return "Γ";
		case FFXIString.JOB_DB_BST:
			return "b";
		case FFXIString.JOB_DB_BRD:
			return "α";
		case FFXIString.JOB_DB_RNG:
			return "λ";
		case FFXIString.JOB_DB_SAM:
			return "";
		case FFXIString.JOB_DB_NIN:
			return "E";
		case FFXIString.JOB_DB_DRG:
			return "³";
		case FFXIString.JOB_DB_SMN:
			return "’";
		case FFXIString.JOB_DB_BLU:
			return "Β";
		case FFXIString.JOB_DB_COR:
			return "R";
		case FFXIString.JOB_DB_PUP:
			return "©";
		case FFXIString.JOB_DB_DNC:
			return "x";
		case FFXIString.JOB_DB_SCH:
			return "w";
		case FFXIString.JOB_DB_ALL:
			return "All Jobs";

		case FFXIString.PART_MAIN:
			return "Main";
		case FFXIString.PART_SUB:
			return "Sub";
		case FFXIString.PART_HEAD:
			return "Head";
		case FFXIString.PART_BODY:
			return "Body";
		case FFXIString.PART_HANDS:
			return "Hands";
		case FFXIString.PART_FEET:
			return "Feet";
		case FFXIString.PART_LEGS:
			return "Legs";
		case FFXIString.PART_BACK:
			return "Back";
		case FFXIString.PART_NECK:
			return "Neck";
		case FFXIString.PART_WAIST:
			return "Waist";
		case FFXIString.PART_RING1:
			return "Ring1";
		case FFXIString.PART_RING2:
			return "Ring2";
		case FFXIString.PART_EAR1:
			return "Ear1";
		case FFXIString.PART_EAR2:
			return "Ear2";
		case FFXIString.PART_RANGE:
			return "Range";
		case FFXIString.PART_ANMO:
			return "Anmo";

		case FFXIString.PART_DB_MAIN:
			return "ε";
		case FFXIString.PART_DB_SUB:
			return "";
		case FFXIString.PART_DB_HEAD:
			return "ͺ";
		case FFXIString.PART_DB_BODY:
			return "·";
		case FFXIString.PART_DB_HANDS:
			return "Όθ";
		case FFXIString.PART_DB_FEET:
			return "Όr";
		case FFXIString.PART_DB_LEGS:
			return "Ό«";
		case FFXIString.PART_DB_BACK:
			return "w";
		case FFXIString.PART_DB_NECK:
			return "ρ";
		case FFXIString.PART_DB_WAIST:
			return "";
		case FFXIString.PART_DB_RING1:
			return "w";
		case FFXIString.PART_DB_RING2:
			return "w";
		case FFXIString.PART_DB_EAR1:
			return "¨";
		case FFXIString.PART_DB_EAR2:
			return "¨";
		case FFXIString.PART_DB_RANGE:
			return "u";
		case FFXIString.PART_DB_ANMO:
			return "ξe";
		default:
			return null;
		}
	}
	
	public Equipment instanciateEquipment(long id) {
		return mEquipmentTable.newInstance(this, getReadableDatabase(), id, -1);
	}
	public Atma instanciateAtma(long id) {
		return mAtmaTable.newInstance(this, getReadableDatabase(), id);
	}
	public JobTrait[] getJobTraits(int job, int level) {
		return mJobTraitTable.getJobTraits(this, getReadableDatabase(), getString(FFXIString.JOB_DB_WAR + job), level);
	}


	public Cursor getEquipmentCursor(int part, int race, int job, int level, String[] columns, String orderBy) {
		return mEquipmentTable.getCursor(this, getReadableDatabase(), part, race, job, level, columns, orderBy);
	}
	public Cursor getAtmaCursor(String[] columns, String orderBy) {
		return mAtmaTable.getCursor(this, getReadableDatabase(), columns, orderBy);
	}

}
