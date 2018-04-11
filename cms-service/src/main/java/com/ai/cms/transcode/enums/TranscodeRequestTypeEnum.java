package com.ai.cms.transcode.enums;

public enum TranscodeRequestTypeEnum {
	MOVIE(1, "单集类型工单"), BATCH_MOVIE(3, "批量单集工单"), TV(2, "多集类型工单");

	int key;
	String value;

	TranscodeRequestTypeEnum(int key, String value) {
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

	public static TranscodeRequestTypeEnum getEnumByKey(int key) {
		for (TranscodeRequestTypeEnum objEnum : TranscodeRequestTypeEnum
				.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}
}
