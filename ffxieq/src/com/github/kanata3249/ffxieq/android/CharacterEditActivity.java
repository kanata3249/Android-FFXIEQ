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

import java.io.IOException;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.kanata3249.ffxieq.FFXICharacter;
import com.github.kanata3249.ffxieq.R;
import com.github.kanata3249.ffxieq.android.db.FFXIDatabase;

public class CharacterEditActivity extends FFXIEQBaseActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.charactereditactivity);
        
        FragmentManager fm = getSupportFragmentManager();
        CharacterStatusFragment csf = (CharacterStatusFragment)fm.findFragmentById(R.id.CharacterStatus);
    	CharacterEditFragment cef = (CharacterEditFragment)fm.findFragmentById(R.id.CharacterEdit);

    	if (csf != null) {
        	cef = (CharacterEditFragment)fm.findFragmentById(R.id.CharacterEdit);
        	csf.setFFXICharacter(cef.getFFXICharacter());
        	csf.notifyDatasetChanged();
        }

    	if (cef != null) {
    		cef.setOnDatasetChangedListener(new FFXIEQFragment.OnDatasetChangedListener() {
    			public void notifyDatasetChanged() {
    		        FragmentManager fm = getSupportFragmentManager();
    		        CharacterStatusFragment csf = (CharacterStatusFragment)fm.findFragmentById(R.id.CharacterStatus);
    		    	CharacterEditFragment cef = (CharacterEditFragment)fm.findFragmentById(R.id.CharacterEdit);
    		        if (csf != null) {
    		        	csf.setFFXICharacter(cef.getFFXICharacter());
    		        	csf.notifyDatasetChanged();
    		        }
    			}
    		});
    	}
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainactivity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.Delete:
			showDialog(R.layout.querydeletecharacter);
			return true;
			
		case R.id.InstallDB:
			try {
				((FFXIDatabase)getDAO()).copyDatabaseFromAssets();
			} catch (IOException e) {
			}
			return true;

		case R.id.InstallDBFromSD:
			try {
				((FFXIDatabase)getDAO()).copyDatabaseFromSD();
			} catch (IOException e) {
			}
			return true;

		case R.id.ExportDBToSD:
			try {
				((FFXIDatabase)getDAO()).copyDatabaseToSD();
			} catch (IOException e) {
			}
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		Dialog dialog;

		switch (id) {
		case R.layout.querysavecharacter:
	    	dialog = new Dialog(this);
			dialog.setContentView(R.layout.querysavecharacter);
			dialog.setTitle(getString(R.string.QuerySaveCharacterTitle));

			return dialog;
		case R.layout.querydeletecharacter:
	    	dialog = new Dialog(this);
			dialog.setContentView(R.layout.querydeletecharacter);
			dialog.setTitle(getString(R.string.QueryDeleteCharacterTitle));
			return dialog;
		}
		return super.onCreateDialog(id, args);
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
        FragmentManager fm = getSupportFragmentManager();
    	final CharacterEditFragment cef = (CharacterEditFragment)fm.findFragmentById(R.id.CharacterEdit);
    	final long charid = cef.getCharacterID();
    	TextView tv;
    	Button btn;

		switch (id) {
		case R.layout.querysavecharacter:
			tv = (TextView)dialog.findViewById(R.id.CharacterName);
			tv.setText(cef.getName());
			CheckBox cb = (CheckBox)(dialog.findViewById(R.id.SaveNew));
			cb.setChecked(false);
			btn = (Button)dialog.findViewById(R.id.SaveOK);
			btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					TextView tv = (TextView)(v.getRootView().findViewById(R.id.CharacterName));
					String name = tv.getText().toString();
					CheckBox cb = (CheckBox)(v.getRootView().findViewById(R.id.SaveNew));
					long newid;
					
					newid = charid;
					if (cb.isChecked())
						newid = -1;
					newid = getSettings().saveCharInfo(newid, name, cef.getFFXICharacter());
					CharacterEditActivity.this.dismissDialog(R.layout.querysavecharacter);
					if (newid != charid || !name.equals(cef.getName())) {
						cef.loadFFXICharacter(newid);
					}
				}
			});
			return;
		case R.layout.querydeletecharacter:
			tv = (TextView)dialog.findViewById(R.id.CharacterName);
			tv.setText(cef.getName());
			btn = (Button)dialog.findViewById(R.id.DeleteOK);
			btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					getSettings().deleteCharInfo(cef.getCharacterID());
					CharacterEditActivity.this.dismissDialog(R.layout.querydeletecharacter);
					cef.loadFFXICharacter(getSettings().getFirstCharacterId());
				}
			});
			btn = (Button)dialog.findViewById(R.id.Cancel);
			btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					CharacterEditActivity.this.dismissDialog(R.layout.querydeletecharacter);
				}
			});
			return;
		}
		super.onPrepareDialog(id, dialog, args);
	}
	
	public static boolean hasStatusView(View myview) {
        if (myview.findViewById(R.id.CharacterStatus)!= null)
        	return true;
        return false;
	}
    
    public FFXICharacter getFFXICharacter() {
        FragmentManager fm = getSupportFragmentManager();
    	CharacterEditFragment cef = (CharacterEditFragment)fm.findFragmentById(R.id.CharacterEdit);

    	return cef.getFFXICharacter();
    }
}
