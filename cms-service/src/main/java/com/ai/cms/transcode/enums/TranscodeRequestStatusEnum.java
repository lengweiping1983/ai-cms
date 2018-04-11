package com.ai.cms.transcode.enums;

public enum TranscodeRequestStatusEnum {
	EDIT(1, "编辑"), RUNNING(2, "正在执行..."), SUCCESS(3, "执行成功"), FAIL(4, "执行失败"), PART_FAIL(
			5, "部分执行失败");

	int key;
	String value;

	TranscodeRequestStatusEnum(int key, String value) {
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

	public static TranscodeRequestStatusEnum getEnumByKey(int key) {
		for (TranscodeRequestStatusEnum objEnum : TranscodeRequestStatusEnum
				.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}
}
