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

import com.github.kanata3249.ffxieq.FFXICharacter;
import com.github.kanata3249.ffxieq.Magic;
import com.github.kanata3249.ffxieq.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;

public class MagicSelectorActivity extends FFXIEQBaseActivity {
	int mIndex;
	long mCurrent;
	long mSubId;
	long mFilterID;
	long mLongClickingItemId;
	String mFilterByType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Bundle param;
		
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState != null) {
			param = savedInstanceState;
		} else {
			param = getIntent().getExtras();
		}

		mIndex = param.getInt("Index");
		mCurrent = param.getLong("Current");
		mFilterID = param.getLong("Filter");
		mFilterByType = param.getString("FilterByType");
		mSubId = param.getLong("SubId");
		
		setContentView(R.layout.magicselector);
	}
	
	@Override
	protected void onStart() {
		super.onStart();

		MagicListView flv;
		
		flv = (MagicListView)findViewById(R.id.ListView);
		if (flv != null) {
			flv.setFilterByID(mFilterID);
			flv.setFilterByType(mFilterByType);
			flv.setParam(getDAO(), mSubId);
			
			flv.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Intent result = new Intent();
					
					result.putExtra("From", "MagicSelector");
					result.putExtra("Index", mIndex);
					result.putExtra("Id", arg3);
					result.putExtra("SubId", mSubId);
					setResult(RESULT_OK, result);
					finish();
				}
				
			});
			
			flv.setOnItemLongClickListener(new OnItemLongClickListener() {
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					mLongClickingItemId = arg3;
					MagicSelectorActivity.this.openContextMenu(arg0);
					return true;
				}
			});
			
			registerForContextMenu(flv);
		}
		
		{
			Magic cur = getDAO().instantiateMagic(mCurrent);
			if (cur == null) {
				cur = new Magic(-1, -1, getString(R.string.MagicNotSelected), "", "");
			}
			if (cur != null) {
				TextView tv;
				View.OnLongClickListener listener = new View.OnLongClickListener() {
					public boolean onLongClick(View v) {
						mLongClickingItemId = mCurrent;
						MagicSelectorActivity.this.openContextMenu(v);
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
				tv = (TextView)findViewById(R.id.Memo);
				if (tv != null) {
					tv.setText(cur.getMemo());
					tv.setOnLongClickListener(listener);
					registerForContextMenu(tv);
				}
			}
		}
	}

	@Override
	protected void onStop() {
		MagicListView flv;
		
		flv = (MagicListView)findViewById(R.id.ListView);
		if (flv != null) {
			flv.setOnItemClickListener(null);
			flv.setOnItemLongClickListener(null);
		}

		TextView tv;
		
		tv = (TextView)findViewById(R.id.Name);
		if (tv != null) {
			tv.setOnLongClickListener(null);
		}
		tv = (TextView)findViewById(R.id.Description);
		if (tv != null) {
			tv.setOnLongClickListener(null);
		}

		super.onStop();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putInt("Index", mIndex);
		outState.putLong("Current", mCurrent);
		outState.putLong("SubId", mSubId);
		outState.putLong("Filter", mFilterID);
		outState.putString("FilterByType", mFilterByType);
	}

	static public boolean startActivity(Activity from, int request, FFXICharacter charInfo, int index, long current, long subId) {
		Intent intent = new Intent(from, MagicSelectorActivity.class);
		
		intent.putExtra("Index", index);
		intent.putExtra("Current", current);
		intent.putExtra("SubId", subId);
		intent.putExtra("Filter", (long)-1);
		intent.putExtra("FilterByType", "");
		
		from.startActivityForResult(intent, request);
		return true;
	}

	static public boolean startActivity(Fragment from, int request, FFXICharacter charInfo, int index, long current, long subId) {
		Intent intent = new Intent(from.getActivity(), MagicSelectorActivity.class);
		
		intent.putExtra("Index", index);
		intent.putExtra("Current", current);
		intent.putExtra("SubId", subId);
		intent.putExtra("Filter", (long)-1);
		intent.putExtra("FilterByType", "");
		
		from.startActivityForResult(intent, request);
		return true;
	}
	static public boolean isComeFrom(Intent data) {
		return data.getStringExtra("From").equals("MagicSelector");
	}
	
	static public int getIndex(Intent data) {
		return data.getIntExtra("Index", -1);
	}
	static public long getMagicId(Intent data) {
		return data.getLongExtra("Id", -1);
	}
	static public long getMagicSubId(Intent data) {
		return data.getLongExtra("SubId", -1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.magicselector, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		MagicListView flv = (MagicListView)findViewById(R.id.ListView);

		if (item.getGroupId() == R.id.FilterByType) {
			if (item.getItemId() < 0) {
				mFilterByType = "";
			} else {
				mFilterByType = (String)item.getTitle();
			}
			flv.setFilterByType(mFilterByType);
			return true;
		}

		switch (item.getItemId()) {
		case R.id.Remove:
			Intent result = new Intent();
			
			result.putExtra("From", "MagicSelector");
			result.putExtra("Index", mIndex);
			result.putExtra("Id", -1);
			result.putExtra("SubId", mSubId);
			setResult(RESULT_OK, result);
			finish();
			return true;
		case R.id.Filter:
			showDialog(0);
			return true;
		case R.id.ResetFilter:
			if (flv != null) {
				flv.setFilter("");
			}
			mFilterID = -1;
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
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
					MagicListView flv = (MagicListView)findViewById(R.id.ListView);
					if (flv != null) {
						flv.setFilter(filter);
					}
				}
				
			}
			
		});
		return dialog;
	}
}
