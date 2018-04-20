package com.ai.cms.injection.enums;

public enum InjectionItemTypeEnum {
	PROGRAM(1, "Program", "节目"), MOVIE(2, "Movie", "媒体内容"), CHANNEL(5,
			"Channel", "频道"), SCHEDULE(7, "Schedule", "时间表"), PICTURE(8,
			"Picture", "图片"), CATEGORY(9, "Category", "分类"), SERIES(10,
			"Series", "连续剧"), PACKAGE(1002, "Package", "服务");

	int key;
	String value;
	String name;

	InjectionItemTypeEnum(int key, String value, String name) {
		this.key = key;
		this.value = value;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static InjectionItemTypeEnum getEnumByKey(int key) {
		for (InjectionItemTypeEnum objEnum : InjectionItemTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

	public static InjectionItemTypeEnum getEnumByValue(String value) {
		for (InjectionItemTypeEnum objEnum : InjectionItemTypeEnum.values()) {
			if (objEnum.getValue().equalsIgnoreCase(value)) {
				return objEnum;
			}
		}
		return null;
	}
}
