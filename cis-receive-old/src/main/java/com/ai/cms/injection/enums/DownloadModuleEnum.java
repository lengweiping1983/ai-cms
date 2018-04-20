package com.ai.cms.injection.enums;

/**
 * 下载模块状态
 *
 */
public enum DownloadModuleEnum {
	RECEIVE(1, "接收模块"), ;

	int key;
	String value;

	DownloadModuleEnum(int key, String value) {
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

	public static DownloadModuleEnum getEnumByKey(int key) {
		for (DownloadModuleEnum taskStatusEnum : DownloadModuleEnum.values()) {
			if (key == taskStatusEnum.getKey()) {
				return taskStatusEnum;
			}
		}
		return null;
	}

}
