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
import com.github.kanata3249.ffxieq.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.app.Activity;

public class CharacterStatusView extends ScrollView {
	public CharacterStatusView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		View children = ((Activity)context).getLayoutInflater().inflate(R.layout.statusview, null);
		this.addView(children);
	}

	public boolean bindFFXICharacter(FFXICharacter charinfo) {
		notifyDatasetChanged(charinfo);

		return true;
	}

	public void notifyDatasetChanged(FFXICharacter charInfo) {
		TextView tv;

    	tv = (TextView)findViewById(R.id.HP);
    	if (tv != null) {
    		tv.setText(charInfo.getHP(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.MP);
    	if (tv != null) {
    		tv.setText(charInfo.getMP(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.STR);
    	if (tv != null) {
    		tv.setText(charInfo.getSTR(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.DEX);
    	if (tv != null) {
    		tv.setText(charInfo.getDEX(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.VIT);
    	if (tv != null) {
    		tv.setText(charInfo.getVIT(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.AGI);
    	if (tv != null) {
    		tv.setText(charInfo.getAGI(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.INT);
    	if (tv != null) {
    		tv.setText(charInfo.getINT(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.MND);
    	if (tv != null) {
    		tv.setText(charInfo.getMND(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.CHR);
    	if (tv != null) {
    		tv.setText(charInfo.getCHR(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}

    	tv = (TextView)findViewById(R.id.Accuracy);
    	if (tv != null) {
    		tv.setText(charInfo.getAccuracy(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.Attack);
    	if (tv != null) {
    		tv.setText(charInfo.getAttack(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.AccuracySub);
    	if (tv != null) {
    		tv.setText(charInfo.getAccuracySub(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.AttackSub);
    	if (tv != null) {
    		tv.setText(charInfo.getAttackSub(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.AccuracyRange);
    	if (tv != null) {
    		tv.setText(charInfo.getAccuracyRange(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.AttackRange);
    	if (tv != null) {
    		tv.setText(charInfo.getAttackRange(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.Haste);
    	if (tv != null) {
    		tv.setText(charInfo.getHaste(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.Slow);
    	if (tv != null) {
    		tv.setText(charInfo.getSlow(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.SubtleBlow);
    	if (tv != null) {
    		tv.setText(charInfo.getSubtleBlow(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.StoreTP);
    	if (tv != null) {
    		tv.setText(charInfo.getStoreTP(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.Evasion);
    	if (tv != null) {
    		tv.setText(charInfo.getEvasion(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.DoubleAttack);
    	if (tv != null) {
    		tv.setText(charInfo.getDoubleAttack(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.TrippleAttack);
    	if (tv != null) {
    		tv.setText(charInfo.getTrippleAttack(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.QuadAttack);
    	if (tv != null) {
    		tv.setText(charInfo.getQuadAttack(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.CriticalRate);
    	if (tv != null) {
    		tv.setText(charInfo.getCriticalRate(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.CriticalDamage);
    	if (tv != null) {
    		tv.setText(charInfo.getCriticalDamage(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.CriticalDamage);
    	if (tv != null) {
    		tv.setText(charInfo.getCriticalDamage(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.Enmity);
    	if (tv != null) {
    		tv.setText(charInfo.getEnmity(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.AccuracyMagic);
    	if (tv != null) {
    		tv.setText(charInfo.getAccuracyMagic(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.AttackMagic);
    	if (tv != null) {
    		tv.setText(charInfo.getAttackMagic(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.D);
    	if (tv != null) {
    		tv.setText(charInfo.getD(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.DSub);
    	if (tv != null) {
    		tv.setText(charInfo.getDSub(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.DRange);
    	if (tv != null) {
    		tv.setText(charInfo.getDRange(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.Delay);
    	if (tv != null) {
    		tv.setText(charInfo.getDelay(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.DelaySub);
    	if (tv != null) {
    		tv.setText(charInfo.getDelaySub(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.DelayRange);
    	if (tv != null) {
    		tv.setText(charInfo.getDelayRange(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.Defence);
    	if (tv != null) {
    		tv.setText(charInfo.getDefence(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.DefenceMagic);
    	if (tv != null) {
    		tv.setText(charInfo.getDefenceMagic(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.DamageCutPhysical);
    	if (tv != null) {
    		tv.setText(charInfo.getDamageCutPhysical(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.DamageCutMagic);
    	if (tv != null) {
    		tv.setText(charInfo.getDamageCutMagic(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.DamageCutBreath);
    	if (tv != null) {
    		tv.setText(charInfo.getDamageCutBreath(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}

    	tv = (TextView)findViewById(R.id.RegistFire);
    	if (tv != null) {
    		tv.setText(charInfo.getRegistFire(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.RegistIce);
    	if (tv != null) {
    		tv.setText(charInfo.getRegistIce(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.RegistWind);
    	if (tv != null) {
    		tv.setText(charInfo.getRegistWind(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.RegistEarth);
    	if (tv != null) {
    		tv.setText(charInfo.getRegistEarth(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.RegistLightning);
    	if (tv != null) {
    		tv.setText(charInfo.getRegistLightning(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.RegistWater);
    	if (tv != null) {
    		tv.setText(charInfo.getRegistWater(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.RegistLight);
    	if (tv != null) {
    		tv.setText(charInfo.getRegistLight(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.RegistDark);
    	if (tv != null) {
    		tv.setText(charInfo.getRegistDark(FFXICharacter.GETSTATUS_STRING_SEPARATE));
    	}
    	tv = (TextView)findViewById(R.id.UnknownTokens);
    	if (tv != null) {
    		tv.setText(charInfo.getUnknownTokens());
    	}
	}
}
