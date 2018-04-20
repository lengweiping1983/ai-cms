package com.ai.common.enums;

public enum TemplateParamModeEnum {
	REQUIRED(1, "必选"), OPTIONAL(9, "可选");

	int key;
	String value;

	TemplateParamModeEnum(int key, String value) {
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

	public static TemplateParamModeEnum getEnumByKey(Integer key) {
		if (key == null) {
			return REQUIRED;
		}
		for (TemplateParamModeEnum objEnum : TemplateParamModeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return REQUIRED;
	}

}
