package com.ai.common.enums;

public enum AlbumJumpTypeEnum {
	DEFAULT(1, "默认"), GET(2, "GET"), POST(3, "POST"), BACK(9, "BACK");

	int key;
	String value;

	AlbumJumpTypeEnum(int key, String value) {
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

	public static AlbumJumpTypeEnum getEnumByKey(int key) {
		for (AlbumJumpTypeEnum objEnum : AlbumJumpTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}
}
