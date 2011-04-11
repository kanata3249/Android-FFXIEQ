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

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.TabHost;

public class FFXIEQActivity extends TabActivity {
	CharacterEditActivity mEditActivity;

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
		spec = tabHost.newTabSpec("edit").setIndicator("Edit", res.getDrawable(android.R.drawable.ic_menu_edit));
		spec.setContent(intent);
		tabHost.addTab(spec);
		mEditActivity = (CharacterEditActivity)getCurrentActivity();
		
		if (!CharacterEditActivity.hasStatusView(tabHost.getCurrentView())) {
			intent = new Intent().setClass(this, CharacterStatusActivity.class);
			spec = tabHost.newTabSpec("status").setIndicator("Status", res.getDrawable(android.R.drawable.ic_menu_view));
			spec.setContent(intent);
			tabHost.addTab(spec);
			
			tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
				public void onTabChanged(String tabId) {
					if (tabId.equals("edit")) {
						mEditActivity = (CharacterEditActivity)getCurrentActivity();
					} else if (tabId.equals("status")) {
						CharacterStatusActivity status;

						status = (CharacterStatusActivity)getCurrentActivity();
						status.setFFXICharacter(mEditActivity.getFFXICharacter());
					}
				}
			});
		} else {
			/* disable tab */
			getTabWidget().setVisibility(View.GONE);
		}
	}
	
}
