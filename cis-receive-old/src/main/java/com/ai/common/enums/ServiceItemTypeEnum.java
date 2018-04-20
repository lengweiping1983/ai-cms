package com.ai.common.enums;

public enum ServiceItemTypeEnum {
	 PROGRAM(1, "节目"), SERIES(2, "剧头");

	int key;
	String value;

	ServiceItemTypeEnum(int key, String value) {
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

	public static ServiceItemTypeEnum getEnumByKey(int key) {
		for (ServiceItemTypeEnum objEnum : ServiceItemTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

}
