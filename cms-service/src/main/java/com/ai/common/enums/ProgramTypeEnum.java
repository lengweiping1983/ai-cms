package com.ai.common.enums;

import org.apache.commons.lang3.StringUtils;

public enum ProgramTypeEnum {
	MOVIE(1, "单集类型"), TV(2, "多集类型");

	int key;
	String value;

	ProgramTypeEnum(int key, String value) {
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

	public static ProgramTypeEnum getEnumByKey(int key) {
		for (ProgramTypeEnum objEnum : ProgramTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

	public static int getKeyByValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return ProgramTypeEnum.MOVIE.getKey();
		}
		for (ProgramTypeEnum objEnum : ProgramTypeEnum.values()) {
			if (objEnum.getValue().equals(value)) {
				return objEnum.key;
			}
		}
		return ProgramTypeEnum.MOVIE.getKey();
	}
}
