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

public class JobLevelAndRace implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int Hum = 0;
	public static final int Elv = 1;
	public static final int Tar = 2;
	public static final int Mit = 3;
	public static final int Gal = 4;
	
	public static final int WAR = 0;
	public static final int MNK = 1;
	public static final int WHM = 2;
	public static final int BLM = 3;
	public static final int RDM = 4;
	public static final int THF = 5;
	public static final int PLD = 6;
	public static final int DRK = 7;
	public static final int BST = 8;
	public static final int BRD = 9;
	public static final int RNG = 10;
	public static final int SAM = 11;
	public static final int NIN = 12;
	public static final int DRG = 13;
	public static final int SMN = 14;
	public static final int BLU = 15;
	public static final int COR = 16;
	public static final int PUP = 17;
	public static final int DNC = 18;
	public static final int SCH = 19;
	public static final int RUN = 20;
	public static final int GEO = 21;
	public static final int JOB_MAX = 22;

	int mJob;
	int mSubJob;
	int mLevel;
	int mSubLevel;
	int mRace;

	public JobLevelAndRace(int race, int job, int subjob, int level, int sublevel) { this.mRace = race; this.mJob = job; this.mSubJob = subjob; this.mLevel = level; this.mSubLevel = sublevel; };
	public JobLevelAndRace(JobLevelAndRace from) {
		mJob = from.mJob;
		mSubJob = from.mSubJob;
		mLevel = from.mLevel;
		mSubLevel = from.mSubLevel;
		mRace = from.mRace;
	}
	
	public boolean equals(JobLevelAndRace other) {
		return 	mJob == other.mJob
				&& mSubJob == other.mSubJob
				&& mLevel == other.mLevel
				&& mSubLevel == other.mSubLevel
				&& mRace == other.mRace;
	}

	public int getJob() { return mJob; };
	public int getSubJob() { return mSubJob; };
	public int getLevel() { return mLevel; };
	public int getSubLevel() { return mSubLevel; };
	public int getRace() { return mRace; };
	
	public void setJob(int job) { mJob = job; };
	public void setSubJob(int subjob) { mSubJob = subjob; };
	public void setLevel(int level) { mLevel = level; };
	public void setSubLevel(int sublevel) { mSubLevel = sublevel; };
	public void setRace(int race) { mRace = race; };
}
