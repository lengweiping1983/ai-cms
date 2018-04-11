package com.ai.common.enums;

public enum WidgetTypeEnum {
	NAVIGATION_BAR(1, "导航条"), PICTURE_RECOMMEND(2, "图片推荐位"), VIDEO_RECOMMEND(3,
			"视频推荐位"), TXT_RECOMMEND(4, "文字推荐位"), ROLL_MESSAGE(5, "文字滚动条");

	int key;
	String value;

	WidgetTypeEnum(int key, String value) {
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

	public static WidgetTypeEnum getEnumByKey(int key) {
		for (WidgetTypeEnum objEnum : WidgetTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}
}
