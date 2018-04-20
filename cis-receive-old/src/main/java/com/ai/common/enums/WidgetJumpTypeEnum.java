package com.ai.common.enums;

public enum WidgetJumpTypeEnum {
	DEFAULT(1, "默认"), GET(2, "GET"), POST(3, "POST");

	int key;
	String value;

	WidgetJumpTypeEnum(int key, String value) {
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

	public static WidgetJumpTypeEnum getEnumByKey(int key) {
		for (WidgetJumpTypeEnum objEnum : WidgetJumpTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}
}
