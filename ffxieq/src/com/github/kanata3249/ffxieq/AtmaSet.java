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

import com.github.kanata3249.ffxi.status.*;

public class AtmaSet extends StatusModifier implements IStatus, Serializable {
	private static final long serialVersionUID = 1L;

	public static final int ATMA_MAX = 3;

	private Atma[] mAtma;
	private int mAbyssiteOfMerit;
	private int mAbyssiteOfFurtherance;

	public AtmaSet() { mAtma = new Atma[ATMA_MAX]; mAbyssiteOfMerit = 0; mAbyssiteOfFurtherance = 0; }; 


	// IStatus
	public StatusValue getStatus(JobLevelAndRace level, StatusType type) {
		StatusValue total = new StatusValue(0, 0);
		for (int i = 0; i < mAtma.length; i++) {
			if (mAtma[i] != null) {
				mAtma[i].parseDescription();
				total.add(mAtma[i].getStatus(level, type));
			}
		}
		switch(type) {
		case STR:
		case DEX:
		case AGI:
		case VIT:
		case INT:
		case MND:
		case CHR:
			total.add(new StatusValue(0, mAbyssiteOfFurtherance * 10 + 10));
			break;
		case HP:
			total.add(new StatusValue(0, 0, mAbyssiteOfMerit * 10 + 20));
			break;
		case MP:
			total.add(new StatusValue(0, 0, mAbyssiteOfMerit * 5 + 10));
			break;
		}
		return total;
	}


	public int getAbyssiteOfMerit() {
		return mAbyssiteOfMerit;
	}


	public void setAbyssiteOfMerit(int n) {
		mAbyssiteOfMerit = n;
	}


	public int getAbyssiteOfFurtherance() {
		return mAbyssiteOfFurtherance;
	}


	public void setAbyssiteOfFurtherance(int n) {
		mAbyssiteOfFurtherance = n;
	}
	
	public Atma getAtma(int index) {
		return mAtma[index];
	}

	public void setAtma(int index, long id) {
		mAtma[index] = Dao.instanciateAtma(id);
	}
	public String getUnknownTokens() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mAtma.length; i++) {
			if (mAtma[i] != null) {
				sb.append(mAtma[i].getUnknownTokens());
			}
		}
		return sb.toString();
	}
}
