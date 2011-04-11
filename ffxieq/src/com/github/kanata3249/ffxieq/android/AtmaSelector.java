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

import com.github.kanata3249.ffxieq.Atma;
import com.github.kanata3249.ffxieq.FFXICharacter;
import com.github.kanata3249.ffxieq.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class AtmaSelector extends FFXIEQBaseActivity {
	long mCurrent;
	int mIndex;
	long mFilterID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Bundle param;
		
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState != null) {
			param = savedInstanceState;
		} else {
			param = getIntent().getExtras();
		}
		
		mCurrent = param.getLong("Current");
		mIndex = param.getInt("Index");
		mFilterID = param.getLong("Filter");

		setContentView(R.layout.atmaselector);
		
		AtmaListView alv;
		
		alv = (AtmaListView)findViewById(R.id.ListView);
		if (alv != null) {
			alv.setFilterByID(mFilterID);
			alv.setParam(getDAO());
			
			alv.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Intent result = new Intent();
					
					result.putExtra("From", "AtmaSelector");
					result.putExtra("Id", arg3);
					result.putExtra("Index", mIndex);
					setResult(RESULT_OK, result);
					finish();
				}
				
			});
		}
		
		{
			Atma cur = getDAO().instanciateAtma(mCurrent);
			if (cur != null) {
				TextView tv;
				
				tv = (TextView)findViewById(R.id.Name);
				if (tv != null) {
					tv.setText(cur.getName());
				}
				tv = (TextView)findViewById(R.id.Description);
				if (tv != null) {
					tv.setText(cur.getDescription());
				}
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putLong("Current", mCurrent);
		outState.putInt("Index", mIndex);
		outState.putLong("Filter", mFilterID);
	}

	static public boolean startActivity(Activity from, int request, FFXICharacter charInfo, int index, long current) {
		Intent intent = new Intent(from, AtmaSelector.class);
		
		intent.putExtra("Current", current);
		intent.putExtra("Index", index);
		intent.putExtra("Filter", (long)-1);

		from.startActivityForResult(intent, request);
		return true;
	}

	static public boolean startActivity(Fragment from, int request, FFXICharacter charInfo, int index, long current) {
		Intent intent = new Intent(from.getActivity(), AtmaSelector.class);
		
		intent.putExtra("Current", current);
		intent.putExtra("Index", index);
		intent.putExtra("Filter", (long)-1);

		from.startActivityForResult(intent, request);
		return true;
	}
	
	static public boolean isComeFrom(Intent data) {
		return data.getStringExtra("From").equals("AtmaSelector");
	}
	static public int getIndex(Intent data) {
		return data.getIntExtra("Index", -1);
	}
	static public long getAtmaId(Intent data) {
		return data.getLongExtra("Id", -1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.atmaselector, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.Remove:
			Intent result = new Intent();
			
			result.putExtra("From", "AtmaSelector");
			result.putExtra("Index", mIndex);
			result.putExtra("Id", -1);
			setResult(RESULT_OK, result);
			finish();
			return true;
		case R.id.Filter:
			showDialog(0);
			return true;
		case R.id.ResetFilter:
			
			AtmaListView alv = (AtmaListView)findViewById(R.id.ListView);
			if (alv != null) {
				alv.setFilter("");
			}
			mFilterID = -1;
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		FilterSelectorDialog dialog = new FilterSelectorDialog(this);

		dialog.setOnDismissListener(new OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				FilterSelectorDialog fsd = (FilterSelectorDialog)dialog;
				String filter = fsd.getFilterString();
				mFilterID = fsd.getFilterID();

				if (filter.length() > 0) {
					AtmaListView alv = (AtmaListView)findViewById(R.id.ListView);
					if (alv != null) {
						alv.setFilter(filter);
					}
				}
				
			}
			
		});
		return dialog;
	}
}
