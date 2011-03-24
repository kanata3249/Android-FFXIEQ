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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Spinner;

public class MeritPointEditActivity extends FFXIEQBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.meritpointedit);
		
		updateValues();
	}
	
	void updateValues() {
		MeritPoint merits = getFFXICharacter().getMeritPoint();
		
		Spinner spin;
		
		spin = (Spinner) findViewById(R.id.HP);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.HP));
		spin = (Spinner) findViewById(R.id.MP);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.MP));
		spin = (Spinner) findViewById(R.id.STR);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.STR));
		spin = (Spinner) findViewById(R.id.DEX);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.DEX));
		spin = (Spinner) findViewById(R.id.VIT);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.VIT));
		spin = (Spinner) findViewById(R.id.AGI);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.AGI));
		spin = (Spinner) findViewById(R.id.INT);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.INT));
		spin = (Spinner) findViewById(R.id.MND);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.MND));
		spin = (Spinner) findViewById(R.id.CHR);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.CHR));
		spin = (Spinner) findViewById(R.id.HANDTOHAND);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_HANDTOHAND));
		spin = (Spinner) findViewById(R.id.DAGGER);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_DAGGER));
		spin = (Spinner) findViewById(R.id.SWORD);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_SWORD));
		spin = (Spinner) findViewById(R.id.GREATSWORD);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_GREATSWORD));
		spin = (Spinner) findViewById(R.id.AXE);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_AXE));
		spin = (Spinner) findViewById(R.id.GREATAXE);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_GREATAXE));
		spin = (Spinner) findViewById(R.id.SCYTH);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_SCYTH));
		spin = (Spinner) findViewById(R.id.POLEARM);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_POLEARM));
		spin = (Spinner) findViewById(R.id.KATANA);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_KATANA));
		spin = (Spinner) findViewById(R.id.GREATKATANA);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_GREATKATANA));
		spin = (Spinner) findViewById(R.id.CLUB);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_CLUB));
		spin = (Spinner) findViewById(R.id.STAFF);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_STAFF));
		spin = (Spinner) findViewById(R.id.GUARDING);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_GUARDING));
		spin = (Spinner) findViewById(R.id.EVASION);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_EVASION));
		spin = (Spinner) findViewById(R.id.SHIELD);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_SHIELD));
		spin = (Spinner) findViewById(R.id.PARRYING);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_PARRYING));
		spin = (Spinner) findViewById(R.id.DIVINEMAGIC);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_DIVINE_MAGIC));
		spin = (Spinner) findViewById(R.id.HEALINGMAGIC);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_HEALING_MAGIC));
		spin = (Spinner) findViewById(R.id.ENCHANCINGMAGIC);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_ENCHANCING_MAGIC));
		spin = (Spinner) findViewById(R.id.ENFEEBLINGMAGIC);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_ENFEEBLING_MAGIC));
		spin = (Spinner) findViewById(R.id.ELEMENTALMAGIC);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_ELEMENTAL_MAGIC));
		spin = (Spinner) findViewById(R.id.DARKMAGIC);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_DARK_MAGIC));
		spin = (Spinner) findViewById(R.id.SINGING);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_SINGING));
		spin = (Spinner) findViewById(R.id.STRINGINSTRUMENT);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_STRING_INSTRUMENT));
		spin = (Spinner) findViewById(R.id.WINDINSTRUMENT);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_WIND_INSTRUMENT));
		spin = (Spinner) findViewById(R.id.SUMMONING);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_SUMMONING));
		spin = (Spinner) findViewById(R.id.BLUEMAGIC);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SKILL_BLUE_MAGIC));
		spin = (Spinner) findViewById(R.id.ENMITY);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.Enmity) + 4);
		spin = (Spinner) findViewById(R.id.CRITICAL);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.CriticalRate));
		spin = (Spinner) findViewById(R.id.CRITICALDEFENCE);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.CriticalRateDefence));
		spin = (Spinner) findViewById(R.id.SPELLINTERRUPTION);
		if (spin != null)
			spin.setSelection(merits.getMeritPoint(StatusType.SpellInterruptionRate));
	}
	
	void saveValues() {
		MeritPoint merits = new MeritPoint();
		
		Spinner spin;
		
		spin = (Spinner) findViewById(R.id.HP);
		if (spin != null)
			merits.setMeritPoint(StatusType.HP, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.MP);
		if (spin != null)
			merits.setMeritPoint(StatusType.MP, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.STR);
		if (spin != null)
			merits.setMeritPoint(StatusType.STR, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.DEX);
		if (spin != null)
			merits.setMeritPoint(StatusType.DEX, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.VIT);
		if (spin != null)
			merits.setMeritPoint(StatusType.VIT, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.AGI);
		if (spin != null)
			merits.setMeritPoint(StatusType.AGI, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.INT);
		if (spin != null)
			merits.setMeritPoint(StatusType.INT, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.MND);
		if (spin != null)
			merits.setMeritPoint(StatusType.MND, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.CHR);
		if (spin != null)
			merits.setMeritPoint(StatusType.CHR, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.HANDTOHAND);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_HANDTOHAND, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.DAGGER);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_DAGGER, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.SWORD);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_SWORD, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.GREATSWORD);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_GREATSWORD, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.AXE);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_AXE, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.GREATAXE);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_GREATAXE, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.SCYTH);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_SCYTH, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.POLEARM);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_POLEARM, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.KATANA);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_KATANA, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.GREATKATANA);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_GREATKATANA, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.CLUB);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_CLUB, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.STAFF);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_STAFF, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.GUARDING);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_GUARDING, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.EVASION);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_EVASION, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.SHIELD);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_SHIELD, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.PARRYING);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_PARRYING, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.DIVINEMAGIC);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_DIVINE_MAGIC, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.HEALINGMAGIC);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_HEALING_MAGIC, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.ENCHANCINGMAGIC);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_ENCHANCING_MAGIC, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.ENFEEBLINGMAGIC);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_ENFEEBLING_MAGIC, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.ELEMENTALMAGIC);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_ELEMENTAL_MAGIC, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.DARKMAGIC);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_DARK_MAGIC, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.SINGING);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_SINGING, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.STRINGINSTRUMENT);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_STRING_INSTRUMENT, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.WINDINSTRUMENT);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_WIND_INSTRUMENT, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.SUMMONING);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_SUMMONING, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.BLUEMAGIC);
		if (spin != null)
			merits.setMeritPoint(StatusType.SKILL_BLUE_MAGIC, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.ENMITY);
		if (spin != null)
			merits.setMeritPoint(StatusType.Enmity, spin.getSelectedItemPosition() - 4);
		spin = (Spinner) findViewById(R.id.CRITICAL);
		if (spin != null)
			merits.setMeritPoint(StatusType.CriticalRate, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.CRITICALDEFENCE);
		if (spin != null)
			merits.setMeritPoint(StatusType.CriticalRateDefence, spin.getSelectedItemPosition());
		spin = (Spinner) findViewById(R.id.SPELLINTERRUPTION);
		if (spin != null)
			merits.setMeritPoint(StatusType.SpellInterruptionRate, spin.getSelectedItemPosition());
		
		getFFXICharacter().setMeritPoint(merits);
	}

	
	public void onSave(View view) {
		saveValues();
		
		Intent result = new Intent();
		
		result.putExtra("From", "MeritPointEdit");
		setResult(RESULT_OK, result);
		
		finish();
	}
	
	static public boolean startActivity(Activity from, int request) {
		Intent intent = new Intent(from, MeritPointEditActivity.class);
		
		from.startActivityForResult(intent, request);
		return true;
	}

	static public boolean startActivity(Fragment from, int request) {
		Intent intent = new Intent(from.getActivity(), MeritPointEditActivity.class);
		
		from.startActivityForResult(intent, request);
		return true;
	}

	static public boolean isComeFrom(Intent data) {
		return data.getStringExtra("From").equals("MeritPointEdit");
	}}
