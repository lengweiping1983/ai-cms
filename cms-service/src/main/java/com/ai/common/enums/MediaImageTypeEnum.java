package com.ai.common.enums;

import org.apache.commons.lang3.StringUtils;

public enum MediaImageTypeEnum {
	STILLS(2, "剧照");

	int key;
	String value;

	MediaImageTypeEnum(int key, String value) {
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

	public static MediaImageTypeEnum getEnumByKey(int key) {
		for (MediaImageTypeEnum objEnum : MediaImageTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

	public static int getKeyByValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return MediaImageTypeEnum.STILLS.getKey();
		}
		for (MediaImageTypeEnum objEnum : MediaImageTypeEnum.values()) {
			if (objEnum.getValue().equals(value)) {
				return objEnum.key;
			}
		}
		return MediaImageTypeEnum.STILLS.getKey();
	}
}
