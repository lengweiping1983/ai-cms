package com.ai.common.enums;

public enum ActivityTypeEnum {
	ONLINE(1, "线上"), OFFLINE(2, "线下");

	int key;
	String value;

	ActivityTypeEnum(int key, String value) {
		this.key = key;
		this.value = value;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static ActivityTypeEnum getEnumByKey(int key) {
		for (ActivityTypeEnum objEnum : ActivityTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

}
