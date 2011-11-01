package com.github.kanata3249.ffxieq.android;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TabHost;

public class FFXIEQTabHost extends TabHost {

	public FFXIEQTabHost(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setCurrentTab(int index) {
		FFXIEQBaseActivity cur = (FFXIEQBaseActivity)((FFXIEQActivity)getContext()).getCurrentActivity();
		
		if (cur != null)
			cur.saveValues();
		super.setCurrentTab(index);
	}
}
