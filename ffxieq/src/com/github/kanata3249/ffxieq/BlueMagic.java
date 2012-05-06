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

public class BlueMagic extends StatusModifierWithDescription implements Serializable {
	private static final long serialVersionUID = 1L;

	private long mId;
	private long mLevel;
	private long mBP;
	private long mSP;
	private String mName;
	
	public BlueMagic(long id, long level, long bp, long sp, String name, String description) {
		super();
		
		mId = id;
		mLevel = level;
		mBP = bp;
		mSP = sp;
		mName = name;
		mDescription = canonicalizeDescription(description);
		
		mNeedParseDescription = true;
	}

	public long getId() {
		return mId;
	}

	public void setId(long id) {
		this.mId = id;
	}
	
	public long getBP() {
		return mBP;
	}

	public void setBP(long bp) {
		this.mBP = bp;
	}

	public long getSP() {
		return mSP;
	}

	public void setSP(long sp) {
		this.mSP = sp;
	}

	public String getName() {
		return mName;
	}

	public void setLevel(long level) {
		this.mLevel = level;
	}

	public long getLevel() {
		return mLevel;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		this.mDescription = description;
	}
}
