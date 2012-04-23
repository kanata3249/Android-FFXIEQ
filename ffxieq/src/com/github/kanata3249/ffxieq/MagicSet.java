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
package com.github.kanata3249.ffxieq;

import java.io.Serializable;
import java.util.ArrayList;

import com.github.kanata3249.ffxi.status.*;

public class MagicSet extends StatusModifier implements IStatus, Serializable {
	private static final long serialVersionUID = 1L;

	private ArrayList<Magic> mMagics;

	public MagicSet() { mMagics = new ArrayList<Magic>(); }; 


	// IStatus
	public StatusValue getStatus(JobLevelAndRace level, StatusType type) {
		StatusValue total = new StatusValue(0, 0);
		if (mMagics != null) {
			for (int i = 0; i < mMagics.size(); i++) {
				Magic magic;
				
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

	public Magic getMagic(int index) {
		return mMagics.get(index);
	}

	public void setMagic(int index, long id) {
		if (id < 0)
			mMagics.remove(index);
		else
			mMagics.set(index, Dao.instantiateMagic(id));
	}
	
	public void addMagic(long id) {
		mMagics.add(Dao.instantiateMagic(id));
	}

	public SortedStringList getUnknownTokens() {
		SortedStringList unknownTokens = new SortedStringList();
		if (mMagics != null) {
			for (int i = 0; i < mMagics.size(); i++) {
				Magic magic;
				
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
				Magic magic;
				
				magic = mMagics.get(i);
				mMagics.set(i, Dao.instantiateMagic(magic.getId()));
				mMagics.get(i).parseDescription();
			}
		}
		return updated;
	}

	public boolean reloadMagicsForUpdatingDatabase() {
		boolean updated = false;
		for (int i = 0; i < mMagics.size(); i++) {
			if (mMagics.get(i) != null) {
				Magic magic = Dao.instantiateMagic(mMagics.get(i).getId());
				if (magic == null || !magic.getName().equals(mMagics.get(i).getName())) {
					// find
					magic = Dao.findMagic(mMagics.get(i).getName());
					if (magic != null) {
						mMagics.set(i, magic);
						updated = true;
					} else {
						setMagic(i, -1);
						updated = true;
					}
				}
			}
		}
		return updated;
	}
}
