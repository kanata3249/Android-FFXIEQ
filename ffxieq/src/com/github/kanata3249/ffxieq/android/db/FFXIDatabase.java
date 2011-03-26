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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
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
import android.os.Environment;

public class FFXIDatabase extends SQLiteOpenHelper implements FFXIDAO {
	private static final String DB_NAME = "ffxieq";
	private static final String DB_NAME_ASSET = "db.zip";
	private static String DB_PATH;
	private static String SD_PATH;

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
		
		DB_PATH = Environment.getDataDirectory() + "/data/" + context.getPackageName() + "/databases/";
		SD_PATH = Environment.getExternalStorageDirectory() + "/" + context.getPackageName() + "/"; 

		mContext = context;
		mHpTable = new HPTable();
		mMpTable = new MPTable();
		mJobRankTable = new JobRankTable();
		mSkillCapTable = new SkillCapTable();
		mStatusTable = new StatusTable();
		mEquipmentTable = new EquipmentTable();
		mAtmaTable = new AtmaTable();
		mJobTraitTable = new JobTraitTable();

		if (checkDatabase()) {
			try {
				copyDatabaseFromAssets();
			} catch (IOException e) {
			}
		}
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
	public boolean checkDatabase() {
		File in = new File(DB_PATH + DB_NAME);
		
		if (in.isFile())
			return false;
		return true;
	}
	
	public void copyDatabaseFromAssets() throws IOException {
		InputStream in = mContext.getAssets().open(DB_NAME_ASSET, AssetManager.ACCESS_STREAMING);
		ZipInputStream zipIn = new ZipInputStream(in);
		ZipEntry zipEntry = zipIn.getNextEntry();
		File outDir = new File(DB_PATH);
		
		SQLiteDatabase db;

		db = getReadableDatabase();
		if (db != null)
			db.close();

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

	public void copyDatabaseFromSD() throws IOException {
		File outDir = new File(DB_PATH);
		SQLiteDatabase db;

		db = getReadableDatabase();
		if (db != null)
			db.close();

		outDir.mkdir();
		FileChannel channelSource = new FileInputStream(SD_PATH + DB_NAME).getChannel();
		FileChannel channelTarget = new FileOutputStream(DB_PATH + DB_NAME).getChannel();
		channelSource.transferTo(0, channelSource.size(), channelTarget);

		channelSource.close();
		channelTarget.close();
		
	}

	public void copyDatabaseToSD() throws IOException {
		File outDir = new File(SD_PATH);

		outDir.mkdir();
		FileChannel channelSource = new FileInputStream(DB_PATH + DB_NAME).getChannel();
		FileChannel channelTarget = new FileOutputStream(SD_PATH + DB_NAME).getChannel();
		channelSource.transferTo(0, channelSource.size(), channelTarget);

		channelSource.close();
		channelTarget.close();
	}

	// DA methods
	public String jobToRank(int job, StatusType type) {
		String ret;
		
		if (JobToRank == null)
			JobToRank = mJobRankTable.buildJobRankTable(getReadableDatabase());
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
			return "潜在能力:";
		case FFXIString.TOKEN_Assult:
			return "アサルト:";
		case FFXIString.TOKEN_Besieged:
			return "ビシージ:";
		case FFXIString.TOKEN_Campaign:
			return "カンパニエ:";
		case FFXIString.TOKEN_Dynamis:
			return "デュナミス:";

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
			return "HPをMPに変換";
		case FFXIString.TOKEN_CONVERT_MPHP:
			return "MPをHPに変換";
		case FFXIString.TOKEN_DMP:
			return "D";
		case FFXIString.TOKEN_DELAY:
			return "隔";
		case FFXIString.TOKEN_ATTACK:
			return "攻";
		case FFXIString.TOKEN_ATTACK_RANGE:
			return "攻";
		case FFXIString.TOKEN_DEF:
			return "防";
		case FFXIString.TOKEN_HASTE:
			return "ヘイスト";
		case FFXIString.TOKEN_SLOW:
			return "スロウ";
		case FFXIString.TOKEN_SUBTLE_BLOW:
			return "モクシャ";
		case FFXIString.TOKEN_STORE_TP:
			return "ストアTP";
		case FFXIString.TOKEN_EVASION:
			return "回避";

		case FFXIString.TOKEN_ACCURACY:
			return "命中";
		case FFXIString.TOKEN_ACCURACY_RANGE:
			return "飛命";
			
		case FFXIString.TOKEN_DOUBLE_ATTACK:
			return "ダブルアタック";
		case FFXIString.TOKEN_TRIPPLE_ATTACK:
			return "トリプルアタック";
		case FFXIString.TOKEN_QUAD_ATTACK:
			return "クワッドアタック";
		case FFXIString.TOKEN_ENMITY:
			return "敵対心";

		case FFXIString.TOKEN_CRITICAL_RATE:
			return "クリティカルヒット";
		case FFXIString.TOKEN_CRITICAL_DAMAGE:
			return "クリティカルヒットダメージ";
		case FFXIString.TOKEN_ACCURACY_MAGIC:
			return "魔法命中率";
		case FFXIString.TOKEN_ATTACK_MAGIC:
			return "魔法攻撃力";
		case FFXIString.TOKEN_ATTACK_MAGIC2:
			return "魔法攻撃力アップ";
		case FFXIString.TOKEN_DEFENCE:
			return "防";
		case FFXIString.TOKEN_DEFENCE_MAGIC:
			return "魔法防御力";
		case FFXIString.TOKEN_DEFENCE_MAGIC2:
			return "魔法防御力アップ";
		case FFXIString.TOKEN_DAMAGECUT:
			return "被ダメージ";
		case FFXIString.TOKEN_DAMAGECUT_PHYSICAL:
			return "被物理ダメージ";
		case FFXIString.TOKEN_DAMAGECUT_MAGIC:
			return "被魔法ダメージ";
		case FFXIString.TOKEN_DAMAGECUT_BREATH:
			return "被ブレスダメージ";
			
		case FFXIString.TOKEN_SKILL_HANDTOHAND:
			return "格闘スキル";
		case FFXIString.TOKEN_SKILL_DAGGER:
			return "短剣スキル";
		case FFXIString.TOKEN_SKILL_SWORD:
			return "片手剣スキル";
		case FFXIString.TOKEN_SKILL_GREATSWORD:
			return "両手剣スキル";
		case FFXIString.TOKEN_SKILL_AXE:
			return "片手斧スキル";
		case FFXIString.TOKEN_SKILL_GREATEAXE:
			return "両手斧スキル";
		case FFXIString.TOKEN_SKILL_SCYTH:
			return "両手鎌スキル";
		case FFXIString.TOKEN_SKILL_POLEARM:
			return "両手槍スキル";
		case FFXIString.TOKEN_SKILL_KATANA:
			return "片手刀スキル";
		case FFXIString.TOKEN_SKILL_GREATKATANA:
			return "両手刀スキル";
		case FFXIString.TOKEN_SKILL_CLUB:
			return "片手棍スキル";
		case FFXIString.TOKEN_SKILL_STAFF:
			return "両手棍スキル";
		case FFXIString.TOKEN_SKILL_ARCHERY:
			return "弓術スキル";
		case FFXIString.TOKEN_SKILL_MARKSMANSHIP:
			return "射撃スキル";
		case FFXIString.TOKEN_SKILL_THROWING:
			return "投てきスキル";
		case FFXIString.TOKEN_SKILL_SHIELD:
			return "盾スキル";
		case FFXIString.TOKEN_SKILL_GUARDING:
			return "ガードスキル";
		case FFXIString.TOKEN_SKILL_EVASION:
			return "回避スキル";
		case FFXIString.TOKEN_SKILL_PARRYING:
			return "受け流しスキル";

		case FFXIString.TOKEN_SKILL_HEALING_MAGIC:
			return "回復魔法スキル";
		case FFXIString.TOKEN_SKILL_ENCHANCING_MAGIC:
			return "強化魔法スキル";
		case FFXIString.TOKEN_SKILL_ENFEEBLING_MAGIC:
			return "弱体魔法スキル";
		case FFXIString.TOKEN_SKILL_ELEMENTAL_MAGIC:
			return "精霊魔法スキル";
		case FFXIString.TOKEN_SKILL_DIVINE_MAGIC:
			return "神聖魔法スキル";
		case FFXIString.TOKEN_SKILL_SINGING:
			return "歌唱スキル";
		case FFXIString.TOKEN_SKILL_WIND_INSTRUMENT:
			return "管楽器スキル";
		case FFXIString.TOKEN_SKILL_STRING_INSTRUMENT:
			return "弦楽器スキル";
		case FFXIString.TOKEN_SKILL_NINJUTSU:
			return "忍術スキル";
		case FFXIString.TOKEN_SKILL_SUMMONING:
			return "召喚魔法スキル";
		case FFXIString.TOKEN_SKILL_BLUE_MAGIC:
			return "青魔法スキル";
		case FFXIString.TOKEN_SKILL_DARK_MAGIC:
			return "暗黒魔法スキル";
			
		case FFXIString.TOKEN_REGIST_FIRE:
			return "耐火";
		case FFXIString.TOKEN_REGIST_ICE:
			return "耐氷";
		case FFXIString.TOKEN_REGIST_WIND:
			return "耐風";
		case FFXIString.TOKEN_REGIST_EARTH:
			return "耐土";
		case FFXIString.TOKEN_REGIST_LIGHTNING:
			return "耐雷";
		case FFXIString.TOKEN_REGIST_WATER:
			return "耐水";
		case FFXIString.TOKEN_REGIST_LIGHT:
			return "耐光";
		case FFXIString.TOKEN_REGIST_DARK:
			return "耐闇";
			
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
			return "全種";

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
			return "戦";
		case FFXIString.JOB_DB_MNK:
			return "モ";
		case FFXIString.JOB_DB_WHM:
			return "白";
		case FFXIString.JOB_DB_BLM:
			return "黒";
		case FFXIString.JOB_DB_RDM:
			return "赤";
		case FFXIString.JOB_DB_THF:
			return "シ";
		case FFXIString.JOB_DB_PLD:
			return "ナ";
		case FFXIString.JOB_DB_DRK:
			return "暗";
		case FFXIString.JOB_DB_BST:
			return "獣";
		case FFXIString.JOB_DB_BRD:
			return "吟";
		case FFXIString.JOB_DB_RNG:
			return "狩";
		case FFXIString.JOB_DB_SAM:
			return "侍";
		case FFXIString.JOB_DB_NIN:
			return "忍";
		case FFXIString.JOB_DB_DRG:
			return "竜";
		case FFXIString.JOB_DB_SMN:
			return "召";
		case FFXIString.JOB_DB_BLU:
			return "青";
		case FFXIString.JOB_DB_COR:
			return "コ";
		case FFXIString.JOB_DB_PUP:
			return "か";
		case FFXIString.JOB_DB_DNC:
			return "踊";
		case FFXIString.JOB_DB_SCH:
			return "学";
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
			return "主";
		case FFXIString.PART_DB_SUB:
			return "副";
		case FFXIString.PART_DB_HEAD:
			return "頭";
		case FFXIString.PART_DB_BODY:
			return "胴";
		case FFXIString.PART_DB_HANDS:
			return "両手";
		case FFXIString.PART_DB_FEET:
			return "両脚";
		case FFXIString.PART_DB_LEGS:
			return "両足";
		case FFXIString.PART_DB_BACK:
			return "背";
		case FFXIString.PART_DB_NECK:
			return "首";
		case FFXIString.PART_DB_WAIST:
			return "腰";
		case FFXIString.PART_DB_RING1:
			return "指";
		case FFXIString.PART_DB_RING2:
			return "指";
		case FFXIString.PART_DB_EAR1:
			return "耳";
		case FFXIString.PART_DB_EAR2:
			return "耳";
		case FFXIString.PART_DB_RANGE:
			return "遠隔";
		case FFXIString.PART_DB_ANMO:
			return "矢弾";
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
