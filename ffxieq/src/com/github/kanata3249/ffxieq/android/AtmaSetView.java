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
import com.github.kanata3249.ffxieq.Atma;
import com.github.kanata3249.ffxieq.AtmaSet;
import com.github.kanata3249.ffxieq.FFXICharacter;
import com.github.kanata3249.ffxieq.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AtmaSetView extends ListView {
	AtmaInfo mAtmas[];
	FFXIDAO mDao;
	
	public AtmaSetView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public boolean bindFFXICharacter(FFXICharacter charinfo) {
		AtmaSelectorAdapter adapter;

		mDao = FFXICharacter.getDao();
		mAtmas = new AtmaInfo[AtmaSet.ATMA_MAX];
		for (int i = 0; i < mAtmas.length; i++) {
			Atma eq = charinfo.getAtma(i);
			if (eq == null) {
				mAtmas[i] = new AtmaInfo(-1, "");
			} else {
				mAtmas[i] = new AtmaInfo(eq.getId(), eq.getName());
			}
		}
		adapter = new AtmaSelectorAdapter(getContext(), R.layout.atmasetview, mAtmas);
		setAdapter(adapter);
		
		return true;
	}
	
	private class AtmaInfo {
		long mID;
		String mName;

		public AtmaInfo(long id, String name) { mID = id; mName = name;};

		public String toString() {
			if (mID > 0) {
				return mName;
			}
			return "";
		}
	}
	private class AtmaSelectorAdapter extends ArrayAdapter<AtmaInfo> {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return super.getView(position, convertView, parent);
		}

		public AtmaSelectorAdapter(Context context,
				int textViewResourceId, AtmaInfo[] objects) {
			super(context, textViewResourceId, objects);
		}


	}
	
	public long getItemId(int position) {
		return mAtmas[position].mID;
	}
}
