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
