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

import java.io.File;

import com.github.kanata3249.ffxi.FFXIDAO;
import com.github.kanata3249.ffxi.status.StatusModifier;
import com.github.kanata3249.ffxieq.FFXICharacter;
import com.github.kanata3249.ffxieq.R;
import com.github.kanata3249.ffxieq.android.db.FFXIDatabase;
import com.github.kanata3249.ffxieq.android.db.FFXIEQSettings;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class FFXIEQApplication extends Application {
	FFXIDatabase mFFXIDatabase;
	FFXIEQSettings mFFXIEQSettings;
	ControlBindableValue[] mTemporaryValues;
	
	long mCharacterID;
	FFXICharacter mFFXICharacter;
	long mCharacterIDToCompare;
	FFXICharacter mFFXICharacterToCompare;

	static public abstract class OnDatasetChangedListener {
    	abstract public void notifyDatasetChanged();
    }
	OnDatasetChangedListener mListener;

	public FFXIEQApplication() {
		mFFXIDatabase = null;
		mFFXIEQSettings = null;
		mCharacterID = -1;
		mCharacterIDToCompare = -1;
	}

	public FFXIDAO getFFXIDatabase() {
		if (mFFXIEQSettings == null) {
			mFFXIEQSettings = new FFXIEQSettings(this);
		}
		if (mFFXIDatabase == null) {
			mFFXIDatabase = new FFXIDatabase(this, mFFXIEQSettings.useExternalDB(), mFFXIEQSettings);
		}
		StatusModifier.setDao(mFFXIDatabase);
		FFXICharacter.setDao(mFFXIDatabase);
		return mFFXIDatabase;
	}

	public FFXIEQSettings getFFXIEQSettings() {
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

	public long getCharacterIDToCompare() {
		return mCharacterIDToCompare;
	}

	public void setCharacterIDToCompare(long characterID) {
		this.mCharacterIDToCompare = characterID;
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

	public FFXICharacter getFFXICharacterToCompare() {
		if (mCharacterID == -1) {
        	return null;
		}
		return mFFXICharacterToCompare;
	}

	public void setFFXICharacterToCompare(FFXICharacter charInfo) {
		mFFXICharacterToCompare = charInfo;
	}
	public ControlBindableValue[] getTemporaryValues() {
		return mTemporaryValues;
	}
	public void setTemporaryValues(ControlBindableValue []values) {
		mTemporaryValues = values;
	}

	@Override
	public File getDatabasePath(String name) {
		if (name.equals(FFXIDatabase.DB_NAME)) {
			name = FFXIDatabase.getDBPath(getFFXIEQSettings().useExternalDB());
		}
		return super.getDatabasePath(name);
	}

	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory) {
		if (name.equals(FFXIDatabase.DB_NAME)) {
			name = FFXIDatabase.getDBPath(getFFXIEQSettings().useExternalDB());
		}
		return super.openOrCreateDatabase(name, mode, factory);
	}
	
	public String getJobAndLevelString(FFXICharacter charInfo) {
		String jobstr, levelstr;
		String jobnames[] = getResources().getStringArray(R.array.Jobs);
		int job, joblevel;
		int subjob, subjoblevel;
		
		job = charInfo.getJob();
		joblevel = charInfo.getJobLevel();
		subjob = charInfo.getSubJob();
		subjoblevel = charInfo.getSubJobLevel();
		
		jobstr = jobnames[job];
		levelstr = Integer.valueOf(joblevel).toString();
		if (subjoblevel != 0) {
			jobstr += "/" + jobnames[subjob];
			levelstr += "/" + subjoblevel;
		}
		
		return jobstr + levelstr + (charInfo.isModified() ? "*" : "");
	}
	public CharSequence getCaption() {
		String main, sub;
		
		if (mFFXICharacter != null) {
			main = getJobAndLevelString(mFFXICharacter);
			if (mFFXICharacterToCompare != null) {
				sub = getJobAndLevelString(mFFXICharacterToCompare);
				return main + " vs " + sub;
			} else {
				return main;
			}
			
		}
		return "ffxieq";
	}

	public void setOnDatasetChangedListener(OnDatasetChangedListener listener) {
    	mListener = listener;
    }

	public void datasetChanged() {
    	if (mListener != null) {
    		mListener.notifyDatasetChanged();
    	}
    }
}
