package com.ai.common.enums;

public enum TrailerTypeEnum {
	DEFAULT(1, "关联"), TRAILER(2, "预告片");

	int key;
	String value;

	TrailerTypeEnum(int key, String value) {
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

	public static TrailerTypeEnum getEnumByKey(int key) {
		for (TrailerTypeEnum objEnum : TrailerTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}
}
