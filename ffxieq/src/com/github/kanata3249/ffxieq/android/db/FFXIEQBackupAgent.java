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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import android.app.backup.BackupAgent;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.database.Cursor;
import android.os.ParcelFileDescriptor;


public class FFXIEQBackupAgent extends BackupAgent {
	FFXIEQSettings mSettings;
	
	private final String KEY_CHARNAME = "CharacterName";
	private final String KEY_CHARDATA = "CharacterData";
	private final String KEY_FILTER = "Filter";

	@Override
	public void onCreate() {
		mSettings = new FFXIEQSettings(getApplicationContext());
		super.onCreate();
	}

	@Override
	public void onBackup(ParcelFileDescriptor arg0, BackupDataOutput out,
			ParcelFileDescriptor arg2) throws IOException {
		{ // Characters
			Cursor cursor;
			String columns[] = { FFXIEQSettings.C_Id, FFXIEQSettings.C_Name, FFXIEQSettings.C_CharInfo };

			cursor = mSettings.getCharactersCursor(columns, FFXIEQSettings.C_Id);
			if (cursor != null) {
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					String name;
					byte bytes[];
					byte charInfo[];
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					DataOutputStream outWriter = new DataOutputStream(baos);
					
					name = cursor.getString(cursor.getColumnIndex(FFXIEQSettings.C_Name));
					charInfo = cursor.getBlob(cursor.getColumnIndex(FFXIEQSettings.C_CharInfo));
	
					outWriter.writeUTF(name);
					outWriter.writeInt(charInfo.length);
					outWriter.write(charInfo);
					outWriter.close();

					bytes = baos.toByteArray();
					out.writeEntityHeader(KEY_CHARDATA, bytes.length);
					out.writeEntityData(bytes, bytes.length);
					
					cursor.moveToNext();
				}
				
				cursor.close();
			}
		}
		
		{ // Filters
			Cursor cursor;
			String columns[] = { FFXIEQSettings.C_Id, FFXIEQSettings.C_Filter };

			cursor = mSettings.getFilterCursor(columns);
			if (cursor != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				DataOutputStream outWriter = new DataOutputStream(baos);

				cursor.moveToLast();
				outWriter.writeInt(cursor.getCount());
				for (int i = 0; i < cursor.getCount(); i++) {
					String filter;
					
					filter = cursor.getString(cursor.getColumnIndex(FFXIEQSettings.C_Filter));
					outWriter.writeUTF(filter);
						
					cursor.moveToPrevious();
				}
				
				cursor.close();

				outWriter.close();
				byte bytes[] = baos.toByteArray();

				out.writeEntityHeader(KEY_FILTER, bytes.length);
				out.writeEntityData(bytes, bytes.length);
			}
		}

	}

	@Override
	public void onRestore(BackupDataInput in, int arg1,
			ParcelFileDescriptor arg2) throws IOException {
		ByteArrayInputStream bais;
		DataInputStream inReader;

		try {
		while (in.readNextHeader()) {
			String key = in.getKey();
			int size = in.getDataSize();

			if (key.equals(KEY_CHARDATA)) {
				byte[] buffer = new byte[size];
				
				in.readEntityData(buffer, 0, size);

				bais = new ByteArrayInputStream(buffer);
				inReader = new DataInputStream(bais);
				
				String name = inReader.readUTF();
				int charInfoSize = inReader.readInt();
				byte charInfo[] = new byte[charInfoSize];
				inReader.read(charInfo, 0, charInfoSize);
				inReader.close();

				mSettings.saveCharInfo(-1, name, charInfo);
			} else if (key.equals(KEY_FILTER)) {
				byte[] buffer = new byte[size];

				in.readEntityData(buffer, 0, size);

				bais = new ByteArrayInputStream(buffer);
				inReader = new DataInputStream(bais);
				
				int count = inReader.readInt();
				for (int i = 0; i < count; i++) {
					String filter = inReader.readUTF();

					mSettings.addFilter(filter);
				}
				inReader.close();
			} else {
				in.skipEntityData();
			}
		}
		} catch (IOException e) {
			
		}
	}

}
