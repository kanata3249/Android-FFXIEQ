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
package com.github.kanata3249.ffxieq.android;

import com.github.kanata3249.ffxi.FFXIDAO;
import com.github.kanata3249.ffxieq.FFXICharacter;
import com.github.kanata3249.ffxieq.Magic;
import com.github.kanata3249.ffxieq.R;
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

public class MagicSetView extends ListView {
	MagicInfo mMagics[];
	FFXIDAO mDao;
	
	public MagicSetView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public boolean bindFFXICharacter(FFXICharacter charinfo) {
		MagicSelectorAdapter adapter;
		int nMagics, n;

		mDao = FFXICharacter.getDao();
		String[] columns = { MagicTable.C_Id, MagicTable.C_SubId, MagicTable.C_Name };
		Cursor cursor = ((FFXIDatabase)mDao).getMagicCursor(-1, columns, null, null);

		nMagics = Math.max(cursor != null ? cursor.getCount() : 0, charinfo.getNumMagic());
		mMagics = new MagicInfo[nMagics];
		n = 0;
		for (int i = 0; i < charinfo.getNumMagic(); i++) {
			Magic magic = charinfo.getMagic(i);
			if (magic != null) {
				long subid = magic.getSubId();
				
				mMagics[n++] = new MagicInfo(magic.getId(), subid, magic.getName());
			}
		}
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					long subid = cursor.getLong(cursor.getColumnIndex(MagicTable.C_SubId));
					int ii;
					for (ii = 0; ii < n; ii++) {
						if (mMagics[ii].mSubId == subid)
							break;
					}
					if (ii == n) {
						mMagics[n++] = new MagicInfo(-1, subid, cursor.getString(cursor.getColumnIndex(MagicTable.C_Name)));
					}
					cursor.moveToNext();
				}
			}
			cursor.close();
		}
		adapter = new MagicSelectorAdapter(getContext(), R.layout.magicsetview, mMagics);
		setAdapter(adapter);
		
		return true;
	}
	
	private class MagicInfo {
		long mID;
		long mSubId;
		String mName;

		public MagicInfo(long id, long subId, String name) { mID = id; mSubId = subId; mName = name;};

		public String toString() {
			if (mID >= 0)
				return mName;
			return "";
		}
	}
	private class MagicSelectorAdapter extends ArrayAdapter<MagicInfo> {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = super.getView(position, convertView, parent);
			
			if (mMagics[position].mID == -1)
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
	public long getItemSubId(int position) {
		return mMagics[position].mSubId;
	}
}
