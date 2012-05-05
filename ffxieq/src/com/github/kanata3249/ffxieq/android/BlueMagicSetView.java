/*
   Copyright 2012 kanata3249

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
package com.github.kanata3249.ffxieq.android;

import com.github.kanata3249.ffxi.FFXIDAO;
import com.github.kanata3249.ffxieq.BlueMagic;
import com.github.kanata3249.ffxieq.FFXICharacter;
import com.github.kanata3249.ffxieq.R;
import com.github.kanata3249.ffxieq.android.db.BlueMagicTable;
import com.github.kanata3249.ffxieq.android.db.FFXIDatabase;
import com.github.kanata3249.ffxieq.android.db.MagicTable;

import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class BlueMagicSetView extends ListView {
	MagicInfo mMagics[];
	FFXIDAO mDao;
	
	public BlueMagicSetView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public boolean bindFFXICharacter(FFXICharacter charinfo) {
		MagicSelectorAdapter adapter;
		int nMagics, n;

		mDao = FFXICharacter.getDao();
		String[] columns = { BlueMagicTable.C_Id, BlueMagicTable.C_Name };
		Cursor cursor = ((FFXIDatabase)mDao).getBlueMagicCursor(columns, null);

		nMagics = Math.max(cursor != null ? cursor.getCount() : 0, charinfo.getNumBlueMagic());
		mMagics = new MagicInfo[nMagics];
		n = 0;
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					long id = cursor.getLong(cursor.getColumnIndex(MagicTable.C_Id));
					int ii;
					boolean found;

					found = false;
					for (ii = 0; ii < charinfo.getNumBlueMagic(); ii++) {
						BlueMagic magic = charinfo.getBlueMagic(ii);
						if (magic.getId() == id) {
							found = true;
						}
					}
					mMagics[n++] = new MagicInfo(id, found, cursor.getString(cursor.getColumnIndex(BlueMagicTable.C_Name)));
					cursor.moveToNext();
				}
			}
			cursor.close();
		}
		adapter = new MagicSelectorAdapter(getContext(), R.layout.bluemagicsetview, mMagics);
		setAdapter(adapter);
		
		return true;
	}
	
	private class MagicInfo {
		long mID;
		String mName;
		boolean mEnable;

		public MagicInfo(long id, boolean enable, String name) { mID = id; mEnable = enable; mName = name;};

		public String toString() {
			if (mEnable)
				return mName;
			return "";
		}
	}
	private class MagicSelectorAdapter extends ArrayAdapter<MagicInfo> {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = super.getView(position, convertView, parent);
			
			if (mMagics[position].mEnable == false)
				((TextView)v).setHint(mMagics[position].mName);
			return v;
		}

		public MagicSelectorAdapter(Context context,
				int textViewResourceId, MagicInfo[] objects) {
			super(context, textViewResourceId, objects);
		}


	}
	
	public long getItemId(int position) {
		return mMagics[position].mID;
	}
}
