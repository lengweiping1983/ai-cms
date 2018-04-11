package com.ai.cms.injection.enums;

/**
 * 下载任务状态
 *
 */
public enum DownloadTaskStatusEnum {
	DEFAULT(0, "未下载"), WAIT(1, "等待下载..."), DOWNLOADING(2, "正在下载..."), SUCCESS(
			3, "下载成功"), FAIL(4, "下载失败"), TIMEOUT(5, "下载超时"), REDOWNLOAD(6,
			"等待重新下载..."), PAUSE(7, "已暂停"), STOP(8, "已停止"), MD5_ERROR(9,
			"MD5校验错误"), ;

	int key;
	String value;

	DownloadTaskStatusEnum(int key, String value) {
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

	public static DownloadTaskStatusEnum getEnumByKey(int key) {
		for (DownloadTaskStatusEnum taskStatusEnum : DownloadTaskStatusEnum
				.values()) {
			if (key == taskStatusEnum.getKey()) {
				return taskStatusEnum;
			}
		}
		return null;
	}

}
