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
import com.github.kanata3249.ffxi.status.StatusModifier;
import com.github.kanata3249.ffxieq.FFXICharacter;
import com.github.kanata3249.ffxieq.android.db.FFXIDatabase;
import com.github.kanata3249.ffxieq.android.db.FFXIEQSettings;

import android.app.Application;

public class FFXIEQApplication extends Application {
	FFXIDatabase mFFXIDatabase;
	FFXIEQSettings mFFXIEQSettings;
	ControlBindableValue[] mTemporaryValues;
	
	long mCharacterID;
	FFXICharacter mFFXICharacter;

	public FFXIEQApplication() {
		mFFXIDatabase = null;
		mFFXIEQSettings = null;
		mCharacterID = -1;
	}

	public FFXIDAO getFFXIDatabase() {
		if (mFFXIDatabase == null) {
			mFFXIDatabase = new FFXIDatabase(this);
		}
		StatusModifier.setDao(mFFXIDatabase);
		FFXICharacter.setDao(mFFXIDatabase);
		return mFFXIDatabase;
	}

	public FFXIEQSettings getFFXIEQSettings() {
		getFFXIDatabase();
		if (mFFXIEQSettings == null) {
			mFFXIEQSettings = new FFXIEQSettings(this);
		}
		return mFFXIEQSettings;
	}

	public long getCharacterID() {
		if (mCharacterID == -1) {
        	mCharacterID = getFFXIEQSettings().getFirstCharacterId();
            mFFXICharacter = getFFXIEQSettings().loadCharInfo(mCharacterID);
		}
		return mCharacterID;
	}

	public void setCharacterID(long characterID) {
		this.mCharacterID = characterID;
	}

	public FFXICharacter getFFXICharacter() {
		if (mCharacterID == -1) {
        	mCharacterID = getFFXIEQSettings().getFirstCharacterId();
            mFFXICharacter = getFFXIEQSettings().loadCharInfo(mCharacterID);
		}
		return mFFXICharacter;
	}

	public void setFFXICharacter(FFXICharacter charInfo) {
		mFFXICharacter = charInfo;
	}

	public ControlBindableValue[] getTemporaryValues() {
		return mTemporaryValues;
	}
	public void setTemporaryValues(ControlBindableValue []values) {
		mTemporaryValues = values;
	}
}
