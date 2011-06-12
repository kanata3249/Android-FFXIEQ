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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.github.kanata3249.ffxieq.Equipment;
import com.github.kanata3249.ffxieq.EquipmentSet;
import com.github.kanata3249.ffxieq.FFXICharacter;
import com.github.kanata3249.ffxieq.R;

public class EquipmentSetEditFragment extends FFXIEQFragment {
	private View mView;
	private boolean mUpdating;
	int mLongClickingItemPosition;
	private long mPartsToBeReselect[];

    @Override
    public void onStart() {
    	super.onStart();

   		FFXICharacter charInfo = getFFXICharacter();
   		View v = getView();

        // setup controls
        {
        	EquipmentSetView es;
        	
        	es = (EquipmentSetView)v.findViewById(R.id.Equipments);
        	if (es != null) {
        		es.bindFFXICharacter(charInfo);
            	es.setOnItemClickListener(new OnItemClickListener() {
    				public void onItemClick(AdapterView<?> arg0, View arg1,
    						int arg2, long arg3) {
    					EquipmentSelectorActivity.startActivity(EquipmentSetEditFragment.this, 0, getFFXICharacter(), arg2, ((EquipmentSetView)arg0).getItemId(arg2), ((EquipmentSetView)arg0).getItemAugId(arg2));
    				}
            	});
            	es.setOnItemLongClickListener(new OnItemLongClickListener() {

					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						mLongClickingItemPosition = arg2;
						getActivity().openContextMenu(arg0);
						return true;
					}
            		
            	});
    			registerForContextMenu(es);
    		}
        }
    }
    @Override
	public void onStop() {
		View v = getView();

        // reset listeners
        {
        	EquipmentSetView es;
        	
        	es = (EquipmentSetView)v.findViewById(R.id.Equipments);
        	if (es != null) {
            	es.setOnItemClickListener(null);
        	}
        }
		super.onStop();
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result;

        result = mView = inflater.inflate(R.layout.equipmentseteditfragment, container, false);

        return result;
    }
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	FFXICharacter charInfo = getFFXICharacter();
		if (resultCode == Activity.RESULT_OK) {
			int part;
			long id, augid;
			if (EquipmentSelectorActivity.isComeFrom(data)) {
				part = EquipmentSelectorActivity.getPart(data);
				id = EquipmentSelectorActivity.getEquipmentId(data);
				augid = EquipmentSelectorActivity.getAugmentId(data);
			} else if (AugmentSelectorActivity.isComeFrom(data)) {
				part = AugmentSelectorActivity.getPart(data);
				id = AugmentSelectorActivity.getEquipmentId(data);
				augid = AugmentSelectorActivity.getAugmentId(data);
			} else if (AugmentEditActivity.isComeFrom(data)) {
				part = AugmentEditActivity.getPart(data);
				id = AugmentEditActivity.getEquipmentId(data);
				augid = AugmentEditActivity.getAugmentId(data);
			} else {
				part = -1;
				id = augid = -1;
			}
				
			if (part != -1) {
				charInfo.setEquipment(part, id, augid);
				charInfo.reloadAugmentsIfChangesThere();
				updateValues();

				if (mListener != null) {
					mListener.notifyDatasetChanged();
				}
			}
		} else if (resultCode == Activity.RESULT_CANCELED) {
			if (charInfo.reloadAugmentsIfChangesThere()) {
				updateValues();
				if (mListener != null) {
					mListener.notifyDatasetChanged();
				}
			}
		} else if (resultCode == Activity.RESULT_FIRST_USER) {
			int part;
			EquipmentSetView sv = (EquipmentSetView)getView().findViewById(R.id.Equipments);
			if (sv != null) {
				if (EquipmentSelectorActivity.isComeFrom(data)) {
					part = EquipmentSelectorActivity.getPart(data);
					AugmentSelectorActivity.startActivity(EquipmentSetEditFragment.this, 0, getFFXICharacter(), part, sv.getItemId(part), sv.getItemAugId(part));
				} else if (AugmentSelectorActivity.isComeFrom(data)) {
					part = AugmentSelectorActivity.getPart(data);
					EquipmentSelectorActivity.startActivity(EquipmentSetEditFragment.this, 0, getFFXICharacter(), part, sv.getItemId(part), sv.getItemAugId(part));
				}
			}			
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.Remove:
			getFFXICharacter().setEquipment(mLongClickingItemPosition, -1, -1);
			updateValues();
	        if (mListener != null) {
	    		mListener.notifyDatasetChanged();
	    	}
			return true;
		case R.id.RemoveAll:
			FFXICharacter charInfo = getFFXICharacter();
			
			for (int i = 0; i < EquipmentSet.EQUIPMENT_NUM; i++) {
				charInfo.setEquipment(i, -1, -1);
			}
			updateValues();
	        if (mListener != null) {
	    		mListener.notifyDatasetChanged();
	    	}
			return true;
		case R.id.EditAugment:
			{
				Equipment eq = getFFXICharacter().getEquipment(mLongClickingItemPosition);
				if (eq != null) {
					AugmentEditActivity.startActivity(this, 0, getFFXICharacter(), mLongClickingItemPosition, eq.getId(), eq.getAugId());
				}				
				return true;
			}
		case R.id.EquipmentList:
			{
				Equipment eq = getFFXICharacter().getEquipment(mLongClickingItemPosition);
				long current, augid;
	
				current = augid = -1;
				if (eq != null) {
					current = eq.getId();
					augid = eq.getAugId();
				}
				EquipmentSelectorActivity.startActivity(EquipmentSetEditFragment.this, 0, getFFXICharacter(), mLongClickingItemPosition, current, augid);
				return true;
			}
		case R.id.AugmentList:
			{
				Equipment eq = getFFXICharacter().getEquipment(mLongClickingItemPosition);
				long current, augid;
	
				current = augid = -1;
				if (eq != null) {
					current = eq.getId();
					augid = eq.getAugId();
				}
				AugmentSelectorActivity.startActivity(EquipmentSetEditFragment.this, 0, getFFXICharacter(), mLongClickingItemPosition, current, augid);
				return true;
			}
		}

		// Web Search
		Equipment eq = getFFXICharacter().getEquipment(mLongClickingItemPosition);
		Intent intent;
		if (eq != null) {
			String[] urls = getResources().getStringArray(R.array.SearchURIs);
			String name = eq.getName();
			String url;

			url = null;
			switch (item.getItemId()) {
			case R.id.WebSearch0:
				url = urls[0];
				break;
			case R.id.WebSearch1:
				url = urls[1];
				break;
			case R.id.WebSearch2:
				url = urls[2];
				break;
			case R.id.WebSearch3:
				url = urls[3];
				break;
			case R.id.WebSearch4:
				url = urls[4];
				break;
			case R.id.WebSearch5:
				url = urls[5];
				break;
			case R.id.WebSearch6:
				url = urls[6];
				break;
			case R.id.WebSearch7:
				url = urls[7];
				break;
			default:
				url = null;
				break;
			}
			if (url != null) {
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url + Uri.encode(name.split("\\+")[0])));
				startActivity(intent);
				return true;
			}
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.equipmentset_context, menu);
		
		Equipment eq = getFFXICharacter().getEquipment(mLongClickingItemPosition);
		MenuItem item;
		
		item = menu.findItem(R.id.EditAugment);
		if (item != null)
			item.setEnabled(eq != null);
		item = menu.findItem(R.id.Remove);
		if (item != null)
			item.setEnabled(eq != null);
		item = menu.findItem(R.id.WebSearch0);
		if (item != null)
			item.setEnabled(eq != null);
		item = menu.findItem(R.id.WebSearch1);
		if (item != null)
			item.setEnabled(eq != null);
		item = menu.findItem(R.id.WebSearch2);
		if (item != null)
			item.setEnabled(eq != null);
		item = menu.findItem(R.id.WebSearch3);
		if (item != null)
			item.setEnabled(eq != null);
		item = menu.findItem(R.id.WebSearch4);
		if (item != null)
			item.setEnabled(eq != null);
		item = menu.findItem(R.id.WebSearch5);
		if (item != null)
			item.setEnabled(eq != null);
		item = menu.findItem(R.id.WebSearch6);
		if (item != null)
			item.setEnabled(eq != null);
		item = menu.findItem(R.id.WebSearch7);
		if (item != null)
			item.setEnabled(eq != null);
	}

	public void updateValues() {
    	FFXICharacter charInfo = getFFXICharacter();

        if (mUpdating)
        	return;
        mUpdating = true;

    	EquipmentSetView es;
    	
    	es = (EquipmentSetView)mView.findViewById(R.id.Equipments);
    	if (es != null) {
    		es.bindFFXICharacter(charInfo);
    	}
    	
    	mUpdating = false;

    	mPartsToBeReselect = charInfo.reloadForUpdatingDatabase();
    	if (mPartsToBeReselect[EquipmentSet.EQUIPMENT_NUM] == 1) {
			if (mListener != null) {
				mListener.notifyDatasetChanged();
			}
    	}
        for (int i = 0; i < EquipmentSet.EQUIPMENT_NUM; i++) {
        	if (mPartsToBeReselect[i] >= 0) {
        		getActivity().showDialog(R.string.EquipmentNotFound);
        		break;
        	}
        }
    }
	
	public void startReselect() {
		if (mPartsToBeReselect != null) {
			for (int i = 0; i < EquipmentSet.EQUIPMENT_NUM; i++) {
				if (mPartsToBeReselect[i] >= 0) {
					mPartsToBeReselect[i] = -1;
					
					EquipmentSelectorActivity.startActivity(this, 0, getFFXICharacter(), i, -1, -1);
					break;
				}
			}
			
		}
	}
	
	public void cancelReselect() {
		mPartsToBeReselect = null;
	}
}