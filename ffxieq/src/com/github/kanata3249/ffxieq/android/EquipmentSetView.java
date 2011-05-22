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
import com.github.kanata3249.ffxieq.Equipment;
import com.github.kanata3249.ffxieq.EquipmentSet;
import com.github.kanata3249.ffxieq.FFXICharacter;
import com.github.kanata3249.ffxieq.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class EquipmentSetView extends ListView {
	EquipmentInfo mEquipments[];
	FFXIDAO mDao;
	
	public EquipmentSetView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public boolean bindFFXICharacter(FFXICharacter charinfo) {
		EquipmentSelectorAdapter adapter;

		mDao = FFXICharacter.getDao();
		mEquipments = new EquipmentInfo[EquipmentSet.EQUIPMENT_NUM];
		for (int i = 0; i < mEquipments.length; i++) {
			Equipment eq = charinfo.getEquipment(i);
			if (eq == null) {
				mEquipments[i] = new EquipmentInfo(i, -1, -1, "");
			} else {
				mEquipments[i] = new EquipmentInfo(i, eq.getId(), eq.getAugId(), eq.getName());
			}
		}
		adapter = new EquipmentSelectorAdapter(getContext(), R.layout.equipmentsetview, mEquipments);
		setAdapter(adapter);
		
		return true;
	}
	
	private class EquipmentInfo {
		long mEquipmentID;
		long mAugmentID;
		String mName;
		int mPart;

		public EquipmentInfo(int part, long id, long augId, String name) { mPart = part; mEquipmentID = id; mAugmentID = augId; mName = name;};

		public String toString() {
			StringBuilder sb = new StringBuilder();
			String [] parts = getContext().getResources().getStringArray(R.array.Parts);
			
			sb.append(parts[mPart]);
			sb.append(": ");
			if (mEquipmentID > 0) {
				sb.append(mName);
			}
			return sb.toString();
		}
	}
	private class EquipmentSelectorAdapter extends ArrayAdapter<EquipmentInfo> {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return super.getView(position, convertView, parent);
		}

		public EquipmentSelectorAdapter(Context context,
				int textViewResourceId, EquipmentInfo[] objects) {
			super(context, textViewResourceId, objects);
		}


	}
	
	public long getItemId(int position) {
		return mEquipments[position].mEquipmentID;
	}
	public long getItemAugId(int position) {
		return mEquipments[position].mAugmentID;
	}
}
