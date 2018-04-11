package com.ai.cms.injection.enums;

public enum PlatformTypeEnum {
	MEDIA_SYSTEM(1, "媒资系统"), BUSINESS_SYSTEM(2, "业务系统");

	int key;
	String value;

	PlatformTypeEnum(int key, String value) {
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

	public static PlatformTypeEnum getEnumByKey(int key) {
		for (PlatformTypeEnum objEnum : PlatformTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

}
