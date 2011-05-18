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

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class WebViewDialog extends Dialog {
	String mURL;

	protected WebViewDialog(Context context) {
		super(context, android.R.style.Theme_Dialog);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.webviewdialog);
		
		Button btn = (Button)findViewById(R.id.OK);
		if (btn != null) {
			btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});
		}
		
		if (mURL != null) {
			loadURL(mURL);
		}
	}
	
	public void loadURL(String url) {
		WebView wv = (WebView)findViewById(R.id.WebView);
		if (wv != null) {
			wv.loadUrl(url);
			setTitle(wv.getTitle());
		} else {
			mURL = url;
		}
	}
}
