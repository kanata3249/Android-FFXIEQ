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

import com.github.kanata3249.ffxi.status.JobLevelAndRace;
import com.github.kanata3249.ffxi.status.StatusType;
import com.github.kanata3249.ffxieq.FFXICharacter;
import com.github.kanata3249.ffxieq.MeritPoint;
import com.github.kanata3249.ffxieq.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MeritPointEditActivity extends FFXIEQBaseActivity {
	long mCharacterIDToLink;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.meritpointedit);

		prepairControls((FFXIEQApplication)getApplication());
		ControlBindableInteger values[] = (ControlBindableInteger[])getTemporaryValues();

		bindControlAndValue(R.id.HP, values[StatusType.HP.ordinal()]);
		bindControlAndValue(R.id.MP, values[StatusType.MP.ordinal()]);
		bindControlAndValue(R.id.STR, values[StatusType.STR.ordinal()]);
		bindControlAndValue(R.id.DEX, values[StatusType.DEX.ordinal()]);
		bindControlAndValue(R.id.VIT, values[StatusType.VIT.ordinal()]);
		bindControlAndValue(R.id.AGI, values[StatusType.AGI.ordinal()]);
		bindControlAndValue(R.id.INT, values[StatusType.INT.ordinal()]);
		bindControlAndValue(R.id.MND, values[StatusType.MND.ordinal()]);
		bindControlAndValue(R.id.CHR, values[StatusType.CHR.ordinal()]);
		bindControlAndValue(R.id.HANDTOHAND, values[StatusType.SKILL_HANDTOHAND.ordinal()]);
		bindControlAndValue(R.id.DAGGER, values[StatusType.SKILL_DAGGER.ordinal()]);
		bindControlAndValue(R.id.SWORD, values[StatusType.SKILL_SWORD.ordinal()]);
		bindControlAndValue(R.id.GREATSWORD, values[StatusType.SKILL_GREATSWORD.ordinal()]);
		bindControlAndValue(R.id.AXE, values[StatusType.SKILL_AXE.ordinal()]);
		bindControlAndValue(R.id.GREATAXE, values[StatusType.SKILL_GREATAXE.ordinal()]);
		bindControlAndValue(R.id.SCYTH, values[StatusType.SKILL_SCYTH.ordinal()]);
		bindControlAndValue(R.id.POLEARM, values[StatusType.SKILL_POLEARM.ordinal()]);
		bindControlAndValue(R.id.KATANA, values[StatusType.SKILL_KATANA.ordinal()]);
		bindControlAndValue(R.id.GREATKATANA, values[StatusType.SKILL_GREATKATANA.ordinal()]);
		bindControlAndValue(R.id.CLUB, values[StatusType.SKILL_CLUB.ordinal()]);
		bindControlAndValue(R.id.STAFF, values[StatusType.SKILL_STAFF.ordinal()]);
		bindControlAndValue(R.id.ARCHERY, values[StatusType.SKILL_ARCHERY.ordinal()]);
		bindControlAndValue(R.id.MARKSMANSHIP, values[StatusType.SKILL_MARKSMANSHIP.ordinal()]);
		bindControlAndValue(R.id.THROWING, values[StatusType.SKILL_THROWING.ordinal()]);
		bindControlAndValue(R.id.GUARDING, values[StatusType.SKILL_GUARDING.ordinal()]);
		bindControlAndValue(R.id.EVASION, values[StatusType.SKILL_EVASION.ordinal()]);
		bindControlAndValue(R.id.SHIELD, values[StatusType.SKILL_SHIELD.ordinal()]);
		bindControlAndValue(R.id.PARRYING, values[StatusType.SKILL_PARRYING.ordinal()]);
		bindControlAndValue(R.id.DIVINEMAGIC, values[StatusType.SKILL_DIVINE_MAGIC.ordinal()]);
		bindControlAndValue(R.id.HEALINGMAGIC, values[StatusType.SKILL_HEALING_MAGIC.ordinal()]);
		bindControlAndValue(R.id.ENCHANCINGMAGIC, values[StatusType.SKILL_ENCHANCING_MAGIC.ordinal()]);
		bindControlAndValue(R.id.ENFEEBLINGMAGIC, values[StatusType.SKILL_ENFEEBLING_MAGIC.ordinal()]);
		bindControlAndValue(R.id.ELEMENTALMAGIC, values[StatusType.SKILL_ELEMENTAL_MAGIC.ordinal()]);
		bindControlAndValue(R.id.DARKMAGIC, values[StatusType.SKILL_DARK_MAGIC.ordinal()]);
		bindControlAndValue(R.id.SINGING, values[StatusType.SKILL_SINGING.ordinal()]);
		bindControlAndValue(R.id.STRINGINSTRUMENT, values[StatusType.SKILL_STRING_INSTRUMENT.ordinal()]);
		bindControlAndValue(R.id.WINDINSTRUMENT, values[StatusType.SKILL_WIND_INSTRUMENT.ordinal()]);
		bindControlAndValue(R.id.NINJUTSU, values[StatusType.SKILL_NINJUTSU.ordinal()]);
		bindControlAndValue(R.id.SUMMONING, values[StatusType.SKILL_SUMMONING.ordinal()]);
		bindControlAndValue(R.id.BLUEMAGIC, values[StatusType.SKILL_BLUE_MAGIC.ordinal()]);
		bindControlAndValue(R.id.GEOMANCERMAGIC, values[StatusType.SKILL_GEOMANCER_MAGIC.ordinal()]);
		bindControlAndValue(R.id.HANDBELL, values[StatusType.SKILL_HANDBELL.ordinal()]);
		bindControlAndValue(R.id.ENMITY, values[StatusType.Enmity.ordinal()]);
		bindControlAndValue(R.id.CRITICAL, values[StatusType.CriticalRate.ordinal()]);
		bindControlAndValue(R.id.CRITICALDEFENCE, values[StatusType.CriticalRateDefence.ordinal()]);
		bindControlAndValue(R.id.SPELLINTERRUPTION, values[StatusType.SpellInterruptionRate.ordinal()]);

		updateValues();
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveValues();
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN){
			if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
				prepairControls((FFXIEQApplication)getApplication());
				saveValues();
				
				{
					MeritPoint merits = new MeritPoint();
					MeritPoint oldmerits = getFFXICharacter().getMeritPoint();
					StatusType[] types = StatusType.values();
					ControlBindableInteger values[] = (ControlBindableInteger[])getTemporaryValues();
					String []enmity_entries;
					boolean modified;
		
					modified = false;
					enmity_entries = getResources().getStringArray(R.array.Merits_Enmity_Entries);
					int i;
					for (i = 0; i < StatusType.MODIFIER_NUM.ordinal(); i++) {
						if (oldmerits.getMeritPoint(types[i]) != values[i].getIntValue() && types[i] != StatusType.Enmity)
							modified = true;
						merits.setMeritPoint(types[i], values[i].getIntValue());
					}
					if (oldmerits.getMeritPoint(StatusType.Enmity)!= values[StatusType.Enmity.ordinal()].getIntValue() - enmity_entries.length / 2)
						modified = true;
					merits.setMeritPoint(StatusType.Enmity, values[StatusType.Enmity.ordinal()].getIntValue() - enmity_entries.length / 2);
					for (int job = 0; job < JobLevelAndRace.JOB_MAX; job++) {
						for (int category = 0; category < MeritPoint.MAX_JOB_SPECIFIC_MERIT_POINT_CATEGORY; category++) {
							for (int index = 0; index < MeritPoint.MAX_JOB_SPECIFIC_MERIT_POINT; index++) {
								if (oldmerits.getJobSpecificMeritPoint(job, category, index) != values[i].getIntValue())
									modified = true;
								merits.setJobSpecificMeritPoint(job, category, index, values[i++].getIntValue());
							}
						}
					}
					
					if (modified)
						getFFXICharacter().setMeritPoint(merits);
				}
				
				Intent result = new Intent();
				
				result.putExtra("From", "MeritPointEdit");
				setResult(RESULT_OK, result);
				
				finish();
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}
	
	static public boolean startActivity(FFXIEQActivity from, int request) {
		from.setTemporaryValues(null);
		if (prepairControls((FFXIEQApplication)from.getApplication())) {
			Intent intent = new Intent(from, MeritPointEditActivity.class);
			
			from.startActivityForResult(intent, request);
			return true;
		}
		return false;
	}

	static public boolean isComeFrom(Intent data) {
		return data.getStringExtra("From").equals("MeritPointEdit");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.meritpointedit, menu);
		
		String jobtitles[] = getResources().getStringArray(R.array.Jobs);
		Menu submenu = menu.findItem(R.id.JobSpecificMeritPoint).getSubMenu();
		submenu.findItem(R.id.JobSpecificMeritPointEdit01).setTitle(jobtitles[0]);
		submenu.findItem(R.id.JobSpecificMeritPointEdit02).setTitle(jobtitles[1]);
		submenu.findItem(R.id.JobSpecificMeritPointEdit03).setTitle(jobtitles[2]);
		submenu.findItem(R.id.JobSpecificMeritPointEdit04).setTitle(jobtitles[3]);
		submenu.findItem(R.id.JobSpecificMeritPointEdit05).setTitle(jobtitles[4]);
		submenu.findItem(R.id.JobSpecificMeritPointEdit06).setTitle(jobtitles[5]);
		submenu.findItem(R.id.JobSpecificMeritPointEdit07).setTitle(jobtitles[6]);
		submenu.findItem(R.id.JobSpecificMeritPointEdit08).setTitle(jobtitles[7]);
		submenu.findItem(R.id.JobSpecificMeritPointEdit09).setTitle(jobtitles[8]);
		submenu.findItem(R.id.JobSpecificMeritPointEdit10).setTitle(jobtitles[9]);
		submenu.findItem(R.id.JobSpecificMeritPointEdit11).setTitle(jobtitles[10]);
		submenu.findItem(R.id.JobSpecificMeritPointEdit12).setTitle(jobtitles[11]);
		submenu.findItem(R.id.JobSpecificMeritPointEdit13).setTitle(jobtitles[12]);
		submenu.findItem(R.id.JobSpecificMeritPointEdit14).setTitle(jobtitles[13]);
		submenu.findItem(R.id.JobSpecificMeritPointEdit15).setTitle(jobtitles[14]);
		submenu.findItem(R.id.JobSpecificMeritPointEdit16).setTitle(jobtitles[15]);
		submenu.findItem(R.id.JobSpecificMeritPointEdit17).setTitle(jobtitles[16]);
		submenu.findItem(R.id.JobSpecificMeritPointEdit18).setTitle(jobtitles[17]);
		submenu.findItem(R.id.JobSpecificMeritPointEdit19).setTitle(jobtitles[18]);
		submenu.findItem(R.id.JobSpecificMeritPointEdit20).setTitle(jobtitles[19]);
		submenu.findItem(R.id.JobSpecificMeritPointEdit21).setTitle(jobtitles[20]);
		submenu.findItem(R.id.JobSpecificMeritPointEdit22).setTitle(jobtitles[21]);
		
		return true;
	}

	@Override
	protected Dialog onCreateDialog(final int id) {
		Dialog dialog;
		Builder builder;
		switch (id) {
		case R.layout.meritpointlinkdialog:
	    	dialog = new Dialog(this);
			dialog.setContentView(R.layout.meritpointlinkdialog);
			dialog.setTitle(getString(R.string.LinkMeritPoint));

			return dialog;
		case R.string.QueryLinkMeritPoint:
			builder = new AlertDialog.Builder(this);
			builder.setCancelable(true);
	    	builder.setMessage(getString(R.string.QueryLinkMeritPoint));
	    	builder.setTitle(getString(R.string.LinkMeritPoint));
	    	builder.setPositiveButton(R.string.LinkOK, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					long id;
					FFXICharacter refChar, charInfo;
					
					id = mCharacterIDToLink;
					charInfo = getFFXICharacter();
					refChar = getSettings().loadCharInfo(id);
					charInfo.setMeritPointId(refChar.getMeritPointId());
					charInfo.setMeritPoint(refChar.getMeritPoint());
					reloadMeritpoint();
					dismissDialog(R.string.QueryLinkMeritPoint);
					dismissDialog(R.layout.meritpointlinkdialog);
					showDialog(R.string.LinkMeritPointSuccess);
				}
			});
	    	builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(R.string.QueryLinkMeritPoint);
				}
			});
			dialog = builder.create();
			return dialog;
		case R.string.LinkMeritPointSuccess:
			builder = new AlertDialog.Builder(this);
			builder.setCancelable(true);
	    	builder.setMessage(getString(R.string.LinkMeritPointSuccess));
	    	builder.setTitle(getString(R.string.LinkMeritPoint));
	    	builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(R.string.LinkMeritPointSuccess);
				}
			});
			dialog = builder.create();
			return dialog;
		case R.string.UnlinkMeritPointSuccess:
			builder = new AlertDialog.Builder(this);
			builder.setCancelable(true);
	    	builder.setMessage(getString(R.string.UnlinkMeritPointSuccess));
	    	builder.setTitle(getString(R.string.LinkMeritPoint));
	    	builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(R.string.UnlinkMeritPointSuccess);
				}
			});
			dialog = builder.create();
			return dialog;
		}
		return super.onCreateDialog(id);
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
    	Button btn;

		switch (id) {
		case R.layout.meritpointlinkdialog:
	    	final CharacterSelectorView cs = (CharacterSelectorView)dialog.findViewById(R.id.CharacterSelector);
	    	if (cs != null) {
		        cs.setParam(getSettings(), getDAO(), getSettings().getMeritPointMasterId(getFFXICharacter().getMeritPointId()));
	    	}
			btn = (Button)dialog.findViewById(R.id.Cancel);
			if (btn != null) {
				btn.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						dismissDialog(R.layout.meritpointlinkdialog);
					}
				});
			}
			btn = (Button)dialog.findViewById(R.id.Unlink);
			if (btn != null) {
				btn.setEnabled(getCharacterID() != getFFXICharacter().getMeritPointId());
				btn.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						getFFXICharacter().setMeritPointId(getCharacterID());
						dismissDialog(R.layout.meritpointlinkdialog);
						showDialog(R.string.UnlinkMeritPointSuccess);
					}
				});
			}
			btn = (Button)dialog.findViewById(R.id.LinkOK);
			if (btn != null) {
				btn.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						mCharacterIDToLink = cs.getSelectedItemId();
						showDialog(R.string.QueryLinkMeritPoint);
					}
				});
			}
			return;
		}
		super.onPrepareDialog(id, dialog);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.Revert:
			// Reload from character data.
			reloadMeritpoint();
			return true;
		case R.id.LinkMeritPoint:
			showDialog(R.layout.meritpointlinkdialog);
			return true;
		case R.id.JobSpecificMeritPointEdit01:
		case R.id.JobSpecificMeritPointEdit02:
		case R.id.JobSpecificMeritPointEdit03:
		case R.id.JobSpecificMeritPointEdit04:
		case R.id.JobSpecificMeritPointEdit05:
		case R.id.JobSpecificMeritPointEdit06:
		case R.id.JobSpecificMeritPointEdit07:
		case R.id.JobSpecificMeritPointEdit08:
		case R.id.JobSpecificMeritPointEdit09:
		case R.id.JobSpecificMeritPointEdit10:
		case R.id.JobSpecificMeritPointEdit11:
		case R.id.JobSpecificMeritPointEdit12:
		case R.id.JobSpecificMeritPointEdit13:
		case R.id.JobSpecificMeritPointEdit14:
		case R.id.JobSpecificMeritPointEdit15:
		case R.id.JobSpecificMeritPointEdit16:
		case R.id.JobSpecificMeritPointEdit17:
		case R.id.JobSpecificMeritPointEdit18:
		case R.id.JobSpecificMeritPointEdit19:
		case R.id.JobSpecificMeritPointEdit20:
		case R.id.JobSpecificMeritPointEdit21:
		case R.id.JobSpecificMeritPointEdit22:
			startJobSpecificMeritPointEdit(item.getItemId());
			return true;
		}
		return false;
	}
	
	private void startJobSpecificMeritPointEdit(int id) {
		int job;

		switch (id) {
		case R.id.JobSpecificMeritPointEdit01:
			job = JobLevelAndRace.WAR;
			break;
		case R.id.JobSpecificMeritPointEdit02:
			job = JobLevelAndRace.MNK;
			break;
		case R.id.JobSpecificMeritPointEdit03:
			job = JobLevelAndRace.WHM;
			break;
		case R.id.JobSpecificMeritPointEdit04:
			job = JobLevelAndRace.BLM;
			break;
		case R.id.JobSpecificMeritPointEdit05:
			job = JobLevelAndRace.RDM;
			break;
		case R.id.JobSpecificMeritPointEdit06:
			job = JobLevelAndRace.THF;
			break;
		case R.id.JobSpecificMeritPointEdit07:
			job = JobLevelAndRace.PLD;
			break;
		case R.id.JobSpecificMeritPointEdit08:
			job = JobLevelAndRace.DRK;
			break;
		case R.id.JobSpecificMeritPointEdit09:
			job = JobLevelAndRace.BST;
			break;
		case R.id.JobSpecificMeritPointEdit10:
			job = JobLevelAndRace.BRD;
			break;
		case R.id.JobSpecificMeritPointEdit11:
			job = JobLevelAndRace.RNG;
			break;
		case R.id.JobSpecificMeritPointEdit12:
			job = JobLevelAndRace.SAM;
			break;
		case R.id.JobSpecificMeritPointEdit13:
			job = JobLevelAndRace.NIN;
			break;
		case R.id.JobSpecificMeritPointEdit14:
			job = JobLevelAndRace.DRG;
			break;
		case R.id.JobSpecificMeritPointEdit15:
			job = JobLevelAndRace.SMN;
			break;
		case R.id.JobSpecificMeritPointEdit16:
			job = JobLevelAndRace.BLU;
			break;
		case R.id.JobSpecificMeritPointEdit17:
			job = JobLevelAndRace.COR;
			break;
		case R.id.JobSpecificMeritPointEdit18:
			job = JobLevelAndRace.PUP;
			break;
		case R.id.JobSpecificMeritPointEdit19:
			job = JobLevelAndRace.DNC;
			break;
		case R.id.JobSpecificMeritPointEdit20:
			job = JobLevelAndRace.SCH;
			break;
		case R.id.JobSpecificMeritPointEdit21:
			job = JobLevelAndRace.RUN;
			break;
		case R.id.JobSpecificMeritPointEdit22:
			job = JobLevelAndRace.GEO;
			break;
		default:
			return;
		}
		
		JobSpecificMeritPointEditActivity.startActivity(this, job);
	}
	
	static public boolean prepairControls(FFXIEQApplication app) {
		if (app.getTemporaryValues() == null) {
			try {  // Create temporary copy of merit point values
				ControlBindableInteger values[];
				MeritPoint merits;
		
				merits = app.getFFXICharacter().getMeritPoint();
				values = new ControlBindableInteger[StatusType.MODIFIER_NUM.ordinal() + JobLevelAndRace.JOB_MAX * MeritPoint.MAX_JOB_SPECIFIC_MERIT_POINT_CATEGORY * MeritPoint.MAX_JOB_SPECIFIC_MERIT_POINT];
				StatusType[] types = StatusType.values();
				String []enmity_entries;
	
				enmity_entries = app.getResources().getStringArray(R.array.Merits_Enmity_Entries);
				int i;
				for (i = 0; i < StatusType.MODIFIER_NUM.ordinal(); i++) {
					values[i] = new ControlBindableInteger(merits.getMeritPoint(types[i]));
				}
				values[StatusType.Enmity.ordinal()].setIntValue(merits.getMeritPoint(StatusType.Enmity) + enmity_entries.length / 2);
				for (int job = 0; job < JobLevelAndRace.JOB_MAX; job++) {
					for (int category = 0; category < MeritPoint.MAX_JOB_SPECIFIC_MERIT_POINT_CATEGORY; category++) {
						for (int index = 0; index < MeritPoint.MAX_JOB_SPECIFIC_MERIT_POINT; index++) {
							values[i++] = new ControlBindableInteger(merits.getJobSpecificMeritPoint(job, category, index));
						}
					}
				}
				app.setTemporaryValues(values);
			} catch (OutOfMemoryError e) {
				throw e;
			}
		}
		return true;
	}
	
	private void reloadMeritpoint() {
		// Reload from character data.
		ControlBindableInteger values[];
		MeritPoint merits;

		merits = getFFXICharacter().getMeritPoint();
		values = (ControlBindableInteger[])getTemporaryValues();
		StatusType[] types = StatusType.values();
		String []enmity_entries;

		enmity_entries = getResources().getStringArray(R.array.Merits_Enmity_Entries);
		int i;
		for (i = 0; i < StatusType.MODIFIER_NUM.ordinal(); i++) {
			values[i] = new ControlBindableInteger(merits.getMeritPoint(types[i]));
		}
		values[StatusType.Enmity.ordinal()].setIntValue(merits.getMeritPoint(StatusType.Enmity) + enmity_entries.length / 2);
		for (int job = 0; job < JobLevelAndRace.JOB_MAX; job++) {
			for (int category = 0; category < MeritPoint.MAX_JOB_SPECIFIC_MERIT_POINT_CATEGORY; category++) {
				for (int index = 0; index < MeritPoint.MAX_JOB_SPECIFIC_MERIT_POINT; index++) {
					values[i++] = new ControlBindableInteger(merits.getJobSpecificMeritPoint(job, category, index));
				}
			}
		}
		updateValues();
	}
}
