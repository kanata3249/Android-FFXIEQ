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

import com.github.kanata3249.ffxieq.FFXICharacter;
import com.github.kanata3249.ffxieq.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.app.Activity;

public class BlueMagicSelectionStatusView extends ScrollView {
	FFXICharacter mCharInfo;

	public BlueMagicSelectionStatusView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mCharInfo = null;
		View children = ((Activity)context).getLayoutInflater().inflate(R.layout.bluemagicselectionstatusview, null);
		this.addView(children);
	}

	public boolean bindFFXICharacter(FFXICharacter charinfo) {
		mCharInfo = charinfo;
		notifyDatasetChanged();

		return true;
	}
	
	public void notifyDatasetChanged() {
		String text;
		
		text = String.format(" BP %d/%d\n", mCharInfo.getCurrentBP(), mCharInfo.getBP());
		text += String.format(" BSP %d/%d\n", mCharInfo.getCurrentBSP(), mCharInfo.getBSP());
		((TextView)findViewById(R.id.textView)).setText(text);
	}
}
