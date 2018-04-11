package com.ai.common.enums;

public enum TemplateParamCategoryEnum {
	MAIN(1, "固定->在页面上固定"), DYNAMIC(2, "连动->根据项不同显示不同对象");

	int key;
	String value;

	TemplateParamCategoryEnum(int key, String value) {
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

	public static TemplateParamCategoryEnum getEnumByKey(Integer key) {
		if (key == null) {
			return MAIN;
		}
		for (TemplateParamCategoryEnum objEnum : TemplateParamCategoryEnum
				.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return MAIN;
	}

}
