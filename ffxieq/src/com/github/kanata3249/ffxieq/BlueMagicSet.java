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
import java.util.ArrayList;

import com.github.kanata3249.ffxi.status.*;

public class BlueMagicSet extends StatusModifier implements IStatus, Serializable {
	private static final long serialVersionUID = 1L;

	private ArrayList<BlueMagic> mMagics;

	public BlueMagicSet() { mMagics = new ArrayList<BlueMagic>(); }; 


	// IStatus
	public StatusValue getStatus(JobLevelAndRace level, StatusType type) {
		StatusValue total = new StatusValue(0, 0);
		if (mMagics != null) {
			for (int i = 0; i < mMagics.size(); i++) {
				BlueMagic magic;
				
				magic = mMagics.get(i);
				magic.parseDescription();
				total.add(magic.getStatus(level, type));
			}
		}
		return total;
	}

	public int getNumMagic() {
		return mMagics.size();
	}

	public BlueMagic getMagic(int index) {
		return mMagics.get(index);
	}

	public void addMagic(long id) {
		mMagics.add(Dao.instantiateBlueMagic(id));
	}

	public void removeMagic(long id) {
		for (int i = 0; i < mMagics.size(); i++) {
			BlueMagic magic;
			
			magic = mMagics.get(i);
			if (magic.getId() == id) {
				mMagics.remove(i);
				break;
			}
		}
	}

	public SortedStringList getUnknownTokens() {
		SortedStringList unknownTokens = new SortedStringList();
		if (mMagics != null) {
			for (int i = 0; i < mMagics.size(); i++) {
				BlueMagic magic;
				
				magic = mMagics.get(i);
				magic.parseDescription();
				unknownTokens.mergeList(magic.getUnknownTokens());
			}
		}
		return unknownTokens;
	}

	public boolean reloadMagics() {
		boolean updated = false;
		if (mMagics != null) {
			for (int i = 0; i < mMagics.size(); i++) {
				BlueMagic magic;
				
				magic = mMagics.get(i);
				mMagics.set(i, Dao.instantiateBlueMagic(magic.getId()));
				mMagics.get(i).parseDescription();
			}
		}
		return updated;
	}


	public boolean isSet(long itemId) {
		for (int i = 0; i < mMagics.size(); i++) {
			BlueMagic magic;
			
			magic = mMagics.get(i);
			if (magic.getId() == itemId) {
				return true;
			}
		}
		return false;
	}
}
