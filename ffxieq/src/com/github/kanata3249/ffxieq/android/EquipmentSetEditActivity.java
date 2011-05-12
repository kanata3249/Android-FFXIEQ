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

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import com.github.kanata3249.ffxieq.R;

public class EquipmentSetEditActivity extends FFXIEQBaseActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.equipmentseteditactivity);

        notifyDatasetChanged();

        FragmentManager fm = getSupportFragmentManager();
        FFXIEQFragment.OnDatasetChangedListener listener = new FFXIEQFragment.OnDatasetChangedListener() {
			@Override
			public void notifyDatasetChanged() {
				EquipmentSetEditActivity.this.notifyDatasetChanged();
			}
		};

		{
			EquipmentSetEditFragment fragment = (EquipmentSetEditFragment)fm.findFragmentById(R.id.CharacterEdit);
	        if (fragment != null)
	        	fragment.setOnDatasetChangedListener(listener);
	    }

		{
			CharacterStatusFragment fragment = (CharacterStatusFragment)fm.findFragmentById(R.id.CharacterStatus);
	        if (fragment != null)
	        	fragment.setOnDatasetChangedListener(listener);
		}
		
		{
			BasicEditFragment fragment = (BasicEditFragment)fm.findFragmentById(R.id.BasicEdit);
	        if (fragment != null)
	        	fragment.setOnDatasetChangedListener(listener);
		}
    }

	public void notifyDatasetChanged() {
		FragmentManager fm = getSupportFragmentManager();

		{
			EquipmentSetEditFragment fragment = (EquipmentSetEditFragment)fm.findFragmentById(R.id.CharacterEdit);
	        if (fragment != null)
	        	fragment.updateValues();
	    }

		{
			CharacterStatusFragment fragment = (CharacterStatusFragment)fm.findFragmentById(R.id.CharacterStatus);
	        if (fragment != null)
	        	fragment.updateValues();
		}
		
		{
			BasicEditFragment fragment = (BasicEditFragment)fm.findFragmentById(R.id.BasicEdit);
	        if (fragment != null)
	        	fragment.updateValues();
		}
	}
	
	public static boolean hasAllView(View myview) {
        if (myview.findViewById(R.id.CharacterStatus)!= null)
        	return true;
        return false;
	}
	
	public void setDisplayParam(int param) {
		FragmentManager fm = getSupportFragmentManager();
		CharacterStatusFragment fragment = (CharacterStatusFragment)fm.findFragmentById(R.id.CharacterStatus);
        if (fragment != null)
        	fragment.setDisplayParam(param);
	}
}
