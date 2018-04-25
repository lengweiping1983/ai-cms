package com.ai.sys.enums;

import org.apache.commons.lang3.StringUtils;

public enum UserTypeEnum {
	SYSTEM(1, "系统用户"), CP(2, "提供商用户");

	int key;
	String value;

	UserTypeEnum(int key, String value) {
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

	public static UserTypeEnum getEnumByKey(int key) {
		for (UserTypeEnum objEnum : UserTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

	public static int getKeyByValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return UserTypeEnum.SYSTEM.getKey();
		}
		for (UserTypeEnum objEnum : UserTypeEnum.values()) {
			if (objEnum.getValue().equals(value)) {
				return objEnum.key;
			}
		}
		return UserTypeEnum.SYSTEM.getKey();
	}
}
