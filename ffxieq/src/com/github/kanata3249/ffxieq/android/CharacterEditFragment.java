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

public class CharacterEditFragment extends FFXIEQFragment {
	private View mView;
	private boolean mUpdating;
	int mLongClickingItemPosition;

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
    					EquipmentSelectorActivity.startActivity(CharacterEditFragment.this, 0, getFFXICharacter(), arg2, ((EquipmentSetView)arg0).getItemId(arg2));
    				}
            	});
            	es.setOnItemLongClickListener(new OnItemLongClickListener() {

					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						mLongClickingItemPosition = arg2;
						getActivity().openContextMenu(arg0);						// TODO Auto-generated method stub
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

        result = mView = inflater.inflate(R.layout.charactereditfragment, container, false);

        return result;
    }
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	FFXICharacter charInfo = getFFXICharacter();
		if (resultCode == Activity.RESULT_OK) {
			if (EquipmentSelectorActivity.isComeFrom(data)) {
				int part = EquipmentSelectorActivity.getPart(data);
				long id = EquipmentSelectorActivity.getEquipmentId(data);
				
				if (part != -1) {
					charInfo.setEquipment(part, id);
				}
		        updateValues();

		        if (mListener != null) {
		    		mListener.notifyDatasetChanged();
		    	}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.Remove:
			getFFXICharacter().setEquipment(mLongClickingItemPosition, -1);
			updateValues();
	        if (mListener != null) {
	    		mListener.notifyDatasetChanged();
	    	}
			return super.onContextItemSelected(item);
		case R.id.RemoveAll:
			FFXICharacter charInfo = getFFXICharacter();
			
			for (int i = 0; i < EquipmentSet.EQUIPMENT_NUM; i++) {
				charInfo.setEquipment(i, -1);
			}
			updateValues();
	        if (mListener != null) {
	    		mListener.notifyDatasetChanged();
	    	}
			return super.onContextItemSelected(item);
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
    }
}