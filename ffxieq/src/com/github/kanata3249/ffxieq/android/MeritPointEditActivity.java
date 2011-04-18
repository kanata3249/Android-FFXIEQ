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

public class MeritPointEditActivity extends FFXIEQBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.meritpointedit);

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
	
	public void onSave(View view) {
		saveValues();
		
		{
			MeritPoint merits = new MeritPoint();
			StatusType[] types = StatusType.values();
			ControlBindableInteger values[] = (ControlBindableInteger[])getTemporaryValues();
			String []enmity_entries;

			enmity_entries = getResources().getStringArray(R.array.Merits_Enmity_Entries);
			for (int i = 0; i < values.length; i++) {
				merits.setMeritPoint(types[i], values[i].getIntValue());
			}
			merits.setMeritPoint(StatusType.Enmity, values[StatusType.Enmity.ordinal()].getIntValue() - enmity_entries.length / 2);
			
			getFFXICharacter().setMeritPoint(merits);
		}
		
		Intent result = new Intent();
		
		result.putExtra("From", "MeritPointEdit");
		setResult(RESULT_OK, result);
		
		finish();
	}
	
	static public boolean startActivity(FFXIEQBaseActivity from, int request) {
		{  // Create temporary copy of merit point values
			ControlBindableInteger values[];
			MeritPoint merits;
	
			merits = from.getFFXICharacter().getMeritPoint();
			values = new ControlBindableInteger[StatusType.MODIFIER_NUM.ordinal()];
			StatusType[] types = StatusType.values();
			String []enmity_entries;

			enmity_entries = from.getResources().getStringArray(R.array.Merits_Enmity_Entries);
			for (int i = 0; i < values.length; i++) {
				values[i] = new ControlBindableInteger(merits.getMeritPoint(types[i]));
			}
			values[StatusType.Enmity.ordinal()].setIntValue(merits.getMeritPoint(StatusType.Enmity) + enmity_entries.length / 2);
			from.setTemporaryValues(values);
		}

		{
			Intent intent = new Intent(from, MeritPointEditActivity.class);
		
			from.startActivityForResult(intent, request);
		}
		return true;
	}

	static public boolean isComeFrom(Intent data) {
		return data.getStringExtra("From").equals("MeritPointEdit");
	}}
