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

public class MagicSetEditFragment extends FFXIEQFragment {
	private View mView;
	private boolean mUpdating;
    
    @Override
    public void onStart() {
    	super.onStart();

   		FFXICharacter charInfo = getFFXICharacter();
   		View v = getView();

        // setup controls
        {
        	MagicSetView ms;
        	
        	ms = (MagicSetView)v.findViewById(R.id.Magics);
        	if (ms != null) {
        		ms.bindFFXICharacter(charInfo);
            	ms.setOnItemClickListener(new OnItemClickListener() {
    				public void onItemClick(AdapterView<?> arg0, View arg1,
    						int arg2, long arg3) {
    					MagicSelectorActivity.startActivity(MagicSetEditFragment.this, 0, getFFXICharacter(), arg2, ((MagicSetView)arg0).getItemId(arg2), ((MagicSetView)arg0).getItemSubId(arg2));
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
        	MagicSetView ms;
        	
        	ms = (MagicSetView)v.findViewById(R.id.Magics);
        	if (ms != null) {
            	ms.setOnItemClickListener(null);
        	}
        }
        
        super.onStop();
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result;

        result = mView = inflater.inflate(R.layout.magicseteditfragment, container, false);

        return result;
    }
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	FFXICharacter charInfo = getFFXICharacter();
		if (resultCode == Activity.RESULT_OK) {
			if (MagicSelectorActivity.isComeFrom(data)) {
				long id = MagicSelectorActivity.getMagicId(data);
				charInfo.setMagic(id, MagicSelectorActivity.getMagicSubId(data));
		        saveAndUpdateValues();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

    private void saveAndUpdateValues() {
        if (mUpdating)
        	return;
        if (getActivity() == null) {
        	return;
        }

    	updateValues();

    	if (mListener != null) {
    		mListener.notifyDatasetChanged();
    	}
    }

    public void updateValues() {
        FFXICharacter charInfo = getFFXICharacter();

        if (mUpdating)
        	return;
        mUpdating = true;

    	MagicSetView ms;
    	
    	ms = (MagicSetView)mView.findViewById(R.id.Magics);
    	if (ms != null) {
    		ms.bindFFXICharacter(charInfo);
    	}

    	mUpdating = false;
    }
}
