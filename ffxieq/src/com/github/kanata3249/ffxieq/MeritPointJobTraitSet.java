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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Arrays;

import com.github.kanata3249.ffxi.status.*;

public class MeritPointJobTraitSet extends StatusModifier implements IStatus, Serializable {
	private static final long serialVersionUID = 1L;
	
	JobLevelAndRace mLevel;
	int mJobSpecificMeritPointUsing[][][];

	private ArrayList<JobTrait> mJobTraits;

	public MeritPointJobTraitSet(JobLevelAndRace level, int jobSpecificMeritPoint[][][]) {
		setLevel(level, jobSpecificMeritPoint);
	}
	
	public void setLevel(JobLevelAndRace level, int jobSpecificMeritPoint[][][]) {
		if (mLevel != null && mLevel.equals(level)
			&& Arrays.deepEquals(mJobSpecificMeritPointUsing, jobSpecificMeritPoint)) {
				return;
		}
		mLevel = new JobLevelAndRace(level);
		mJobTraits = new ArrayList<JobTrait>();
		{ // Deep copy JobSpecificMeritPoint values
			ObjectOutputStream oos;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();;
			ObjectInputStream ois;
			ByteArrayInputStream bais;

			try {
				oos = new ObjectOutputStream(baos);
				oos.writeObject(jobSpecificMeritPoint);
				oos.close();
				bais = new ByteArrayInputStream(baos.toByteArray());
				ois = new ObjectInputStream(bais);
				mJobSpecificMeritPointUsing = (int [][][])ois.readObject();
				ois.close();
			} catch (StreamCorruptedException e) {
				return;
			} catch (ClassNotFoundException e) {
				return;
			} catch (IOException e) {
				return;
			}
		}

		long cat1[] = Dao.getJobSpecificMeritPointItemIds(level.getJob(), 1);
		long cat2[] = Dao.getJobSpecificMeritPointItemIds(level.getJob(), 2);

		if (cat1 != null) {
			for (int i = 0; i < cat1.length; i++) {
				int point = jobSpecificMeritPoint[level.getJob()][0][i];
				if (point > 0) {
					mJobTraits.add(Dao.instantiateMeritPointJobTrait(cat1[i], point));
				}
			}
		}
		if (cat2 != null) {
			for (int i = 0; i < cat2.length; i++) {
				int point = jobSpecificMeritPoint[level.getJob()][1][i];
				if (point > 0) {
					mJobTraits.add(Dao.instantiateMeritPointJobTrait(cat2[i], point));
				}
			}
		}
	}

	// IStatus
	public StatusValue getStatus(JobLevelAndRace level, StatusType type) {
		StatusValue total = new StatusValue(0, 0);
		for (int i = 0; i < mJobTraits.size(); i++) {
			JobTrait trait = mJobTraits.get(i);
			
			trait.parseDescription();
			total.add(trait.getStatus(level, type));
		}
		return total;
	}

	public String getUnknownTokens() {
		return "";
	}
}
