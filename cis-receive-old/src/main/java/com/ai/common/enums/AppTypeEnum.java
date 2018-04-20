package com.ai.common.enums;

public enum AppTypeEnum {
	ZONE(1, "专区"), PORTAL(2, "门户"), LAUNCHER(3, "桌面"), ;

	int key;
	String value;

	AppTypeEnum(int key, String value) {
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

	public static AppTypeEnum getEnumByKey(int key) {
		for (AppTypeEnum objEnum : AppTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

}
