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

public class JobTrait extends StatusModifierWithDescription implements Serializable {
	private static final long serialVersionUID = 1L;

	private long mId;
	private String mName;
	
	public JobTrait(long id, String name, String job, int level, String description) {
		super();
		
		mId = id;
		mName = name;
		mDescription = canonicalizeDescription(description);
		
		mNeedParseDescription = true;
	}


	@Override
	protected StatusValue handleCommonToken(StatusValue base, String parameter) {
		StatusValue v;
		
		v = super.handleCommonToken(base, parameter);
		if (v != null) {
			v.setValue(v.getAdditional()); // JobTrait should not be treat as Additional...
			v.setAdditional(0);
		}
		
		return v;
	}
	
	public long getId() {
		return mId;
	}

	public void setId(long mId) {
		this.mId = mId;
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String mDescription) {
		this.mDescription = mDescription;
	}
}
