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

import java.util.ArrayList;

import com.github.kanata3249.ffxi.FFXIDAO;
import com.github.kanata3249.ffxieq.FFXICharacter;
import com.github.kanata3249.ffxieq.android.db.FFXIEQSettings;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class FFXIEQBaseActivity extends FragmentActivity {
	public FFXIEQBaseActivity() {
		super();
		
		mControlAndValues = new ArrayList<ControlAndValue>();
	}

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

	public ControlBindableValue[] getTemporaryValues() {
		return ((FFXIEQApplication)getApplication()).getTemporaryValues();
	}

	public void setTemporaryValues(ControlBindableValue []values) {
		((FFXIEQApplication)getApplication()).setTemporaryValues(values);
	}
	
	class ControlAndValue {
		int mControlId;
		ControlBindableValue mValue;
		
		ControlAndValue(int id, ControlBindableValue value) {
			mControlId = id;
			mValue = value;
		}
		
		int getControlID() {
			return mControlId;
		}
		ControlBindableValue getValue() {
			return mValue;
		}
	}
	
	ArrayList<ControlAndValue> mControlAndValues;
	
	protected void bindControlAndValue(int id, ControlBindableValue value) {
		ControlAndValue entry = new ControlAndValue(id, value);
		
		mControlAndValues.add(entry);
	}
	
	protected void updateValues() {
		for (int i = 0; i < mControlAndValues.size(); i++) {
			ControlAndValue entry = mControlAndValues.get(i);
			View control;
			ControlBindableValue value;
			
			value = entry.getValue();
			control = findViewById(entry.getControlID());
			if (control != null) {
				if (control instanceof EditText) {
					EditText et = (EditText)control;
					et.setText(value.getValue());
				} else if (control instanceof Spinner) {
					Spinner spin = (Spinner)control;
					spin.setSelection(Integer.valueOf(value.getValue()));
				}
			}
		}
	}
	
	protected void saveValues() {
		for (int i = 0; i < mControlAndValues.size(); i++) {
			ControlAndValue entry = mControlAndValues.get(i);
			View control;
			ControlBindableValue value;
			
			value = entry.getValue();
			control = findViewById(entry.getControlID());
			if (control != null) {
				if (control instanceof EditText) {
					EditText et = (EditText)control;
					value.setValue(et.getText().toString());
				} else if (control instanceof Spinner) {
					Spinner spin = (Spinner)control;
					
					value.setValue(((Integer)spin.getSelectedItemPosition()).toString());
				}
			}
		}
	}
}
