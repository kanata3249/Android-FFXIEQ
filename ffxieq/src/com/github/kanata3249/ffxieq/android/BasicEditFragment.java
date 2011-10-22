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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView.OnEditorActionListener;

import com.github.kanata3249.ffxieq.Atma;
import com.github.kanata3249.ffxieq.AtmaSet;
import com.github.kanata3249.ffxieq.FFXICharacter;
import com.github.kanata3249.ffxieq.Food;
import com.github.kanata3249.ffxieq.R;

public class BasicEditFragment extends FFXIEQFragment {
	private View mView;
	private boolean mUpdating;
	int mLongClickingItemPosition;
	View mLongClickingView;
    
    @Override
    public void onStart() {
    	super.onStart();

   		FFXICharacter charInfo = getFFXICharacter();
   		View v = getView();

        // setup controls
        {
	        Spinner spin;
	        AdapterView.OnItemSelectedListener listener;
	
	    	listener = new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					switch (arg0.getId()) {
					case R.id.Race:
						if (getFFXICharacter().getRace() != arg2) {
							saveAndUpdateValues();
						}
						break;
					case R.id.Job:
						if (getFFXICharacter().getJob() != arg2) {
							saveAndUpdateValues();
						}
						break;
					case R.id.SubJob:
						if (getFFXICharacter().getSubJob() != arg2) {
							saveAndUpdateValues();
						}
						break;
					case R.id.AbyssiteOfFurtherance:
						if (getFFXICharacter().getAbyssiteOfFurtherance() != arg2) {
							saveAndUpdateValues();
						}
						break;
					case R.id.AbyssiteOfMerit:
						if (getFFXICharacter().getAbyssiteOfMerit() != arg2) {
							saveAndUpdateValues();
						}
						break;
					}
				}
				public void onNothingSelected(AdapterView<?> arg0) {
					saveAndUpdateValues();
				}
			};
	    	spin = (Spinner)v.findViewById(R.id.Race);
	    	if (spin != null) {
				spin.setOnItemSelectedListener(listener);
	    	}
	    	spin = (Spinner)v.findViewById(R.id.Job);
	    	if (spin != null) {
				spin.setOnItemSelectedListener(listener);
	    	}
	    	spin = (Spinner)v.findViewById(R.id.SubJob);
	    	if (spin != null) {
				spin.setOnItemSelectedListener(listener);
	    	}
	    	
	    	spin = (Spinner)v.findViewById(R.id.AbyssiteOfFurtherance);
	    	if (spin != null) {
				spin.setOnItemSelectedListener(listener);
	    	}
	    	spin = (Spinner)v.findViewById(R.id.AbyssiteOfMerit);
	    	if (spin != null) {
				spin.setOnItemSelectedListener(listener);
	    	}

	    	spin = (Spinner)v.findViewById(R.id.AbyssiteOfFurtherance);
	    	if (spin != null) {
				spin.setOnItemSelectedListener(listener);
	    	}
	    	spin = (Spinner)v.findViewById(R.id.AbyssiteOfMerit);
	    	if (spin != null) {
				spin.setOnItemSelectedListener(listener);
	    	}
	    }

        {
	        EditText et;
	        OnEditorActionListener editorActionListener;
	        OnFocusChangeListener focusChangeListener;

	        focusChangeListener = new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus == false) {
						if (v.getId() == R.id.JobLevel){
							FFXICharacter charInfo = getFFXICharacter();
							if (charInfo.getSubJobLevel() >= charInfo.getJobLevel() / 2) {
						    	EditText edit = (EditText)mView.findViewById(R.id.JobLevel);
						    	if (edit != null) {
							    	int value;
						    		String str = edit.getText().toString();
						    		try {
						    			value = Integer.valueOf(str);
						    		} catch (NumberFormatException e) {
						    			value = 0;
						    		}
						    		if (value > 1)
						    			value = value / 2;
						    		edit = (EditText)mView.findViewById(R.id.SubJobLevel);
						    		if (edit != null) {
						    			edit.setText(((Integer)value).toString());
						    		}
						    	}
							}
						}
						saveAndUpdateValues();						
					}
				}
	        };
	        editorActionListener = new OnEditorActionListener() {
				public boolean onEditorAction(TextView v, int actionId,
						KeyEvent event) {
					if (v.getId() == R.id.JobLevel){
						FFXICharacter charInfo = getFFXICharacter();
						if (charInfo.getSubJobLevel() >= charInfo.getJobLevel() / 2) {
					    	EditText edit = (EditText)mView.findViewById(R.id.JobLevel);
					    	if (edit != null) {
						    	int value;
					    		String str = edit.getText().toString();
					    		try {
					    			value = Integer.valueOf(str);
					    		} catch (NumberFormatException e) {
					    			value = 0;
					    		}
					    		if (value > 1)
					    			value = value / 2;
					    		edit = (EditText)mView.findViewById(R.id.SubJobLevel);
					    		if (edit != null) {
					    			edit.setText(((Integer)value).toString());
					    		}
					    	}
						}
					}
					saveAndUpdateValues();
					return false;
				}
	        };
	        et = (EditText)v.findViewById(R.id.JobLevel);
	        if (et != null) {
	        	et.setOnEditorActionListener(editorActionListener);
	        	et.setOnFocusChangeListener(focusChangeListener);
	        }
	        et = (EditText)v.findViewById(R.id.SubJobLevel);
	        if (et != null) {
	        	et.setOnEditorActionListener(editorActionListener);
	        	et.setOnFocusChangeListener(focusChangeListener);
	        }
        }
        {
        	AtmaSetView as;
        	
        	as = (AtmaSetView)v.findViewById(R.id.Atmas);
        	if (as != null) {
        		as.bindFFXICharacter(charInfo);
            	as.setOnItemClickListener(new OnItemClickListener() {
    				public void onItemClick(AdapterView<?> arg0, View arg1,
    						int arg2, long arg3) {
    					AtmaSelector.startActivity(BasicEditFragment.this, 0, getFFXICharacter(), arg2, ((AtmaSetView)arg0).getItemId(arg2));
    				}
            	});
            	as.setOnItemLongClickListener(new OnItemLongClickListener() {

					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						mLongClickingView = arg0;
						mLongClickingItemPosition = arg2;
						getActivity().openContextMenu(arg0);
						return true;
					}
            		
            	});
    			registerForContextMenu(as);
        	}
        }
        {
        	FoodSetView fs;
        	
        	fs = (FoodSetView)v.findViewById(R.id.Foods);
        	if (fs != null) {
        		fs.bindFFXICharacter(charInfo);
            	fs.setOnItemClickListener(new OnItemClickListener() {
    				public void onItemClick(AdapterView<?> arg0, View arg1,
    						int arg2, long arg3) {
    					FoodSelectorActivity.startActivity(BasicEditFragment.this, 0, getFFXICharacter(), ((FoodSetView)arg0).getItemId(arg2));
    				}
            	});
            	fs.setOnItemLongClickListener(new OnItemLongClickListener() {

					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						mLongClickingView = arg0;
						mLongClickingItemPosition = arg2;
						getActivity().openContextMenu(arg0);
						return true;
					}
            		
            	});
    			registerForContextMenu(fs);
        	}
        }
        
        {
        	CheckBox cb;
        	
        	cb = (CheckBox)v.findViewById(R.id.InAbyssea);
        	if (cb != null) {
        		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
        		   		getFFXICharacter().setInAbysea(arg1);
        		   		saveAndUpdateValues();
					}
        		});
        	}
        }
    }

    @Override
	public void onStop() {
		View v = getView();

        // reset listeners
        {
	        Spinner spin;
	
	    	spin = (Spinner)v.findViewById(R.id.Race);
	    	if (spin != null) {
				spin.setOnItemSelectedListener(null);
	    	}
	    	spin = (Spinner)v.findViewById(R.id.Job);
	    	if (spin != null) {
				spin.setOnItemSelectedListener(null);
	    	}
	    	spin = (Spinner)v.findViewById(R.id.SubJob);
	    	if (spin != null) {
				spin.setOnItemSelectedListener(null);
	    	}
	    	spin = (Spinner)v.findViewById(R.id.AbyssiteOfFurtherance);
	    	if (spin != null) {
				spin.setOnItemSelectedListener(null);
	    	}
	    	spin = (Spinner)v.findViewById(R.id.AbyssiteOfMerit);
	    	if (spin != null) {
				spin.setOnItemSelectedListener(null);
	    	}
	    }

        {
	        EditText et;
	        et = (EditText)v.findViewById(R.id.JobLevel);
	        if (et != null) {
	        	et.setOnEditorActionListener(null);
	        }
	        et = (EditText)v.findViewById(R.id.SubJobLevel);
	        if (et != null) {
	        	et.setOnEditorActionListener(null);
	        }
        }
        
        {
        	AtmaSetView as;
        	
        	as = (AtmaSetView)v.findViewById(R.id.Atmas);
        	if (as != null) {
            	as.setOnItemClickListener(null);
        	}
        }
        {
        	FoodSetView fs;
        	
        	fs = (FoodSetView)v.findViewById(R.id.Foods);
        	if (fs != null) {
            	fs.setOnItemClickListener(null);
        	}
        }
        
        {
        	CheckBox cb;
        	
        	cb = (CheckBox)v.findViewById(R.id.InAbyssea);
        	if (cb != null) {
        		cb.setOnCheckedChangeListener(null);
        	}
        }
        
        super.onStop();
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result;

        result = mView = inflater.inflate(R.layout.basiceditfragment, container, false);

        return result;
    }
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	FFXICharacter charInfo = getFFXICharacter();
		if (resultCode == Activity.RESULT_OK) {
			if (AtmaSelector.isComeFrom(data)) {
				int index = AtmaSelector.getIndex(data);
				long id = AtmaSelector.getAtmaId(data);
				
				if (index != -1) {
					charInfo.setAtma(index, id);
				}
		        saveAndUpdateValues();
			} else if (FoodSelectorActivity.isComeFrom(data)) {
				long id = FoodSelectorActivity.getFoodId(data);
				charInfo.setFood(0, getDAO().instantiateFood(id));
		        saveAndUpdateValues();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

    private void saveAndUpdateValues() {
        Spinner spin;
        EditText edit;
        int v;
        
        FFXICharacter charInfo;
        if (mUpdating)
        	return;
        if (getActivity() == null) {
        	return;
        }
        charInfo = getFFXICharacter();

    	spin = (Spinner)mView.findViewById(R.id.Race);
    	if (spin != null) {
    		v = spin.getSelectedItemPosition();
    		charInfo.setRace(v);
    	}
    	spin = (Spinner)mView.findViewById(R.id.Job);
    	if (spin != null) {
    		v = spin.getSelectedItemPosition();
    		charInfo.setJob(v);
    	}
    	spin = (Spinner)mView.findViewById(R.id.SubJob);
    	if (spin != null) {
    		v = spin.getSelectedItemPosition();
    		charInfo.setSubJob(v);
    	}

    	edit = (EditText)mView.findViewById(R.id.JobLevel);
    	if (edit != null) {
    		String str = edit.getText().toString();
    		try {
    			v = Integer.valueOf(str);
    		} catch (NumberFormatException e) {
    			v = 0;
    		}
    		charInfo.setJobLevel(v);
    	}
    	edit = (EditText)mView.findViewById(R.id.SubJobLevel);
    	if (edit != null) {
    		String str = edit.getText().toString();
    		try {
    			v = Integer.valueOf(str);
    		} catch (NumberFormatException e) {
    			v = 0;
    		}
    		v = Math.min(v, Math.max(1, charInfo.getJobLevel() / 2));
    		charInfo.setSubJobLevel(v);
    	}
    	spin = (Spinner)mView.findViewById(R.id.AbyssiteOfFurtherance);
    	if (spin != null) {
    		v = spin.getSelectedItemPosition();
    		charInfo.setAbyssiteOfFurtherance(v);
    	}
    	spin = (Spinner)mView.findViewById(R.id.AbyssiteOfMerit);
    	if (spin != null) {
    		v = spin.getSelectedItemPosition();
    		charInfo.setAbyssiteOfMerit(v);
    	}

    	updateValues();

    	if (mListener != null) {
    		mListener.notifyDatasetChanged();
    	}
    }

    public void updateValues() {
    	TextView tv;
    	Spinner spin;
        FFXICharacter charInfo = getFFXICharacter();

        if (mUpdating)
        	return;
        mUpdating = true;
    	spin = (Spinner)mView.findViewById(R.id.Race);
    	if (spin != null) {
    		spin.setSelection(charInfo.getRace());
    	}
    	spin = (Spinner)mView.findViewById(R.id.Job);
    	if (spin != null) {
    		spin.setSelection(charInfo.getJob());
    	}
    	spin = (Spinner)mView.findViewById(R.id.SubJob);
    	if (spin != null) {
    		spin.setSelection(charInfo.getSubJob());
    	}
    	tv = (TextView)mView.findViewById(R.id.JobLevel);
    	if (tv != null) {
    		tv.setText(((Integer)charInfo.getJobLevel()).toString());
    	}
    	tv = (TextView)mView.findViewById(R.id.SubJobLevel);
    	if (tv != null) {
    		tv.setText(((Integer)charInfo.getSubJobLevel()).toString());
    	}

    	CheckBox cb;
    	
    	cb = (CheckBox)mView.findViewById(R.id.InAbyssea);
    	if (cb != null) {
    		cb.setChecked(charInfo.isInAbbysea());
    	}
    		
    	spin = (Spinner)mView.findViewById(R.id.AbyssiteOfFurtherance);
    	if (spin != null) {
    		spin.setSelection(charInfo.getAbyssiteOfFurtherance());
    	}
    	spin = (Spinner)mView.findViewById(R.id.AbyssiteOfMerit);
    	if (spin != null) {
    		spin.setSelection(charInfo.getAbyssiteOfMerit());
    	}

    	AtmaSetView as;
    	
    	as = (AtmaSetView)mView.findViewById(R.id.Atmas);
    	if (as != null) {
    		as.bindFFXICharacter(charInfo);
    	}

    	FoodSetView fs;
    	
    	fs = (FoodSetView)mView.findViewById(R.id.Foods);
    	if (fs != null) {
    		fs.bindFFXICharacter(charInfo);
    	}

    	mUpdating = false;
    }

    @Override
	public boolean onContextItemSelected(MenuItem item) {
		if (mLongClickingView == getView().findViewById(R.id.Atmas)) {
			return onAtmaSetContextItemSelected(item);
		} else {
			return onFoodSetContextItemSelected(item);
		}
	}

	public boolean onFoodSetContextItemSelected(MenuItem item) {
		FFXICharacter charInfo = getFFXICharacter();

		switch (item.getItemId()) {
		case R.id.Remove:
			charInfo.setFood(-1, null);
			updateValues();
	        if (mListener != null) {
	    		mListener.notifyDatasetChanged();
	    	}
			return true;
		case R.id.List:
			{
	        	FoodSetView fs;
	        	
	        	fs = (FoodSetView)getView().findViewById(R.id.Foods);
				FoodSelectorActivity.startActivity(BasicEditFragment.this, 0, getFFXICharacter(), fs.getItemId(mLongClickingItemPosition));
				return true;
			}
		}

		// Web Search
		Food food = getFFXICharacter().getFood(0);
		Intent intent;
		if (food != null) {
			String[] urls = getResources().getStringArray(R.array.SearchURIs);
			String name = food.getName();
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
	
	public boolean onAtmaSetContextItemSelected(MenuItem item) {
		FFXICharacter charInfo = getFFXICharacter();

		switch (item.getItemId()) {
		case R.id.Remove:
			charInfo.setAtma(mLongClickingItemPosition, -1);
			updateValues();
	        if (mListener != null) {
	    		mListener.notifyDatasetChanged();
	    	}
			return true;
		case R.id.RemoveAll:
			for (int i = 0; i < AtmaSet.ATMA_MAX; i++) {
				charInfo.setAtma(i, -1);
			}
			updateValues();
	        if (mListener != null) {
	    		mListener.notifyDatasetChanged();
	    	}
			return true;
		case R.id.List:
			{
	        	AtmaSetView as;
	        	
	        	as = (AtmaSetView)getView().findViewById(R.id.Atmas);
				AtmaSelector.startActivity(BasicEditFragment.this, 0, getFFXICharacter(), mLongClickingItemPosition, as.getItemId(mLongClickingItemPosition));
				return true;
			}
		}

		// Web Search
		Atma atma = getFFXICharacter().getAtma(mLongClickingItemPosition);
		Intent intent;
		if (atma != null) {
			String[] urls = getResources().getStringArray(R.array.SearchURIs);
			String name = atma.getName();
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

		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.basicedit_context, menu);

		Object obj;
		MenuItem item;
		if (mLongClickingView == v.findViewById(R.id.Atmas)) {
			Atma atma = getFFXICharacter().getAtma(mLongClickingItemPosition);

			obj = atma;
			item = menu.findItem(R.id.Remove);
			if (item != null)
				item.setEnabled(atma != null);
		} else {
			Food food = getFFXICharacter().getFood(0);

			obj = food;
			item = menu.findItem(R.id.Remove);
			if (item != null)
				item.setEnabled(food != null);
			menu.removeItem(R.id.RemoveAll);
		}
		item = menu.findItem(R.id.WebSearch0);
		if (item != null)
			item.setEnabled(obj != null);
		item = menu.findItem(R.id.WebSearch1);
		if (item != null)
			item.setEnabled(obj != null);
		item = menu.findItem(R.id.WebSearch2);
		if (item != null)
			item.setEnabled(obj != null);
		item = menu.findItem(R.id.WebSearch3);
		if (item != null)
			item.setEnabled(obj != null);
		item = menu.findItem(R.id.WebSearch4);
		if (item != null)
			item.setEnabled(obj != null);
		item = menu.findItem(R.id.WebSearch5);
		if (item != null)
			item.setEnabled(obj != null);
		item = menu.findItem(R.id.WebSearch6);
		if (item != null)
			item.setEnabled(obj != null);
		item = menu.findItem(R.id.WebSearch7);
		if (item != null)
			item.setEnabled(obj != null);
	}
	
	public void saveValues() {
		if (getActivity().getCurrentFocus().getId() == R.id.JobLevel){
			FFXICharacter charInfo = getFFXICharacter();
			if (charInfo.getSubJobLevel() >= charInfo.getJobLevel() / 2) {
		    	EditText edit = (EditText)mView.findViewById(R.id.JobLevel);
		    	if (edit != null) {
			    	int value;
		    		String str = edit.getText().toString();
		    		try {
		    			value = Integer.valueOf(str);
		    		} catch (NumberFormatException e) {
		    			value = 0;
		    		}
		    		if (value > 1)
		    			value = value / 2;
		    		edit = (EditText)mView.findViewById(R.id.SubJobLevel);
		    		if (edit != null) {
		    			edit.setText(((Integer)value).toString());
		    		}
		    	}
			}
		}
		saveAndUpdateValues();						
	}
}
