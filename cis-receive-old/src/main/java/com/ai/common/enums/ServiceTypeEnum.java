package com.ai.common.enums;

public enum ServiceTypeEnum {
	VOD(0, "VOD"), CHANNEL(2, "Channel"), SVOD(4, "SVOD");

	int key;
	String value;

	ServiceTypeEnum(int key, String value) {
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

	public static ServiceTypeEnum getEnumByKey(int key) {
		for (ServiceTypeEnum objEnum : ServiceTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

}
