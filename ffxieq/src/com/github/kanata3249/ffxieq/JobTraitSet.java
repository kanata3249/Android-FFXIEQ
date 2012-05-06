/*
   Copyright 2011-2012 kanata3249

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
package com.github.kanata3249.ffxieq;

import java.io.Serializable;

import com.github.kanata3249.ffxi.status.*;

public class JobTraitSet extends StatusModifier implements IStatus, Serializable {
	private static final long serialVersionUID = 1L;

	private JobTrait[] mJobTraits;
	private JobTrait[] mSubJobTraits;
	private JobTrait[] mBlueMagicSetJobTraits;
	private JobLevelAndRace mLevel;

	public JobTraitSet() {
	}
	
	public void setLevel(JobLevelAndRace level, BlueMagicSet blueMagicSet) {
		if (mLevel != null && mLevel.equals(level) && (blueMagicSet == null || !blueMagicSet.needParseSetJobTraits()))
			return;
		mLevel = new JobLevelAndRace(level);
		
		mJobTraits = Dao.getJobTraits(level.getJob(), level.getLevel());
		for (int i = 0; i < mJobTraits.length; i++) {
			if (mJobTraits[i] != null) {
				mJobTraits[i].parseDescription();
			}
		}
		mSubJobTraits = Dao.getJobTraits(level.getSubJob(), level.getSubLevel());
		for (int i = 0; i < mSubJobTraits.length; i++) {
			if (mSubJobTraits[i] != null) {
				mSubJobTraits[i].parseDescription();
			}
		}
		if (blueMagicSet != null && blueMagicSet.needParseSetJobTraits()) {
			mBlueMagicSetJobTraits = blueMagicSet.getJobTraits();
			if (mBlueMagicSetJobTraits != null) {
				for (int i = 0; i < mBlueMagicSetJobTraits.length; i++) {
					if (mBlueMagicSetJobTraits[i] != null) {
						mBlueMagicSetJobTraits[i].parseDescription();
					}
				}
			}
		}
	}

	// IStatus
	public StatusValue getStatus(JobLevelAndRace level, StatusType type) {
		StatusValue total = new StatusValue(0, 0);
		StatusValue subtotal = new StatusValue(0, 0);
		StatusValue blutotal = new StatusValue(0, 0);

		for (int i = 0; i < mJobTraits.length; i++) {
			total.add(mJobTraits[i].getStatus(level, type));
		}
		for (int i = 0; i < mSubJobTraits.length; i++) {
			subtotal.add(mSubJobTraits[i].getStatus(level, type));
		}
		if (mBlueMagicSetJobTraits != null) {
			for (int i = 0; i < mBlueMagicSetJobTraits.length; i++) {
				blutotal.add(mBlueMagicSetJobTraits[i].getStatus(level, type));
			}
		}

		if (subtotal.getValue() > total.getValue()
			|| subtotal.getAdditional() > total.getAdditional()
			|| subtotal.getAdditionalPercent() > total.getAdditionalPercent())
			total = subtotal;

		if (blutotal.getValue() > total.getValue()
			|| blutotal.getAdditional() > total.getAdditional()
			|| blutotal.getAdditionalPercent() > total.getAdditionalPercent())
			total = blutotal;

		return total;
	}

	public SortedStringList getUnknownTokens() {
		SortedStringList unknownTokens = new SortedStringList();
		for (int i = 0; i < mJobTraits.length; i++) {
			if (mJobTraits[i] != null) {
				unknownTokens.mergeList(mJobTraits[i].getUnknownTokens());
			}
		}
		for (int i = 0; i < mSubJobTraits.length; i++) {
			if (mSubJobTraits[i] != null) {
				unknownTokens.mergeList(mSubJobTraits[i].getUnknownTokens());
			}
		}
		if (mBlueMagicSetJobTraits != null) {
			for (int i = 0; i < mBlueMagicSetJobTraits.length; i++) {
				if (mBlueMagicSetJobTraits[i] != null) {
					unknownTokens.mergeList(mBlueMagicSetJobTraits[i].getUnknownTokens());
				}
			}
		}
		return unknownTokens;
	}
}
