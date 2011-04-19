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
package com.github.kanata3249.ffxi.status;

import java.io.Serializable;

public class StatusValue implements Serializable {
	private static final long serialVersionUID = 1L;

	private int value;
	private int additional;
	private int additionalPercent;
	private int cap;

	public StatusValue() { this(0, 0, 0); };
	public StatusValue(int value, int additional) { this(value, additional, 0); };
	public StatusValue(StatusValue from) { this(from.value, from.additional, from.additionalPercent); };
	public StatusValue(int value, int additional, int percent) { this(value, additional, percent, 0); };
	public StatusValue(int value, int additional, int percent, int cap) { this.value = value; this.additional = additional; this.additionalPercent = percent; this.cap = cap; };

	// Accessor
	public void setValue(int value) { this.value = value; };
	public void setAdditional(int additional) { this.additional = additional; };
	public void setAdditionalPercent(int additionalPercent) { this.additionalPercent = additionalPercent; };
	public void setCap(int cap) { this.cap = cap; };

	public int getValue() { return value; };
	public int getAdditional() { return additional; };
	public int getAdditionalPercent() { return additionalPercent; };
	public int getCap() { return cap; };
	
	public void add(StatusValue value) { this.value += value.value; this.additional += value.additional; this.additionalPercent += value.additionalPercent; if (this.cap == 0) this.cap = value.cap; };
	public void diff(StatusValue value) {
		int v1, p1;
		int v2, p2;

		if (this.value == 0 && this.additional == 0 && this.additionalPercent != 0) {
			this.value = this.additionalPercent;
			this.additional = 0;
			this.additionalPercent = value.additionalPercent - this.additionalPercent;
			this.cap = 0;
		} else {
			v1 = (this.value + this.additional);
			p1 = v1 * (this.additionalPercent) / 100;
			if (cap > 0)
				p1 = Math.min(p1, this.cap);
			v1 += p1;
			v2 = (value.value + value.additional);
			p2 = v2 * (value.additionalPercent) / 100;
			if (value.cap > 0)
				p2 = Math.min(p2, value.cap);
			v2 += p2;
			
			this.value = v1;
			this.additional = v2 - v1;
			this.additionalPercent = 0;
			this.cap = 0;
		}
		return;
	}
}
