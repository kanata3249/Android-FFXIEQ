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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import com.github.kanata3249.ffxieq.FFXICharacter;
import com.github.kanata3249.ffxieq.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

public class FFXIEQSettings extends SQLiteOpenHelper {
	private static final String DB_NAME = "ffxisettings";
	private static final String TABLE_NAME_CHARINFO = "Characters";
	public static final String C_Id = "_id";
	public static final String C_Name = "Name";
	public static final String C_CharInfo = "CharInfo";
	private static final String TABLE_NAME_FILTERS = "Filters";
	public static final String C_Filter = "Filter";
	public static final String C_LastUsed = "LastUsed";
	private static final int MAX_FILTERS = 16;
	
	Context mContext;

	// Constructor
	public FFXIEQSettings(Context context) {
		super(context, DB_NAME, null, 1);
		
		mContext = context;
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.beginTransaction();
		
		try {
			StringBuilder createSql = new StringBuilder();
			
			createSql.append("create table " + TABLE_NAME_CHARINFO + " (");
			createSql.append(C_Id + " integer primary key autoincrement not null,");
			createSql.append(C_Name + " text not null,");
			createSql.append(C_CharInfo + " blob");
			createSql.append(")");
			
			db.execSQL(createSql.toString());

			createSql.setLength(0);
			createSql.append("create table " + TABLE_NAME_FILTERS + " (");
			createSql.append(C_Id + " integer primary key autoincrement not null,");
			createSql.append(C_Filter + " text not null,");
			createSql.append(C_LastUsed + " integer not null");
			createSql.append(")");
			
			db.execSQL(createSql.toString());
			
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
	public long getFirstCharacterId() {
		Cursor cursor;
		String []columns = { C_Id };
		SQLiteDatabase db;
		long id;

		db = getReadableDatabase();
		cursor = db.query(TABLE_NAME_CHARINFO, columns, null, null, null, null, null, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			id = cursor.getLong(cursor.getColumnIndex(C_Id));
			cursor.close();
		} else {
			cursor.close();

			// Create new character
			String name = mContext.getString(R.string.NewCharacterName);
			id = saveCharInfo(-1, name, new FFXICharacter());
		}
		
		return id;
	}

	public FFXICharacter loadCharInfo(long id) {
		Cursor cursor;
		String []columns = { C_CharInfo };
		SQLiteDatabase db;
		byte [] chardata;
		FFXICharacter charInfo;

		db = getReadableDatabase();
		cursor = db.query(TABLE_NAME_CHARINFO, columns, C_Id + " = '" + id + "'", null, null, null, null, null);
		
		if (cursor.getCount() < 1) {
			// no matched row in table
			cursor.close();
			return null;
		}
		cursor.moveToFirst();
		chardata = cursor.getBlob(0);
		cursor.close();

		charInfo = null;
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(chardata);
			ObjectInputStream ois = new ObjectInputStream(bais);
			charInfo = (FFXICharacter)ois.readObject();
		} catch (StreamCorruptedException e) {
		} catch (ClassNotFoundException e) {
		} catch (IOException e) {
		}
		return charInfo;
	}
	public long saveCharInfo(long id, String name, FFXICharacter charInfo) {
		SQLiteDatabase db;
		ContentValues values = new ContentValues();;
		byte [] chardata;
		long newId;
		
		try {
			ObjectOutputStream oos;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();;

			oos = new ObjectOutputStream(baos);
			oos.writeObject(charInfo);
			oos.close();
			chardata = baos.toByteArray();
			baos.close();
		} catch (IOException e) {
			return -1;
		}
		db = getWritableDatabase();

		values.put(C_Name, name);
		values.put(C_CharInfo, chardata);
		db.beginTransaction();
		newId = id;
		try {
			if (id >= 0) {
				db.update(TABLE_NAME_CHARINFO, values, C_Id + " ='" + id + "'", null);
			} else {
				newId = db.insert(TABLE_NAME_CHARINFO, null, values);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		return newId;
	}
	
	public void deleteCharInfo(long id) {
		SQLiteDatabase db;

		db = getWritableDatabase();
		db.delete(TABLE_NAME_CHARINFO, C_Id + " = '" + id + "'", null);

		return;
	}

	public Cursor getCharactersCursor(String [] columns, String orderBy) {
		return getReadableDatabase().query(TABLE_NAME_CHARINFO, columns, null, null, null, null, orderBy);
	}
	
	public String getCharacterName(long id) {
		String []columns = { C_Name };
		String name;
		Cursor cursor = getReadableDatabase().query(TABLE_NAME_CHARINFO, columns, C_Id + "='" + id + "'", null, null, null, null, null);
		if (cursor.getCount() != 1) {
			cursor.close();
			return "";
		}
		cursor.moveToFirst();
		name = cursor.getString(cursor.getColumnIndex(C_Name));
		cursor.close();
		
		return name;
	}

	public Cursor getFilterCursor(String [] columns) {
		SQLiteDatabase db;
		
		db = getWritableDatabase();
		db.beginTransaction();
		
		try {
			StringBuilder createSql = new StringBuilder();
			
			createSql.append("create table " + TABLE_NAME_FILTERS + " (");
			createSql.append(C_Id + " integer primary key autoincrement not null,");
			createSql.append(C_Filter + " text not null,");
			createSql.append(C_LastUsed + " integer not null");
			createSql.append(")");
			
			db.execSQL(createSql.toString());
			db.setTransactionSuccessful();
		} catch (SQLiteException e) {
			/* ignore */
		} finally {
			db.endTransaction();
		}

		return getReadableDatabase().query(TABLE_NAME_FILTERS, columns, null, null, null, null, C_LastUsed + " DESC");
	}

	public long addFilter(String filter) {
		ContentValues values = new ContentValues();
		SQLiteDatabase db = getWritableDatabase();
		long id;

		values.put(C_Filter, filter);
		values.put(C_LastUsed, System.currentTimeMillis());
		db.beginTransaction();
		id = -1;
		try {
			id = db.insert(TABLE_NAME_FILTERS, null, values);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		
		removeOldFilters();
		return id;
	}
	
	public void removeOldFilters() {
		Cursor cursor;
		SQLiteDatabase db = getWritableDatabase();
		String [] columns = { C_Id };


		cursor = getFilterCursor(columns);
		if (cursor.getCount() > MAX_FILTERS) {
			int i;
			
			cursor.moveToFirst();
			for (i = 0; i < MAX_FILTERS; i++) {
				cursor.moveToNext();
			}
			for ( ; i < cursor.getCount(); i++) {
				db.delete(TABLE_NAME_FILTERS, C_Id + " = '" + cursor.getLong(cursor.getColumnIndex(C_Id)), null);
				cursor.moveToNext();
			}
		}
	}
	public void useFilter(long id) {
		ContentValues values = new ContentValues();
		SQLiteDatabase db = getWritableDatabase();

		values.put(C_LastUsed, System.currentTimeMillis());
		db.beginTransaction();
		try {
			db.update(TABLE_NAME_FILTERS, values, C_Id + " ='" + id + "'", null);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}
	
	public String getFilter(long id) {
		String []columns = { C_Filter };
		String filter;
		Cursor cursor = getReadableDatabase().query(TABLE_NAME_FILTERS, columns, C_Id + "='" + id + "'", null, null, null, null, null);
		if (cursor.getCount() != 1) {
			cursor.close();
			return "";
		}
		cursor.moveToFirst();
		filter = cursor.getString(cursor.getColumnIndex(C_Filter));
		cursor.close();
		
		return filter;
	}
}

