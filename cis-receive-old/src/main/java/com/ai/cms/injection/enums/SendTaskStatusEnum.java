package com.ai.cms.injection.enums;

/**
 * 发送任务状态
 *
 */
public enum SendTaskStatusEnum {
	DEFAULT(0, "未处理"), WAIT(1, "等待处理..."), PROCESSING(2, "工单已发送"), SUCCESS(3,
			"处理成功"), FAIL(4, "处理失败"), TIMEOUT(5, "处理超时"), PAUSE(7, "已暂停"), STOP(
			8, "已停止");

	int key;
	String value;

	SendTaskStatusEnum(int key, String value) {
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

	public static SendTaskStatusEnum getEnumByKey(int key) {
		for (SendTaskStatusEnum taskStatusEnum : SendTaskStatusEnum.values()) {
			if (key == taskStatusEnum.getKey()) {
				return taskStatusEnum;
			}
		}
		return null;
	}

}
