package com.github.kanata3249.ffxieq.android;

import com.github.kanata3249.ffxieq.R;
import com.github.kanata3249.ffxieq.android.db.FFXIEQSettings;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class FilterSelectorDialog extends Dialog {
	private long mId;
	final FFXIEQSettings mSettings;
	boolean mApply;

	protected FilterSelectorDialog(Context context) {
		super(context, android.R.style.Theme_Dialog);
		
		mId = -1;
		mSettings = ((FFXIEQBaseActivity)context).getSettings(); 
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.filterselectordialog);
		setTitle(getContext().getString(R.string.EditFilterTitle));
		
		{
			Button btn = (Button)findViewById(R.id.ApplyFilter);
			if (btn != null) {
				btn.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						TextView tv;
						
						tv = (TextView)findViewById(R.id.Filter);
						if (tv != null) {
							String newFilter, oldFilter;
							
							newFilter = tv.getText().toString();
							if (mId >= 0) {
								oldFilter = mSettings.getFilter(mId);
							} else {
								oldFilter = "";
							}
							
							if (newFilter.length() > 0) {
								if (newFilter.equals(oldFilter)) {
									mSettings.useFilter(mId);
								} else {
									mId = mSettings.addFilter(newFilter);
								}
							}
						}
						mApply = true;
						dismiss();
					}
				});
			}
		}
	}

	@Override
	protected void onStart() {
		ListView lv;
		Cursor cursor;
		String[] columns = { FFXIEQSettings.C_Id, FFXIEQSettings.C_Filter };
		int[] views = { 0, R.id.Filter };
		FilterListAdapter adapter;
		

		lv = (ListView) findViewById(R.id.Filters);
		if (lv != null) {
			cursor = mSettings.getFilterCursor(columns);
			adapter = new FilterListAdapter(getContext(), R.layout.filterlistview, cursor, columns, views);
			lv.setAdapter(adapter);
			
			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					TextView tv = (TextView)findViewById(R.id.Filter);
					if (tv != null) {
						mId = arg3;
						tv.setText(mSettings.getFilter(arg3));
					}
				}
			});
		}
		
		super.onStart();
	}

	@Override
	public void onBackPressed() {
		mApply = false;
		super.onBackPressed();
	}

	private class FilterListAdapter extends SimpleCursorAdapter {

		public FilterListAdapter(Context context,
				int textViewResourceId, Cursor c, String[] from, int[] to) {
			super(context, textViewResourceId, c, from, to);
		}
	}
	
	public String getFilterString() {
		if (mApply)
			return mSettings.getFilter(mId);
		return "";
	}

	public long getFilterID() {
		return mId;
	}
}
