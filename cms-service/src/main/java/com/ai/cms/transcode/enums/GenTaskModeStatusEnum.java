package com.ai.cms.transcode.enums;

public enum GenTaskModeStatusEnum {
	DEFAULT(1, "正常转码"), LOCAL(2, "已转好码[仅生成媒资元数据]");

	int key;
	String value;

	GenTaskModeStatusEnum(int key, String value) {
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

	public static GenTaskModeStatusEnum getEnumByKey(int key) {
		for (GenTaskModeStatusEnum objEnum : GenTaskModeStatusEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}
}
