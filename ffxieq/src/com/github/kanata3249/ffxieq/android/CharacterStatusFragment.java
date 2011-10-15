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

import com.github.kanata3249.ffxieq.R;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;

public class CharacterStatusFragment extends FFXIEQFragment {
	View mView;
	int mDisplayParam;
	boolean mShowSkill;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDisplayParam = CharacterStatusView.GETSTATUS_STRING_SEPARATE;
        mShowSkill = false;
        setRetainInstance(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.characterstatusfragment, container, false);
        return mView;
    }

    public void updateValues() {
    	CharacterStatusView sv = (CharacterStatusView) mView.findViewById(R.id.StatusView);
    	if (sv != null) {
    		sv.setDisplayParam(mDisplayParam);
    		sv.bindFFXICharacter(getFFXICharacter(), getFFXICharacterToCompare());
    	}
    }
    
    public void setDisplayParam(int param) {
    	CharacterStatusView sv = (CharacterStatusView) mView.findViewById(R.id.StatusView);
    	
    	if (mDisplayParam == param)
    		return;
    	mDisplayParam = param;
    	if (sv != null) {
    		sv.setDisplayParam(mDisplayParam);
    	}
    }

    public void showSkillValue(boolean showSkill) {
    	CharacterStatusView sv = (CharacterStatusView) mView.findViewById(R.id.StatusView);
    	
    	if (mShowSkill == showSkill)
    		return;
    	mShowSkill = showSkill;
    	if (sv != null) {
    		sv.showSkillValue(mShowSkill);
    	}
    }

    @Override
    public void onStart() {
    	super.onStart();

   		// setup controls
        {
        	CharacterStatusView sv;
        	
        	sv = (CharacterStatusView) mView.findViewById(R.id.StatusView);
        	if (sv != null) {
        		sv.setLongClickable(true);
            	sv.setOnLongClickListener(new OnLongClickListener() {

					public boolean onLongClick(View v) {
						getActivity().openContextMenu(v);
						return false;
					}
            		
            	});
    			registerForContextMenu(sv);
    		}
        }
    }
    @Override
	public void onStop() {
		// reset listeners
        {
        	CharacterStatusView es;
        	
        	es = (CharacterStatusView) mView.findViewById(R.id.StatusView);
        	if (es != null) {
        		es.setOnLongClickListener(null);
        	}
        }
		super.onStop();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.statusview_context, menu);
		
		MenuItem item;
		
		item = menu.findItem(R.id.ToggleShowStatusSeparate);
		if (item != null) {
			if (mDisplayParam == CharacterStatusView.GETSTATUS_STRING_SEPARATE) {
				item.setTitle(getString(R.string.ShowStatusTotal));
			} else {
				item.setTitle(getString(R.string.ShowStatusSeparate));
			}
		}
		item = menu.findItem(R.id.ToggleShowSkill);
		if (item != null) {
			if (mShowSkill) {
				item.setTitle(getString(R.string.HideSkill));
			} else {
				item.setTitle(getString(R.string.ShowSkill));
			}
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.ToggleShowSkill:
			if (mShowSkill) {
				showSkillValue(false);
			} else {
				showSkillValue(true);
			}
			return true;
		case R.id.ToggleShowStatusSeparate:
			if (mDisplayParam == CharacterStatusView.GETSTATUS_STRING_SEPARATE) {
				setDisplayParam(CharacterStatusView.GETSTATUS_STRING_TOTAL);
			} else {
				setDisplayParam(CharacterStatusView.GETSTATUS_STRING_SEPARATE);
			}

			return true;
		}
		return super.onContextItemSelected(item);
	}
}