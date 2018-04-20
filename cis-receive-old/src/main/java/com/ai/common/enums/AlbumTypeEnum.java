package com.ai.common.enums;

public enum AlbumTypeEnum {
	DEFAULT(1, "普通"), CUSTOM(2, "自定义"), SENIOR(3, "自定义并带Trailer");

	int key;
	String value;

	AlbumTypeEnum(int key, String value) {
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

	public static AlbumTypeEnum getEnumByKey(int key) {
		for (AlbumTypeEnum objEnum : AlbumTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

}
