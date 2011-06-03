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
import com.github.kanata3249.ffxieq.android.db.FFXIDatabase;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AugmentEditActivity extends FFXIEQBaseActivity {
	int mPart;
	long mBaseID;
	long mAugID;

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
		mBaseID = param.getLong("BaseId");
		mAugID = param.getLong("AugId");

		setContentView(R.layout.augmentedit);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		{
			Equipment cur = getDAO().instantiateEquipment(mBaseID, mAugID);
			if (cur != null) {
				TextView tv;
				
				mBaseID = cur.getId();
				cur.parseDescription();
				cur.removeAugmentCommentFromUnknownToken();
				tv = (TextView)findViewById(R.id.Name);
				if (tv != null) {
					tv.setText(cur.getName());
				}
				tv = (TextView)findViewById(R.id.Job);
				if (tv != null) {
					tv.setText(cur.getJob());
				}
				tv = (TextView)findViewById(R.id.Description);
				if (tv != null) {
					tv.setText(cur.getDescription());
				}
				tv = (TextView)findViewById(R.id.Level);
				if (tv != null) {
					tv.setText(((Integer)cur.getLevel()).toString());
				}
				tv = (TextView)findViewById(R.id.Race);
				if (tv != null) {
					tv.setText(cur.getRace());
				}
				tv = (TextView)findViewById(R.id.Ex);
				if (tv != null) {
					if (cur.isEx())
						tv.setVisibility(View.VISIBLE);
					else
						tv.setVisibility(View.INVISIBLE);
				}
				tv = (TextView)findViewById(R.id.Rare);
				if (tv != null) {
					if (cur.isRare())
						tv.setVisibility(View.VISIBLE);
					else
						tv.setVisibility(View.INVISIBLE);
				}
				
				EditText et = (EditText)findViewById(R.id.AugmentDescription);
				if (et != null) {
					if (mAugID >= 0)
						et.setText(cur.getAugment());
					else {
						String tmpl = cur.getAugmentComment();
						if (tmpl != null) {
						} else {
							tmpl = "";
						}
						et.setText(tmpl);
					}
				}
			}
		}

		{
			Button btn;
			
			btn = (Button)findViewById(R.id.Save);
			btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					long newId = saveAugment();
					
					Intent result = new Intent();
						
					result.putExtra("From", "AugmentEditActivity");
					result.putExtra("Part", mPart);
					result.putExtra("BaseId", mBaseID);
					result.putExtra("AugId", newId);
					setResult(RESULT_OK, result);
					finish();
				}
			});
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putInt("Part", mPart);
		outState.putLong("BaseId", mBaseID);
		outState.putLong("AugId", mAugID);
	}

	static public boolean startActivity(Activity from, int request, FFXICharacter charInfo, int part, long baseID, long augID) {
		Intent intent = new Intent(from, AugmentEditActivity.class);
	
		intent.putExtra("Part", part);
		intent.putExtra("BaseId", baseID);
		intent.putExtra("AugId", augID);

		from.startActivityForResult(intent, request);
		return true;
	}

	static public boolean startActivity(Fragment from, int request, FFXICharacter charInfo, int part, long baseID, long augID) {
		Intent intent = new Intent(from.getActivity(), AugmentEditActivity.class);
		
		intent.putExtra("Part", part);
		intent.putExtra("BaseId", baseID);
		intent.putExtra("AugId", augID);

		from.startActivityForResult(intent, request);
		return true;
	}
	
	static public boolean isComeFrom(Intent data) {
		return data.getStringExtra("From").equals("AugmentEditActivity");
	}

	static public int getPart(Intent data) {
		return data.getIntExtra("Part", 0);
	}
	static public long getEquipmentId(Intent data) {
		return data.getLongExtra("BaseId", -1);
	}

	static public long getAugmentId(Intent data) {
		return data.getLongExtra("AugId", -1);
	}
	
	long saveAugment() {
		EditText et;
		long result;
		
		result = -1;
		et = (EditText)findViewById(R.id.AugmentDescription);
		if (et != null) {
			String augment;
			
			augment = et.getText().toString();
			if (augment.length() == 0) {
				return mAugID;
			}
			result = ((FFXIDatabase)getDAO()).saveAugment(mAugID, augment, mBaseID);
			if (result >= 0)
				mAugID = result;
		}
		return result;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN){
			if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
				EditText et;
				Equipment eq;
				
				eq = getDAO().instantiateEquipment(mBaseID, mAugID);
				et = (EditText)findViewById(R.id.AugmentDescription);
				if (eq != null && et != null) {
					String augment;
					
					augment = et.getText().toString();
					if (!eq.getAugment().equals(augment)) {
						showDialog(R.string.QueryDiscardChanges);
						return true;
					}
				}
			}
		}
		
		return super.dispatchKeyEvent(event);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		AlertDialog.Builder builder;

		switch (id) {
		case R.string.QueryDiscardChanges:
			builder = new AlertDialog.Builder(this);
			builder.setCancelable(true);
	    	builder.setMessage(getString(R.string.QueryDiscardChanges));
	    	builder.setTitle(getString(R.string.QueryDiscardChangesTitle));
	    	builder.setPositiveButton(R.string.DiscardOK, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(R.string.QueryDiscardChanges);

					Intent result = new Intent();
					
					result.putExtra("From", "AugmentEditActivity");
					setResult(RESULT_CANCELED, result);
					finish();
				}
			});
	    	builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(R.string.QueryDiscardChanges);
				}
			});
			dialog = builder.create();
			return dialog;
		}
		return super.onCreateDialog(id);
	}
}
