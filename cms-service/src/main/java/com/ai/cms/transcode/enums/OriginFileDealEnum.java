package com.ai.cms.transcode.enums;

public enum OriginFileDealEnum {
	NO_DEAL(0, "不处理"), DELETE(1, "删除"), ARCHIVE(2, "归档");

	int key;
	String value;

	OriginFileDealEnum(int key, String value) {
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

	public static OriginFileDealEnum getEnumByKey(int key) {
		for (OriginFileDealEnum objEnum : OriginFileDealEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}
}
