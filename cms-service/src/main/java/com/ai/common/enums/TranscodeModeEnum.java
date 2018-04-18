package com.ai.common.enums;

public enum TranscodeModeEnum {
	LOCAL(1, "本地转码"), ONLINE(2, "在线转码");

	int key;
	String value;

	TranscodeModeEnum(int key, String value) {
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

	public static TranscodeModeEnum getEnumByKey(int key) {
		for (TranscodeModeEnum objEnum : TranscodeModeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}
}
