package com.ai.common.enums;

public enum ChargeTypeEnum {
	FREE(0, "免费"), PPV(1, "单片"), MONTHLY(2, "包月"), YEARLY(3, "包年"), PACKAGE(4,
			"套餐"), TIME(5, "包时段");

	int key;
	String value;

	ChargeTypeEnum(int key, String value) {
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

	public static ChargeTypeEnum getEnumByKey(int key) {
		for (ChargeTypeEnum objEnum : ChargeTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

}
