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
import com.github.kanata3249.ffxieq.Food;
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

public class FoodSelectorActivity extends FFXIEQBaseActivity {
	long mCurrent;
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
		
		mCurrent = param.getLong("Current");
		mFilterID = param.getLong("Filter");
		mFilterByType = param.getString("FilterByType");
		
		setContentView(R.layout.foodselector);
	}
	
	@Override
	protected void onStart() {
		super.onStart();

		FoodListView flv;
		
		flv = (FoodListView)findViewById(R.id.ListView);
		if (flv != null) {
			flv.setFilterByID(mFilterID);
			flv.setFilterByType(mFilterByType);
			flv.setParam(getDAO());
			
			flv.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Intent result = new Intent();
					
					result.putExtra("From", "FoodSelector");
					result.putExtra("Id", arg3);
					setResult(RESULT_OK, result);
					finish();
				}
				
			});
			
			flv.setOnItemLongClickListener(new OnItemLongClickListener() {
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					mLongClickingItemId = arg3;
					FoodSelectorActivity.this.openContextMenu(arg0);
					return true;
				}
			});
			
			registerForContextMenu(flv);
		}
		
		{
			Food cur = getDAO().instantiateFood(mCurrent);
			if (cur == null) {
				cur = new Food(-1, getString(R.string.FoodNotSelected), "");
			}
			if (cur != null) {
				TextView tv;
				View.OnLongClickListener listener = new View.OnLongClickListener() {
					public boolean onLongClick(View v) {
						mLongClickingItemId = mCurrent;
						FoodSelectorActivity.this.openContextMenu(v);
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
		FoodListView flv;
		
		flv = (FoodListView)findViewById(R.id.ListView);
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
		
		outState.putLong("Current", mCurrent);
		outState.putLong("Filter", mFilterID);
		outState.putString("FilterByType", mFilterByType);
	}

	static public boolean startActivity(Activity from, int request, FFXICharacter charInfo, long current) {
		Intent intent = new Intent(from, FoodSelectorActivity.class);
		
		intent.putExtra("Current", current);
		intent.putExtra("Filter", (long)-1);
		intent.putExtra("FilterByType", "");
		
		from.startActivityForResult(intent, request);
		return true;
	}

	static public boolean startActivity(Fragment from, int request, FFXICharacter charInfo, long current) {
		Intent intent = new Intent(from.getActivity(), FoodSelectorActivity.class);
		
		intent.putExtra("Current", current);
		intent.putExtra("Filter", (long)-1);
		intent.putExtra("FilterByType", "");
		
		from.startActivityForResult(intent, request);
		return true;
	}
	static public boolean isComeFrom(Intent data) {
		return data.getStringExtra("From").equals("FoodSelector");
	}
	
	static public long getFoodId(Intent data) {
		return data.getLongExtra("Id", -1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.foodselector, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem item;
		
		FoodListView flv;
		
		item = menu.findItem(R.id.FilterByType);
		SubMenu submenu = item.getSubMenu();
		submenu.removeGroup(R.id.FilterByType);

		flv = (FoodListView)findViewById(R.id.ListView);
		if (flv != null) {
			String types[] = flv.getAvailableFoodTypes();
			
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
		FoodListView flv = (FoodListView)findViewById(R.id.ListView);

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
			
			result.putExtra("From", "FoodSelector");
			result.putExtra("Id", -1);
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
	public boolean onContextItemSelected(MenuItem item) {
		Food food = getDAO().instantiateFood(mLongClickingItemId);
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
		inflater.inflate(R.menu.foodselector_context, menu);
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
					FoodListView flv = (FoodListView)findViewById(R.id.ListView);
					if (flv != null) {
						flv.setFilter(filter);
					}
				}
				
			}
			
		});
		return dialog;
	}
}
