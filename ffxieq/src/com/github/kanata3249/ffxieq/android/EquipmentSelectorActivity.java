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

import com.github.kanata3249.ffxieq.Equipment;
import com.github.kanata3249.ffxieq.FFXICharacter;
import com.github.kanata3249.ffxieq.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;

public class EquipmentSelectorActivity extends FFXIEQBaseActivity {
	int mPart;
	int mJob;
	int mLevel;
	int mRace;
	long mCurrent;
	long mFilterID;
	long mLongClickingItemId;
	boolean mOrderByName;
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
		
		mPart = param.getInt("Part");
		mRace = param.getInt("Race");
		mLevel = param.getInt("Level");
		mJob = param.getInt("Job");
		mCurrent = param.getLong("Current");
		mFilterID = param.getLong("Filter");
		mOrderByName = param.getBoolean("OrderByName");
		mFilterByType = param.getString("FilterByType");
		
		setContentView(R.layout.equipmentselector);
	}
	
	@Override
	protected void onStart() {
		super.onStart();

		EquipmentListView elv;
		
		elv = (EquipmentListView)findViewById(R.id.ListView);
		if (elv != null) {
			elv.setFilterByID(mFilterID);
			elv.setOrderByName(mOrderByName);
			elv.setFilterByType(mFilterByType);
			elv.setParam(getDAO(), mPart, mRace, mJob, mLevel);
			
			elv.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Intent result = new Intent();
					
					result.putExtra("From", "EquipmentSelector");
					result.putExtra("Part", mPart);
					result.putExtra("Id", arg3);
					setResult(RESULT_OK, result);
					finish();
				}
				
			});
			
			elv.setOnItemLongClickListener(new OnItemLongClickListener() {
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					mLongClickingItemId = arg3;
					EquipmentSelectorActivity.this.openContextMenu(arg0);
					return true;
				}
			});
			
			registerForContextMenu(elv);
		}
		
		{
			Equipment cur = getDAO().instantiateEquipment(mCurrent);
			if (cur != null) {
				TextView tv;
				View.OnLongClickListener listener = new View.OnLongClickListener() {
					public boolean onLongClick(View v) {
						mLongClickingItemId = mCurrent;
						EquipmentSelectorActivity.this.openContextMenu(v);
						return true;
					}
				};
				
				tv = (TextView)findViewById(R.id.Name);
				if (tv != null) {
					tv.setText(cur.getName());
					tv.setOnLongClickListener(listener);
					registerForContextMenu(tv);
				}
				tv = (TextView)findViewById(R.id.Job);
				if (tv != null) {
					tv.setText(cur.getJob());
					tv.setOnLongClickListener(listener);
					registerForContextMenu(tv);
				}
				tv = (TextView)findViewById(R.id.Description);
				if (tv != null) {
					tv.setText(cur.getDescription());
					tv.setOnLongClickListener(listener);
					registerForContextMenu(tv);
				}
				tv = (TextView)findViewById(R.id.Level);
				if (tv != null) {
					tv.setText(((Integer)cur.getLevel()).toString());
					tv.setOnLongClickListener(listener);
					registerForContextMenu(tv);
				}
				tv = (TextView)findViewById(R.id.Race);
				if (tv != null) {
					tv.setText(cur.getRace());
					tv.setOnLongClickListener(listener);
					registerForContextMenu(tv);
				}
				tv = (TextView)findViewById(R.id.Ex);
				if (tv != null) {
					if (cur.isEx())
						tv.setVisibility(View.VISIBLE);
					else
						tv.setVisibility(View.INVISIBLE);
					tv.setOnLongClickListener(listener);
					registerForContextMenu(tv);
				}
				tv = (TextView)findViewById(R.id.Rare);
				if (tv != null) {
					if (cur.isRare())
						tv.setVisibility(View.VISIBLE);
					else
						tv.setVisibility(View.INVISIBLE);
					tv.setOnLongClickListener(listener);
					registerForContextMenu(tv);
				}
				
			}
		}
	}

	@Override
	protected void onStop() {
		EquipmentListView elv;
		
		elv = (EquipmentListView)findViewById(R.id.ListView);
		if (elv != null) {
			elv.setOnItemClickListener(null);
			elv.setOnItemLongClickListener(null);
		}

		TextView tv;
		
		tv = (TextView)findViewById(R.id.Name);
		if (tv != null) {
			tv.setOnLongClickListener(null);
		}
		tv = (TextView)findViewById(R.id.Job);
		if (tv != null) {
			tv.setOnLongClickListener(null);
		}
		tv = (TextView)findViewById(R.id.Description);
		if (tv != null) {
			tv.setOnLongClickListener(null);
		}
		tv = (TextView)findViewById(R.id.Level);
		if (tv != null) {
			tv.setOnLongClickListener(null);
		}
		tv = (TextView)findViewById(R.id.Race);
		if (tv != null) {
			tv.setOnLongClickListener(null);
		}
		super.onStop();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putInt("Part", mPart);
		outState.putInt("Race", mRace);
		outState.putInt("Job", mJob);
		outState.putInt("Level", mLevel);
		outState.putLong("Current", mCurrent);
		outState.putLong("Filter", mFilterID);
		outState.putBoolean("OrderByName", mOrderByName);
		outState.putString("FilterByType", mFilterByType);
	}

	static public boolean startActivity(Activity from, int request, FFXICharacter charInfo, int part, long current) {
		Intent intent = new Intent(from, EquipmentSelectorActivity.class);
		
		intent.putExtra("Part", part);
		intent.putExtra("Race", charInfo.getRace());
		intent.putExtra("Job", charInfo.getJob());
		intent.putExtra("Level", charInfo.getJobLevel());
		intent.putExtra("Current", current);
		intent.putExtra("Filter", (long)-1);
		intent.putExtra("OrderByName", false);
		intent.putExtra("FilterByType", "");
		
		from.startActivityForResult(intent, request);
		return true;
	}

	static public boolean startActivity(Fragment from, int request, FFXICharacter charInfo, int part, long current) {
		Intent intent = new Intent(from.getActivity(), EquipmentSelectorActivity.class);
		
		intent.putExtra("Part", part);
		intent.putExtra("Race", charInfo.getRace());
		intent.putExtra("Job", charInfo.getJob());
		intent.putExtra("Level", charInfo.getJobLevel());
		intent.putExtra("Current", current);
		intent.putExtra("Filter", (long)-1);
		intent.putExtra("OrderByName", false);
		intent.putExtra("FilterByType", "");
		
		from.startActivityForResult(intent, request);
		return true;
	}
	static public boolean isComeFrom(Intent data) {
		return data.getStringExtra("From").equals("EquipmentSelector");
	}
	static public int getPart(Intent data) {
		return data.getIntExtra("Part", -1);
	}
	
	static public long getEquipmentId(Intent data) {
		return data.getLongExtra("Id", -1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.equipmentselector, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem item;
		
		item = menu.findItem(R.id.OrderByName);
		if (item != null) {
			if (mOrderByName)
				item.setTitle(getString(R.string.OrderByLevel));
			else
				item.setTitle(getString(R.string.OrderByName));
		}
		
		EquipmentListView elv;
		
		item = menu.findItem(R.id.FilterByType);
		SubMenu submenu = item.getSubMenu();
		submenu.removeGroup(R.id.FilterByType);

		elv = (EquipmentListView)findViewById(R.id.ListView);
		if (elv != null) {
			String types[] = elv.getAvailableWeaponTypes();
			
			if (types == null || types.length == 1) {
				item.setEnabled(false);
			} else {
				item.setEnabled(true);
				submenu.add(R.id.FilterByType, -1, Menu.NONE, getString(R.string.ResetFilterByType));
				for (int i = 0; i < types.length; i++) {
					submenu.add(R.id.FilterByType, i, Menu.NONE, types[i]);
				}
			}
			
		}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		EquipmentListView elv = (EquipmentListView)findViewById(R.id.ListView);

		if (item.getGroupId() == R.id.FilterByType) {
			if (item.getItemId() < 0) {
				mFilterByType = "";
			} else {
				mFilterByType = (String)item.getTitle();
			}
			elv.setFilterByType(mFilterByType);
			return true;
		}

		switch (item.getItemId()) {
		case R.id.OrderByName:
			mOrderByName = !mOrderByName;
			if (elv != null) {
				elv.setOrderByName(mOrderByName);
			}
			return true;
		case R.id.Remove:
			Intent result = new Intent();
			
			result.putExtra("From", "EquipmentSelector");
			result.putExtra("Part", mPart);
			result.putExtra("Id", -1);
			setResult(RESULT_OK, result);
			finish();
			return true;
		case R.id.Filter:
			showDialog(0);
			return true;
		case R.id.ResetFilter:
			if (elv != null) {
				elv.setFilter("");
			}
			mFilterID = -1;
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Equipment eq = getDAO().instantiateEquipment(mLongClickingItemId);
		String name = eq.getName();
		Intent intent;
		if (eq != null) {
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
		inflater.inflate(R.menu.equipmentselector_context, menu);
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
					EquipmentListView elv = (EquipmentListView)findViewById(R.id.ListView);
					if (elv != null) {
						elv.setFilter(filter);
					}
				}
				
			}
			
		});
		return dialog;
	}
}
