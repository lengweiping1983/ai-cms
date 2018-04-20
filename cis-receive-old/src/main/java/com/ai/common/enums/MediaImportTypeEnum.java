package com.ai.common.enums;

public enum MediaImportTypeEnum {
	SERIES_META_DATA(1, "剧头元数据"), PROGRAM_META_DATA(2, "节目元数据"), MEDIA_FILE_META_DATA(
			3, "媒体内容元数据");

	int key;
	String value;

	MediaImportTypeEnum(int key, String value) {
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

	public static MediaImportTypeEnum getEnumByKey(int key) {
		for (MediaImportTypeEnum objEnum : MediaImportTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}
}
