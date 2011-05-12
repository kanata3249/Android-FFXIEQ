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

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import com.github.kanata3249.ffxieq.R;

public class CharacterStatusActivity extends FFXIEQBaseActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.characterstatusactivity);
    }

	public void notifyDatasetChanged() {
		FragmentManager fm = getSupportFragmentManager();
		CharacterStatusFragment fragment = (CharacterStatusFragment)fm.findFragmentById(R.id.CharacterStatus);
        
        fragment.updateValues();
	}
	
	public void setDisplayParam(int param) {
		FragmentManager fm = getSupportFragmentManager();
		CharacterStatusFragment fragment = (CharacterStatusFragment)fm.findFragmentById(R.id.CharacterStatus);
        
        fragment.setDisplayParam(param);
	}
}
