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
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;

public class AtmaSelector extends FFXIEQBaseActivity {
	long mCurrent;
	int mIndex;
	long mFilterID;
	long mLongClickingItemId;

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
	}
	
	@Override
	protected void onStart() {
		super.onStart();

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

			alv.setOnItemLongClickListener(new OnItemLongClickListener() {
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					mLongClickingItemId = arg3;
					AtmaSelector.this.openContextMenu(arg0);
					return true;
				}
			});
			
			registerForContextMenu(alv);
		}
		
		{
			Atma cur = getDAO().instantiateAtma(mCurrent);
			if (cur != null) {
				TextView tv;
				View.OnLongClickListener listener = new View.OnLongClickListener() {
					public boolean onLongClick(View v) {
						mLongClickingItemId = mCurrent;
						AtmaSelector.this.openContextMenu(v);
						return true;
					}
				};

				tv = (TextView)findViewById(R.id.Name);
				if (tv != null) {
					tv.setText(cur.getName());
					tv.setOnLongClickListener(listener);
					registerForContextMenu(tv);
				}
				tv = (TextView)findViewById(R.id.Description);
				if (tv != null) {
					tv.setText(cur.getDescription());
					tv.setOnLongClickListener(listener);
					registerForContextMenu(tv);
				}
			}
		}
	}

	@Override
	protected void onStop() {
		AtmaListView alv;
		
		alv = (AtmaListView)findViewById(R.id.ListView);
		if (alv != null) {
			alv.setOnItemClickListener(null);
		}
		super.onStop();
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
	public boolean onContextItemSelected(MenuItem item) {
		Atma food = getDAO().instantiateAtma(mLongClickingItemId);
		String name = food.getName();
		Intent intent;
		if (food != null) {
			String[] urls = getResources().getStringArray(R.array.SearchURIs);
			String url;

			url = null;
			switch (item.getItemId()) {
			case R.id.WebSearch0:
				url = urls[0];
				break;
			case R.id.WebSearch1:
				url = urls[1];
				break;
			case R.id.WebSearch2:
				url = urls[2];
				break;
			case R.id.WebSearch3:
				url = urls[3];
				break;
			case R.id.WebSearch4:
				url = urls[4];
				break;
			case R.id.WebSearch5:
				url = urls[5];
				break;
			case R.id.WebSearch6:
				url = urls[6];
				break;
			case R.id.WebSearch7:
				url = urls[7];
				break;
			default:
				url = null;
				break;
			}
			if (url != null) {
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url + Uri.encode(name.split("\\+")[0])));
				startActivity(intent);
				return true;
			}
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.atmaselector_context, menu);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
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
