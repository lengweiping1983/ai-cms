package com.ai.common.enums;

public enum CpTypeEnum {
	GENERAL(1, "一般"), VIRTUAL(2, "虚拟");

	int key;
	String value;

	CpTypeEnum(int key, String value) {
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

	public static CpTypeEnum getEnumByKey(int key) {
		for (CpTypeEnum objEnum : CpTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}
}
