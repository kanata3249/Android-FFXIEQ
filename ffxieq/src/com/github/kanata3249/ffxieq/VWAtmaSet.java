/*
   Copyright 2012 kanata3249

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

public class VWAtmaSet extends StatusModifier implements IStatus, Serializable {
	private static final long serialVersionUID = 1L;

	public static final int ATMA_MAX = 3;

	private Atma[] mAtma;

	public VWAtmaSet() { mAtma = new Atma[ATMA_MAX]; }; 


	// IStatus
	public StatusValue getStatus(JobLevelAndRace level, StatusType type) {
		StatusValue total = new StatusValue(0, 0);
		for (int i = 0; i < mAtma.length; i++) {
			if (mAtma[i] != null) {
				mAtma[i].parseDescription();
				total.add(mAtma[i].getStatus(level, type));
			}
		}
		return total;
	}

	public Atma getAtma(int index) {
		return mAtma[index];
	}

	public void setAtma(int index, long id) {
		mAtma[index] = Dao.instantiateVWAtma(id);
	}
	public SortedStringList getUnknownTokens() {
		SortedStringList unknownTokens = new SortedStringList();
		for (int i = 0; i < mAtma.length; i++) {
			if (mAtma[i] != null) {
				unknownTokens.mergeList(mAtma[i].getUnknownTokens());
			}
		}
		return unknownTokens;
	}

	public boolean reloadAtmas() {
		boolean updated = false;
		for (int i = 0; i < mAtma.length; i++) {
			if (mAtma[i] != null) {
				mAtma[i] = Dao.instantiateVWAtma(mAtma[i].getId());
				updated = true;
			}
		}
		if (updated) {
			for (int i = 0; i < mAtma.length; i++) {
				if (mAtma[i] != null) {
					mAtma[i].parseDescription();
				}
			}
		}
		return updated;
	}
}
