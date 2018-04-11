package com.ai.common.enums;

public enum SourceEnum {
	MANUAL(0, "手工录入"), BATCH_IMPORT(1, "批量导入"), COLLECTION(2, "自动采集"), TRANSCODE(
			3, "转码"), SYNC_CMS(4, "中央CMS"), INJECTION(5, "分发");

	int key;
	String value;

	SourceEnum(int key, String value) {
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

	public static SourceEnum getEnumByKey(int key) {
		for (SourceEnum objEnum : SourceEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}
}
