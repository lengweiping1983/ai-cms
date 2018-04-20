package com.ai.common.enums;

public enum WidgetJumpItemTypeEnum {
	DEFAULT(0, "默认"), PROGRAM(1, "节目"), SERIES(2, "剧头"), ALBUM(3, "专题"), URI(6,
			"页面"), JUMP(99, "链接");

	int key;
	String value;

	WidgetJumpItemTypeEnum(int key, String value) {
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

	public static WidgetJumpItemTypeEnum getEnumByKey(int key) {
		for (WidgetJumpItemTypeEnum objEnum : WidgetJumpItemTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}
}
