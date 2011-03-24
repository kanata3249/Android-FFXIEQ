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

import android.support.v4.app.FragmentActivity;

public class FFXIEQBaseActivity extends FragmentActivity {
	protected FFXIDAO getDAO() {
		return ((FFXIEQApplication)getApplication()).getFFXIDatabase();
	}
	protected FFXIEQSettings getSettings() {
		return ((FFXIEQApplication)getApplication()).getFFXIEQSettings();
	}

	public long getCharacterID() {
		return ((FFXIEQApplication)getApplication()).getCharacterID();
	}

	public void setCharacterID(long characterID) {
		((FFXIEQApplication)getApplication()).setCharacterID(characterID);
	}

	public FFXICharacter getFFXICharacter() {
		return ((FFXIEQApplication)getApplication()).getFFXICharacter();
	}

	public void setFFXICharacter(FFXICharacter charInfo) {
		((FFXIEQApplication)getApplication()).setFFXICharacter(charInfo);
	}
}
