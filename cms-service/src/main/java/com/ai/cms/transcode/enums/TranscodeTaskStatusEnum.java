package com.ai.cms.transcode.enums;

/**
 * 转码任务状态
 *
 */
public enum TranscodeTaskStatusEnum {
	WAIT(1, "等待转码..."), PROCESSING(2, "正在转码..."), SUCCESS(3, "转码成功"), FAIL(4,
			"转码失败"), TIMEOUT(5, "转码超时"), FILE_NOT_FUOUND(9, "原文件不存在");

	int key;
	String value;

	TranscodeTaskStatusEnum(int key, String value) {
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

	public static TranscodeTaskStatusEnum getEnumByKey(int key) {
		for (TranscodeTaskStatusEnum taskStatusEnum : TranscodeTaskStatusEnum
				.values()) {
			if (key == taskStatusEnum.getKey()) {
				return taskStatusEnum;
			}
		}
		return null;
	}

}
