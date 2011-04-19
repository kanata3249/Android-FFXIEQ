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
import com.github.kanata3249.ffxieq.android.db.FFXIEQSettings;

import android.app.Activity;
import android.support.v4.app.Fragment;

public class FFXIEQFragment extends Fragment {
	protected FFXIDAO getDAO() {
		Activity activity;
		
		activity = getActivity();
		return ((FFXIEQApplication)activity.getApplication()).getFFXIDatabase();
	}
	protected FFXIEQSettings getSettings() {
		Activity activity;
		
		activity = getActivity();
		return ((FFXIEQApplication)activity.getApplication()).getFFXIEQSettings();
	}

	public long getCharacterID() {
		Activity activity;
		
		activity = getActivity();
		return ((FFXIEQApplication)activity.getApplication()).getCharacterID();
	}

	public void setCharacterID(long characterID) {
		Activity activity;
		
		activity = getActivity();
		((FFXIEQApplication)activity.getApplication()).setCharacterID(characterID);
	}

	public long getCharacterIDToCompare() {
		Activity activity;
		
		activity = getActivity();
		return ((FFXIEQApplication)activity.getApplication()).getCharacterIDToCompare();
	}

	public void setCharacterIDToCompare(long characterID) {
		Activity activity;
		
		activity = getActivity();
		((FFXIEQApplication)activity.getApplication()).setCharacterIDToCompare(characterID);
	}

	public FFXICharacter getFFXICharacter() {
		Activity activity;
		
		activity = getActivity();
		return ((FFXIEQApplication)activity.getApplication()).getFFXICharacter();
	}

	public void setFFXICharacter(FFXICharacter charInfo) {
		Activity activity;
		
		activity = getActivity();
		((FFXIEQApplication)activity.getApplication()).setFFXICharacter(charInfo);
	}

	public FFXICharacter getFFXICharacterToCompare() {
		Activity activity;
		
		activity = getActivity();
		return ((FFXIEQApplication)activity.getApplication()).getFFXICharacterToCompare();
	}

	public void setFFXICharacterToCompare(FFXICharacter charInfo) {
		Activity activity;
		
		activity = getActivity();
		((FFXIEQApplication)activity.getApplication()).setFFXICharacterToCompare(charInfo);
	}
	
	static public abstract class OnDatasetChangedListener {
    	abstract public void notifyDatasetChanged();
    }
    OnDatasetChangedListener mListener;
}
