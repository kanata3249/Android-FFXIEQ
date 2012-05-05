/*
   Copyright 2012 kanata3249

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

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.github.kanata3249.ffxieq.FFXICharacter;
import com.github.kanata3249.ffxieq.R;

public class BlueMagicSetEditActivity extends FFXIEQBaseActivity {
	private boolean mUpdating;
	int mLongClickingItemPosition;
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bluemagicseteditactivity);
    }

    @Override
    public void onStart() {
    	super.onStart();

   		FFXICharacter charInfo = getFFXICharacter();

    	// setup controls
        {
        	BlueMagicSetView ms;
        	
        	ms = (BlueMagicSetView)findViewById(R.id.Magics);
        	if (ms != null) {
        		ms.bindFFXICharacter(charInfo);
            	ms.setOnItemClickListener(new OnItemClickListener() {
    				public void onItemClick(AdapterView<?> arg0, View arg1,
    						int arg2, long arg3) {
    				}
            	});
            	ms.setOnItemLongClickListener(new OnItemLongClickListener() {

					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						mLongClickingItemPosition = arg2;
						openContextMenu(arg0);
						return true;
					}
            		
            	});
    			registerForContextMenu(ms);
        	}
        }
    }

    static public boolean startActivity(FFXIEQActivity from, int request) {
		Intent intent = new Intent(from, BlueMagicSetEditActivity.class);
			
		from.startActivityForResult(intent, request);
		return true;
	}

	public void updateValues() {
        FFXICharacter charInfo = getFFXICharacter();

        if (mUpdating)
        	return;
        mUpdating = true;

    	BlueMagicSetView ms;
    	
    	ms = (BlueMagicSetView)findViewById(R.id.Magics);
    	if (ms != null) {
    		ms.bindFFXICharacter(charInfo);
    	}
    	
    	mUpdating = false;
    }

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		FFXICharacter charInfo = getFFXICharacter();
    	BlueMagicSetView ms;
    	
    	ms = (BlueMagicSetView)findViewById(R.id.Magics);
		switch (item.getItemId()) {
		case R.id.Set:
			charInfo.setBlueMagic(ms.getItemId(mLongClickingItemPosition), true);
			updateValues();
			return true;
		case R.id.Remove:
			charInfo.setBlueMagic(ms.getItemId(mLongClickingItemPosition), false);
			updateValues();
			return true;
		case R.id.RemoveAll:
			while (charInfo.getNumBlueMagic() > 0) {
				charInfo.setBlueMagic(charInfo.getBlueMagic(0).getId(), false);
			}
			updateValues();
			return true;
		}

		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.bluemagicset_context, menu);
    	BlueMagicSetView ms;
    	boolean set;
    	
    	ms = (BlueMagicSetView)findViewById(R.id.Magics);
		set = getFFXICharacter().isBlueMagicSet(ms.getItemId(mLongClickingItemPosition));
		MenuItem item;
		
		item = menu.findItem(R.id.Remove);
		if (item != null)
			item.setEnabled(set);
		item = menu.findItem(R.id.Set);
		if (item != null)
			item.setEnabled(!set);
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN){
			if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
				saveValues();
				
				Intent result = new Intent();
					
				result.putExtra("From", "BlueMagicSetEditActivity");
				setResult(RESULT_OK, result);
					
				finish();
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}
}
