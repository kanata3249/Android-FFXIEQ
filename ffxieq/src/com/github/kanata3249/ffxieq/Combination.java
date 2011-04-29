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

import com.github.kanata3249.ffxi.status.*;

import java.io.Serializable;

public class Combination extends StatusModifierWithDescription implements Serializable  {
	private static final long serialVersionUID = 1L;

	private long mId;
	private long mCombinationID;

	
	public Combination(long id, long combinationid, String description) {
		super();
		
		mId = id;
		mCombinationID = combinationid;
		mDescription = description;
		
		mNeedParseDescription = true;
	}
	
	public long getId() {
		return mId;
	}

	public void setId(long mId) {
		this.mId = mId;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String mDescription) {
		this.mDescription = mDescription;
	}

	public long getCombinationID() {
		return mCombinationID;
	}
	public void setCombinationID(long id) {
		mCombinationID = id;
	}
}
