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

import com.github.kanata3249.ffxi.FFXIDAO;

public abstract class StatusModifier implements IStatus, Serializable {
	private static final long serialVersionUID = 1L;


	transient private StatusValue StatusValues[];

	static protected FFXIDAO Dao;
	
	static public void setDao(FFXIDAO dao) {
		Dao = dao;
	}
	public StatusModifier() {
	}
	
	protected void loadDefaultValues() {
		// called when this modifier is refered
		StatusValues = new StatusValue[StatusType.MODIFIER_NUM.ordinal()];
		
		for (int i = 0; i < StatusValues.length; i++) {
			StatusValues[i] = new StatusValue();
		}
	}

	// IStatus
	public StatusValue getStatus(JobLevelAndRace level, StatusType type) {
		if (StatusValues == null) {
			loadDefaultValues();
		}
		if (StatusValues[type.ordinal()] != null) {
			return StatusValues[type.ordinal()];
		} else {
			return new StatusValue(0, 0, 0);
		}
	}

	// Accessor
	final public void setStatus(StatusValue value, StatusType type) { StatusValues[type.ordinal()] = value; };
	final public StatusValue getStatus(StatusType type) { return StatusValues[type.ordinal()]; };
	
}
