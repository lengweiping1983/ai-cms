package com.ai.cms.injection.enums;

/**
 * 接收任务响应状态
 *
 */
public enum ReceiveResponseStatusEnum {
	DEFAULT(0, "未发送"), SUCCESS(3, "发送成功"), FAIL(4, "发送失败");

	int key;
	String value;

	ReceiveResponseStatusEnum(int key, String value) {
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

	public static ReceiveResponseStatusEnum getEnumByKey(int key) {
		for (ReceiveResponseStatusEnum taskStatusEnum : ReceiveResponseStatusEnum
				.values()) {
			if (key == taskStatusEnum.getKey()) {
				return taskStatusEnum;
			}
		}
		return null;
	}

}
