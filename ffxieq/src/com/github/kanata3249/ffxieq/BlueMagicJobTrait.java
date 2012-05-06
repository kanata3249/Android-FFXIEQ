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

import com.github.kanata3249.ffxi.status.*;

public class BlueMagicJobTrait extends JobTrait {
	private static final long serialVersionUID = 1L;
	long mSubId;
	long mPoint;
	String mMagics;

	public BlueMagicJobTrait(long id, long subId, long point, String description, String magics) {
		super(id, "", "", 99, description);
		mSubId = subId;
		mPoint = point;
		mMagics = magics;
	}

	@Override
	protected StatusValue handleCommonToken(StatusValue base, String parameter) {
		StatusValue v;
		
		v = super.handleCommonToken(base, parameter);
		if (v != null) {
			v.setAdditional(v.getValue()); // BlueMagicJobTrait should be treat as Additional...
			v.setValue(0);
		}
		
		return v;
	}
	
	public long getSubId() {
		return mSubId;
	}
	public void setSubId(long subId) {
		this.mSubId = subId;
	}
	public long getPoint() {
		return mPoint;
	}
	public void setPoint(long point) {
		this.mPoint = point;
	}
	public String getMagics() {
		return mMagics;
	}
	public void setMagics(String magics) {
		this.mMagics = magics;
	}
}
