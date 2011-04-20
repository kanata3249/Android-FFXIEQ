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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.github.kanata3249.ffxieq.FFXICharacter;
import com.github.kanata3249.ffxieq.R;

public class CharacterEditFragment extends FFXIEQFragment {
	private View mView;
	private boolean mUpdating;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onStart() {
    	super.onStart();

   		updateValues();

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