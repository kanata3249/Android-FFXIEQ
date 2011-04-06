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
	int mDisplayParam;
	FFXICharacter mCharInfo;

	public CharacterStatusView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mDisplayParam = FFXICharacter.GETSTATUS_STRING_SEPARATE;
		mCharInfo = null;
		View children = ((Activity)context).getLayoutInflater().inflate(R.layout.statusview, null);
		this.addView(children);
	}

	public boolean bindFFXICharacter(FFXICharacter charinfo) {
		mCharInfo = charinfo;
		notifyDatasetChanged(charinfo);

		return true;
	}
	
	public void setDisplayParam(int param) {
		if (mDisplayParam != param) {
			mDisplayParam = param;
			if (mCharInfo != null) {
				notifyDatasetChanged(mCharInfo);
			}
		}
	}

	public void notifyDatasetChanged(FFXICharacter charInfo) {
		TextView tv;

    	tv = (TextView)findViewById(R.id.HP);
    	if (tv != null) {
    		tv.setText(charInfo.getHP(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.MP);
    	if (tv != null) {
    		tv.setText(charInfo.getMP(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.STR);
    	if (tv != null) {
    		tv.setText(charInfo.getSTR(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.DEX);
    	if (tv != null) {
    		tv.setText(charInfo.getDEX(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.VIT);
    	if (tv != null) {
    		tv.setText(charInfo.getVIT(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.AGI);
    	if (tv != null) {
    		tv.setText(charInfo.getAGI(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.INT);
    	if (tv != null) {
    		tv.setText(charInfo.getINT(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.MND);
    	if (tv != null) {
    		tv.setText(charInfo.getMND(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.CHR);
    	if (tv != null) {
    		tv.setText(charInfo.getCHR(mDisplayParam));
    	}

    	tv = (TextView)findViewById(R.id.Accuracy);
    	if (tv != null) {
    		tv.setText(charInfo.getAccuracy(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.Attack);
    	if (tv != null) {
    		tv.setText(charInfo.getAttack(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.AccuracySub);
    	if (tv != null) {
    		tv.setText(charInfo.getAccuracySub(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.AttackSub);
    	if (tv != null) {
    		tv.setText(charInfo.getAttackSub(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.AccuracyRange);
    	if (tv != null) {
    		tv.setText(charInfo.getAccuracyRange(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.AttackRange);
    	if (tv != null) {
    		tv.setText(charInfo.getAttackRange(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.Haste);
    	if (tv != null) {
    		tv.setText(charInfo.getHaste(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.Slow);
    	if (tv != null) {
    		tv.setText(charInfo.getSlow(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.SubtleBlow);
    	if (tv != null) {
    		tv.setText(charInfo.getSubtleBlow(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.StoreTP);
    	if (tv != null) {
    		tv.setText(charInfo.getStoreTP(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.Evasion);
    	if (tv != null) {
    		tv.setText(charInfo.getEvasion(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.DoubleAttack);
    	if (tv != null) {
    		tv.setText(charInfo.getDoubleAttack(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.TrippleAttack);
    	if (tv != null) {
    		tv.setText(charInfo.getTrippleAttack(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.QuadAttack);
    	if (tv != null) {
    		tv.setText(charInfo.getQuadAttack(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.CriticalRate);
    	if (tv != null) {
    		tv.setText(charInfo.getCriticalRate(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.CriticalDamage);
    	if (tv != null) {
    		tv.setText(charInfo.getCriticalDamage(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.CriticalDamage);
    	if (tv != null) {
    		tv.setText(charInfo.getCriticalDamage(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.Enmity);
    	if (tv != null) {
    		tv.setText(charInfo.getEnmity(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.AccuracyMagic);
    	if (tv != null) {
    		tv.setText(charInfo.getAccuracyMagic(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.AttackMagic);
    	if (tv != null) {
    		tv.setText(charInfo.getAttackMagic(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.D);
    	if (tv != null) {
    		tv.setText(charInfo.getD(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.DSub);
    	if (tv != null) {
    		tv.setText(charInfo.getDSub(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.DRange);
    	if (tv != null) {
    		tv.setText(charInfo.getDRange(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.Delay);
    	if (tv != null) {
    		tv.setText(charInfo.getDelay(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.DelaySub);
    	if (tv != null) {
    		tv.setText(charInfo.getDelaySub(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.DelayRange);
    	if (tv != null) {
    		tv.setText(charInfo.getDelayRange(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.Defence);
    	if (tv != null) {
    		tv.setText(charInfo.getDefence(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.DefenceMagic);
    	if (tv != null) {
    		tv.setText(charInfo.getDefenceMagic(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.DamageCutPhysical);
    	if (tv != null) {
    		tv.setText(charInfo.getDamageCutPhysical(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.DamageCutMagic);
    	if (tv != null) {
    		tv.setText(charInfo.getDamageCutMagic(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.DamageCutBreath);
    	if (tv != null) {
    		tv.setText(charInfo.getDamageCutBreath(mDisplayParam));
    	}

    	tv = (TextView)findViewById(R.id.RegistFire);
    	if (tv != null) {
    		tv.setText(charInfo.getRegistFire(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.RegistIce);
    	if (tv != null) {
    		tv.setText(charInfo.getRegistIce(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.RegistWind);
    	if (tv != null) {
    		tv.setText(charInfo.getRegistWind(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.RegistEarth);
    	if (tv != null) {
    		tv.setText(charInfo.getRegistEarth(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.RegistLightning);
    	if (tv != null) {
    		tv.setText(charInfo.getRegistLightning(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.RegistWater);
    	if (tv != null) {
    		tv.setText(charInfo.getRegistWater(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.RegistLight);
    	if (tv != null) {
    		tv.setText(charInfo.getRegistLight(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.RegistDark);
    	if (tv != null) {
    		tv.setText(charInfo.getRegistDark(mDisplayParam));
    	}
    	tv = (TextView)findViewById(R.id.UnknownTokens);
    	if (tv != null) {
    		tv.setText(charInfo.getUnknownTokens());
    	}
	}
}
