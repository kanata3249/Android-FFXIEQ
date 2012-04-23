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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.github.kanata3249.ffxieq.FFXICharacter;
import com.github.kanata3249.ffxieq.Magic;
import com.github.kanata3249.ffxieq.R;

public class MagicSetEditFragment extends FFXIEQFragment {
	private View mView;
	private boolean mUpdating;
	int mLongClickingItemPosition;
    
    @Override
    public void onStart() {
    	super.onStart();

   		FFXICharacter charInfo = getFFXICharacter();
   		View v = getView();

    	charInfo.reloadMagicsForUpdatingDatabase();

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
            	ms.setOnItemLongClickListener(new OnItemLongClickListener() {

					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						mLongClickingItemPosition = arg2;
						getActivity().openContextMenu(arg0);
						return true;
					}
            		
            	});
    			registerForContextMenu(ms);
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

    	charInfo.reloadMagicsForUpdatingDatabase();

    	MagicSetView ms;
    	
    	ms = (MagicSetView)mView.findViewById(R.id.Magics);
    	if (ms != null) {
    		ms.bindFFXICharacter(charInfo);
    	}
    	
    	mUpdating = false;
    }

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		FFXICharacter charInfo = getFFXICharacter();

		switch (item.getItemId()) {
		case R.id.Remove:
			charInfo.setMagic(-1, charInfo.getMagic(mLongClickingItemPosition).getSubId());
			updateValues();
	        if (mListener != null) {
	    		mListener.notifyDatasetChanged();
	    	}
			return true;
		case R.id.RemoveAll:
			while (charInfo.getNumMagic() > 0) {
				charInfo.setMagic(-1, charInfo.getMagic(0).getSubId());
			}
			updateValues();
	        if (mListener != null) {
	    		mListener.notifyDatasetChanged();
	    	}
			return true;
		case R.id.List:
			{
				long cur, subid;
	        	MagicSetView ms;
	        	
	        	ms = (MagicSetView)getView().findViewById(R.id.Magics);
				cur = ms.getItemId(mLongClickingItemPosition);
				subid = ms.getItemSubId(mLongClickingItemPosition);
				MagicSelectorActivity.startActivity(MagicSetEditFragment.this, 0, getFFXICharacter(), mLongClickingItemPosition, cur, subid);
				return true;
			}
		}

		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.magicset_context, menu);
		Magic mg;
		
		if (getFFXICharacter().getNumMagic() > mLongClickingItemPosition)
			mg = getFFXICharacter().getMagic(mLongClickingItemPosition);
		else
			mg = null;
		MenuItem item;
		
		item = menu.findItem(R.id.Remove);
		if (item != null)
			item.setEnabled(mg != null);
	}

}
