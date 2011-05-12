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

public class ControlBindableInteger implements ControlBindableValue {
	int mValue;

	public ControlBindableInteger(int value) {
		mValue = value;
	}

	public void setValue(String value) {
		try {
			mValue = Integer.valueOf(value);
		} catch (NumberFormatException e) {
			/* nop */
		}
	}

	public String getValue() {
		return ((Integer)mValue).toString();
	}

	public void setIntValue(int value) {
		mValue = value;
	}
	
	public int getIntValue() {
		return mValue;
	}
}
