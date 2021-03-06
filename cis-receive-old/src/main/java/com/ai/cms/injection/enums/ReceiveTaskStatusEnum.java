package com.ai.cms.injection.enums;

/**
 * 接收任务状态
 *
 */
public enum ReceiveTaskStatusEnum {
	DEFAULT(0, "未处理"), WAIT(1, "等待处理..."), PROCESSING(2, "正在处理..."), SUCCESS(3,
			"处理成功"), FAIL(4, "处理失败"), TIMEOUT(5, "处理超时"), ;

	int key;
	String value;

	ReceiveTaskStatusEnum(int key, String value) {
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

	public static ReceiveTaskStatusEnum getEnumByKey(int key) {
		for (ReceiveTaskStatusEnum taskStatusEnum : ReceiveTaskStatusEnum
				.values()) {
			if (key == taskStatusEnum.getKey()) {
				return taskStatusEnum;
			}
		}
		return null;
	}

}
