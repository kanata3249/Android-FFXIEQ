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

import com.github.kanata3249.ffxi.status.StatusType;
import com.github.kanata3249.ffxieq.MeritPoint;
import com.github.kanata3249.ffxieq.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

public class JobSpecificMeritPointEditActivity extends FFXIEQBaseActivity {
	private int mJob;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			mJob = savedInstanceState.getInt("Job");
		} else {
			mJob = getIntent().getExtras().getInt("Job");
		}
		
		setContentView(R.layout.jobspecificmeritpointedit);

		ControlBindableInteger values[] = (ControlBindableInteger[])getTemporaryValues();

		{ // category 1
			String titles[] = getDAO().getJobSpecificMeritPointItems(mJob, 1);
			int titleids[] = { R.id.Category1Title1, R.id.Category1Title2, R.id.Category1Title3, R.id.Category1Title4, R.id.Category1Title5,
							   R.id.Category1Title6, R.id.Category1Title7, R.id.Category1Title8, R.id.Category1Title9, R.id.Category1Title10 };
			int valueids[] = { R.id.Category1Value1, R.id.Category1Value2, R.id.Category1Value3, R.id.Category1Value4, R.id.Category1Value5,
							   R.id.Category1Value6, R.id.Category1Value7, R.id.Category1Value8, R.id.Category1Value9, R.id.Category1Value10 };
			int valueindex, i;
			
			valueindex = StatusType.MODIFIER_NUM.ordinal() + mJob * MeritPoint.MAX_JOB_SPECIFIC_MERIT_POINT * MeritPoint.MAX_JOB_SPECIFIC_MERIT_POINT_CATEGORY;
			for (i = 0; i < titles.length; i++) {
				TextView tv = (TextView)findViewById(titleids[i]);
				
				if (tv != null) {
					tv.setText(titles[i]);
					tv.setVisibility(View.VISIBLE);
				}

				Spinner spin = (Spinner)findViewById(valueids[i]);
				if (spin != null) {
					bindControlAndValue(valueids[i], values[valueindex + i]);
					spin.setVisibility(View.VISIBLE);
				}
			}
			for (; i < MeritPoint.MAX_JOB_SPECIFIC_MERIT_POINT; i++) {
				TextView tv = (TextView)findViewById(titleids[i]);
				if (tv != null) {
					tv.setVisibility(View.GONE);
				}

				Spinner spin = (Spinner)findViewById(valueids[i]);
				if (spin != null) {
					spin.setVisibility(View.GONE);
				}
			}
		}

		{ // category 2
			String titles[] = getDAO().getJobSpecificMeritPointItems(mJob, 2);
			int titleids[] = { R.id.Category2Title1, R.id.Category2Title2, R.id.Category2Title3, R.id.Category2Title4, R.id.Category2Title5,
							   R.id.Category2Title6, R.id.Category2Title7, R.id.Category2Title8, R.id.Category2Title9, R.id.Category2Title10 };
			int valueids[] = { R.id.Category2Value1, R.id.Category2Value2, R.id.Category2Value3, R.id.Category2Value4, R.id.Category2Value5,
							   R.id.Category2Value6, R.id.Category2Value7, R.id.Category2Value8, R.id.Category2Value9, R.id.Category2Value10 };
			int valueindex, i;
			
			valueindex = StatusType.MODIFIER_NUM.ordinal() + MeritPoint.MAX_JOB_SPECIFIC_MERIT_POINT +  mJob * MeritPoint.MAX_JOB_SPECIFIC_MERIT_POINT * MeritPoint.MAX_JOB_SPECIFIC_MERIT_POINT_CATEGORY;
			for (i = 0; i < titles.length; i++) {
				TextView tv = (TextView)findViewById(titleids[i]);
				
				if (tv != null) {
					tv.setText(titles[i]);
					tv.setVisibility(View.VISIBLE);
				}

				Spinner spin = (Spinner)findViewById(valueids[i]);
				if (spin != null) {
					bindControlAndValue(valueids[i], values[valueindex + i]);
					spin.setVisibility(View.VISIBLE);
				}
			}
			for (; i < MeritPoint.MAX_JOB_SPECIFIC_MERIT_POINT; i++) {
				TextView tv = (TextView)findViewById(titleids[i]);
				if (tv != null) {
					tv.setVisibility(View.GONE);
				}

				Spinner spin = (Spinner)findViewById(valueids[i]);
				if (spin != null) {
					spin.setVisibility(View.GONE);
				}
			}
		}

		updateValues();
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveValues();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putInt("Job", mJob);
	}

	static public boolean startActivity(MeritPointEditActivity from, int job) {
		Intent intent = new Intent(from, JobSpecificMeritPointEditActivity.class);
	
		intent.putExtra("Job", job);
		from.startActivity(intent);

		return true;
	}

	static public boolean isComeFrom(Intent data) {
		return data.getStringExtra("From").equals("JobSpecificMeritPointEdit");
	}
}
