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

import com.github.kanata3249.ffxi.FFXIDAO;
import com.github.kanata3249.ffxieq.FFXICharacter;
import com.github.kanata3249.ffxieq.R;
import com.github.kanata3249.ffxieq.android.db.FFXIDatabase;
import com.github.kanata3249.ffxieq.android.db.FFXIEQSettings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class FFXIEQActivity extends TabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.ffxieqactivity);

        Resources res = getResources();
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		
		intent = new Intent().setClass(this, CharacterEditActivity.class);
		spec = tabHost.newTabSpec("equipment").setIndicator("Equipment", res.getDrawable(android.R.drawable.ic_menu_edit));
		spec.setContent(intent);
		tabHost.addTab(spec);
		
		if (!CharacterEditActivity.hasAllView(tabHost.getCurrentView())) {
			intent = new Intent().setClass(this, BasicEditActivity.class);
			spec = tabHost.newTabSpec("basic").setIndicator("Basic", res.getDrawable(android.R.drawable.ic_menu_edit));
			spec.setContent(intent);
			tabHost.addTab(spec);
			
			intent = new Intent().setClass(this, CharacterStatusActivity.class);
			spec = tabHost.newTabSpec("status").setIndicator("Status", res.getDrawable(android.R.drawable.ic_menu_view));
			spec.setContent(intent);
			tabHost.addTab(spec);
			
			tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
				public void onTabChanged(String tabId) {
					if (tabId.equals("equipment")) {
						CharacterEditActivity activity;

						activity = (CharacterEditActivity)getCurrentActivity();
						activity.notifyDatasetChanged();
					} else if (tabId.equals("basic")) {
						BasicEditActivity activity;

						activity = (BasicEditActivity)getCurrentActivity();
						activity.notifyDatasetChanged();
					} else if (tabId.equals("status")) {
						CharacterStatusActivity activity;

						activity = (CharacterStatusActivity)getCurrentActivity();
						activity.notifyDatasetChanged();
					}
				}
			});
		} else {
			/* disable tab */
			getTabWidget().setVisibility(View.GONE);
		}
		updateValues();
	}
	
    @Override
    public void onStart() {
    	super.onStart();

        {
        	Button btn;
        	
        	btn = (Button)findViewById(R.id.Save);
        	if (btn != null) {
        		btn.setOnClickListener(new View.OnClickListener() {
					public void onClick(View arg0) {
						showDialog(R.layout.querysavecharacter);
					}
        		});
        	}
        }
        
        {
        	CheckBox cb;
        	
        	cb = (CheckBox)findViewById(R.id.Compare);
        	if (cb != null) {
        		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
						if (arg1) {
							CharacterSelectorView cs = (CharacterSelectorView)findViewById(R.id.CharacterSelectorToCompare);
							long id;
							
							id = cs.getSelectedItemId();
			    			setCharacterIDToCompare(id);
			    			setFFXICharacterToCompare(getSettings().loadCharInfo(id));						
			    			updateSubViewValues();
						} else {
			    			setCharacterIDToCompare(-1);
			    			setFFXICharacterToCompare(null);
			    			updateSubViewValues();
						}
					}
        		});
        	}
        }
        
        {
        	CharacterSelectorView cs;
        	
        	cs = (CharacterSelectorView)findViewById(R.id.CharacterSelector);
        	if (cs != null) {
    	        AdapterView.OnItemSelectedListener listener;

    	    	listener = new AdapterView.OnItemSelectedListener() {
    				public void onItemSelected(AdapterView<?> arg0, View arg1,
    						int arg2, long arg3) {
    					if (arg3 != getCharacterID()) {
    		    			setCharacterID(arg3);
    		    			setFFXICharacter(getSettings().loadCharInfo(arg3));
    		    			updateSubViewValues();
    					}
    				}
    				public void onNothingSelected(AdapterView<?> arg0) {
    				}
    			};
   				cs.setOnItemSelectedListener(listener);
        	}

        	cs = (CharacterSelectorView)findViewById(R.id.CharacterSelectorToCompare);
        	if (cs != null) {
    	        AdapterView.OnItemSelectedListener listener;

    	    	listener = new AdapterView.OnItemSelectedListener() {
    				public void onItemSelected(AdapterView<?> arg0, View arg1,
    						int arg2, long arg3) {
    					if (arg3 != getCharacterIDToCompare()) {
    						CheckBox cb = (CheckBox)findViewById(R.id.Compare);
    						if (cb.isChecked()) {
    							setCharacterIDToCompare(arg3);
    							setFFXICharacterToCompare(getSettings().loadCharInfo(arg3));
    							updateSubViewValues();
    	        		   	}
    					}
    				}
    				public void onNothingSelected(AdapterView<?> arg0) {
						CheckBox cb;
		    			setCharacterIDToCompare(-1);
		    			setFFXICharacterToCompare(null);
		            	cb = (CheckBox)findViewById(R.id.Compare);
		            	cb.setChecked(false);
    				}
    			};
   				cs.setOnItemSelectedListener(listener);
        	}
        }
    }
    @Override
	public void onStop() {
        // reset listeners
        {
        	Button btn;
        	
        	btn = (Button)findViewById(R.id.Save);
        	if (btn != null) {
        		btn.setOnClickListener(null);
        	}
        }
        
        {
        	CheckBox cb;
        	
        	cb = (CheckBox)findViewById(R.id.Compare);
        	if (cb != null) {
        		cb.setOnCheckedChangeListener(null);
        	}
        }
        
        {
        	CharacterSelectorView cs;
        	
        	cs = (CharacterSelectorView)findViewById(R.id.CharacterSelector);
        	if (cs != null) {
   				cs.setOnItemSelectedListener(null);
        	}

        	cs = (CharacterSelectorView)findViewById(R.id.CharacterSelectorToCompare);
        	if (cs != null) {
   				cs.setOnItemSelectedListener(null);
        	}
        }
		super.onStop();
	}
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
	        updateValues();
		}
		super.onActivityResult(requestCode, resultCode, data);
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
		case R.id.EditMerits:
			MeritPointEditActivity.startActivity(this, 0);
			return true;
		case R.id.EditSkill:
			SkillEditActivity.startActivity(this, 0);
			return true;

		case R.id.Delete:
			showDialog(R.layout.querydeletecharacter);
			return true;
			
		case R.id.InstallDB:
			try {
				((FFXIDatabase)getDAO()).copyDatabaseFromAssets();
				showDialog(R.string.InstallDBSucceeded);
			} catch (IOException e) {
				showDialog(R.string.InstallDBFailed);
			}
			return true;

		case R.id.InstallDBFromSD:
			try {
				((FFXIDatabase)getDAO()).copyDatabaseFromSD();
				showDialog(R.string.InstallDBFromSDSucceeded);
			} catch (IOException e) {
				showDialog(R.string.InstallDBFromSDFailed);
			}
			return true;

		case R.id.ExportDBToSD:
			try {
				((FFXIDatabase)getDAO()).copyDatabaseToSD();
				showDialog(R.string.ExportDBSucceeded);
			} catch (IOException e) {
				showDialog(R.string.ExportDBFailed);
			}
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		AlertDialog.Builder builder;

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
		case R.string.InstallDBSucceeded:
			builder = new AlertDialog.Builder(this);
			builder.setCancelable(false);
	    	builder.setMessage(getString(R.string.InstallDBSucceeded));
	    	builder.setTitle(getString(R.string.InstallDB));
	    	builder.setPositiveButton(R.string.OK, null);
			dialog = builder.create();
			return dialog;
		case R.string.InstallDBFailed:
			builder = new AlertDialog.Builder(this);
			builder.setCancelable(false);
	    	builder.setMessage(getString(R.string.InstallDBSucceeded));
	    	builder.setTitle(getString(R.string.InstallDB));
	    	builder.setPositiveButton(R.string.OK, null);
			dialog = builder.create();
			return dialog;
		case R.string.InstallDBFromSDSucceeded:
			builder = new AlertDialog.Builder(this);
			builder.setCancelable(false);
	    	builder.setMessage(getString(R.string.InstallDBFromSDSucceeded));
	    	builder.setTitle(getString(R.string.InstallDB));
	    	builder.setPositiveButton(R.string.OK, null);
			dialog = builder.create();
			return dialog;
		case R.string.InstallDBFromSDFailed:
			builder = new AlertDialog.Builder(this);
			builder.setCancelable(false);
	    	builder.setMessage(getString(R.string.InstallDBFromSDSucceeded));
	    	builder.setTitle(getString(R.string.InstallDBFromSD));
	    	builder.setPositiveButton(R.string.OK, null);
			dialog = builder.create();
			return dialog;
		case R.string.ExportDBSucceeded:
			builder = new AlertDialog.Builder(this);
			builder.setCancelable(false);
	    	builder.setMessage(getString(R.string.ExportDBSucceeded));
	    	builder.setTitle(getString(R.string.ExportDBToSD));
	    	builder.setPositiveButton(R.string.OK, null);
			dialog = builder.create();
			return dialog;
		case R.string.ExportDBFailed:
			builder = new AlertDialog.Builder(this);
			builder.setCancelable(false);
	    	builder.setMessage(getString(R.string.ExportDBFailed));
	    	builder.setTitle(getString(R.string.ExportDBToSD));
	    	builder.setPositiveButton(R.string.OK, null);
			dialog = builder.create();
			return dialog;
		}
		return super.onCreateDialog(id);
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
    	TextView tv;
    	Button btn;

		switch (id) {
		case R.layout.querysavecharacter:
			tv = (TextView)dialog.findViewById(R.id.CharacterName);
			tv.setText(getName());
			CheckBox cb = (CheckBox)(dialog.findViewById(R.id.SaveNew));
			cb.setChecked(false);
			btn = (Button)dialog.findViewById(R.id.SaveOK);
			
			final long charid = FFXIEQActivity.this.getCharacterID();
			btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					TextView tv = (TextView)(v.getRootView().findViewById(R.id.CharacterName));
					String name = tv.getText().toString();
					CheckBox cb = (CheckBox)(v.getRootView().findViewById(R.id.SaveNew));
					long newid;
					String old_name;
					
					newid = charid;
					if (cb.isChecked())
						newid = -1;
					old_name = getName();
					newid = getSettings().saveCharInfo(newid, name, getFFXICharacter());
					dismissDialog(R.layout.querysavecharacter);
					if (newid != charid || !name.equals(old_name)) {
						loadFFXICharacter(newid);
					}
				}
			});
			return;
		case R.layout.querydeletecharacter:
			tv = (TextView)dialog.findViewById(R.id.CharacterName);
			tv.setText(getName());
			btn = (Button)dialog.findViewById(R.id.DeleteOK);
			btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					getSettings().deleteCharInfo(getCharacterID());
					dismissDialog(R.layout.querydeletecharacter);
					loadFFXICharacter(getSettings().getFirstCharacterId());
				}
			});
			btn = (Button)dialog.findViewById(R.id.Cancel);
			btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					dismissDialog(R.layout.querydeletecharacter);
				}
			});
			return;
		}
		super.onPrepareDialog(id, dialog);
	}
	
    protected void updateValues() {
    	CharacterSelectorView cs;
    	
    	cs = (CharacterSelectorView)findViewById(R.id.CharacterSelector);
    	if (cs != null) {
	        cs.setParam(getSettings(), getDAO(), getCharacterID());
    	}
    	cs = (CharacterSelectorView)findViewById(R.id.CharacterSelectorToCompare);
    	if (cs != null) {
	        cs.setParam(getSettings(), getDAO(), getCharacterIDToCompare());
    	}
    }

    protected void updateSubViewValues() {
        ((FFXIEQBaseActivity)getCurrentActivity()).notifyDatasetChanged();
    }

    public String getName() {
		return getSettings().getCharacterName(getCharacterID());
	}

    public void loadFFXICharacter(long id) {
		setCharacterID(id);
		setFFXICharacter(getSettings().loadCharInfo(id));
		
		updateValues();
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

	public long getCharacterIDToCompare() {
		return ((FFXIEQApplication)getApplication()).getCharacterIDToCompare();
	}

	public void setCharacterIDToCompare(long characterID) {
		((FFXIEQApplication)getApplication()).setCharacterIDToCompare(characterID);
	}
	public FFXICharacter getFFXICharacterToCompare() {
		return ((FFXIEQApplication)getApplication()).getFFXICharacterToCompare();
	}

	public void setFFXICharacterToCompare(FFXICharacter charInfo) {
		((FFXIEQApplication)getApplication()).setFFXICharacterToCompare(charInfo);
	}

	public ControlBindableValue[] getTemporaryValues() {
		return ((FFXIEQApplication)getApplication()).getTemporaryValues();
	}

	public void setTemporaryValues(ControlBindableValue []values) {
		((FFXIEQApplication)getApplication()).setTemporaryValues(values);
	}
}
