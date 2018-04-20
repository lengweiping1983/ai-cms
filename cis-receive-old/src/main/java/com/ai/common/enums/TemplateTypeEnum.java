package com.ai.common.enums;

public enum TemplateTypeEnum {
	DETAIL_PROGRAM(1, "单集详页"), DETAIL_SERIES(2, "多集详页"), ALBUM(3, "专题页面"), LIST(
			10, "列表页面"), ZONE(20, "专区页面"), ACTIVITY(30, "活动页面"), FUNCTION(40,
			"功能页面"), ERROR(70, "错误页面"), OTHER(80, "其他页面"), HOME(90, "首页");

	int key;
	String value;

	TemplateTypeEnum(int key, String value) {
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

	public static TemplateTypeEnum getEnumByKey(int key) {
		for (TemplateTypeEnum objEnum : TemplateTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}
}
