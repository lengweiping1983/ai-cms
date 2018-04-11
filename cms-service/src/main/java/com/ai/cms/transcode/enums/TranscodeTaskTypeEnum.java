package com.ai.cms.transcode.enums;

/**
 * 任务类型
 *
 */
public enum TranscodeTaskTypeEnum {
	OFFLINE_UPLOAD(1, "预处理"), TRANSCODE(2, "转码"), IMAGE(3, "抽帧"), AFTER(4,
			"后处理");

	int key;
	String value;

	TranscodeTaskTypeEnum(int key, String value) {
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

	public static TranscodeTaskTypeEnum getEnumByKey(int key) {
		for (TranscodeTaskTypeEnum taskTypeEnum : TranscodeTaskTypeEnum
				.values()) {
			if (key == taskTypeEnum.getKey()) {
				return taskTypeEnum;
			}
		}
		return null;
	}

}
