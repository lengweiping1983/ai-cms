package com.ai.common.enums;

import java.util.ArrayList;
import java.util.List;

public enum MediaStatusEnum {
	OK(0, "正常"), ONLY_MISS_IMAGE(-10, "仅缺海报"), MISS_IMAGE(10, "缺海报"), MISS_VIDEO(
			100, "缺视频"), TRANSCODE(1000, "正在转码..."), TRANSCODE_FAIL(10000,
			"转码失败"), TRANSCODE_PART_FAIL(100000, "部分转码失败"), DOWNLOAD(1000000,
			"正在下载..."), DOWNLOAD_FAIL(10000000, "下载失败"), DOWNLOAD_PART_FAIL(
			100000000, "部分下载失败");

	int key;
	String value;

	MediaStatusEnum(int key, String value) {
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

	public static MediaStatusEnum getEnumByKey(int key) {
		for (MediaStatusEnum objEnum : MediaStatusEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

	public static List<MediaStatusEnum> toMediaFileList() {
		List<MediaStatusEnum> enumList = new ArrayList<MediaStatusEnum>();
		enumList.add(MediaStatusEnum.OK);
		enumList.add(MediaStatusEnum.MISS_VIDEO);
		enumList.add(MediaStatusEnum.TRANSCODE);
		enumList.add(MediaStatusEnum.TRANSCODE_FAIL);
		enumList.add(MediaStatusEnum.DOWNLOAD);
		enumList.add(MediaStatusEnum.DOWNLOAD_FAIL);
		return enumList;
	}

}
