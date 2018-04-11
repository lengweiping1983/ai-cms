package com.ai.common.enums;

import org.apache.commons.lang3.StringUtils;

public enum SeriesTypeEnum {
	TV(1, "电视剧"), CATEGORY(2, "栏目剧");

	int key;
	String value;

	SeriesTypeEnum(int key, String value) {
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

	public static SeriesTypeEnum getEnumByKey(int key) {
		for (SeriesTypeEnum objEnum : SeriesTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

	public static int getKeyByValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return SeriesTypeEnum.TV.getKey();
		}
		for (SeriesTypeEnum objEnum : SeriesTypeEnum.values()) {
			if (objEnum.getValue().equals(value)) {
				return objEnum.key;
			}
		}
		return SeriesTypeEnum.TV.getKey();
	}
}
