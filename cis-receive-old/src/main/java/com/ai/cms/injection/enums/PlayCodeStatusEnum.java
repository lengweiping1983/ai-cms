package com.ai.cms.injection.enums;

public enum PlayCodeStatusEnum {
	DEFAULT(0, "未录入"), INPUT(1, "已录入");

	int key;
	String value;

	PlayCodeStatusEnum(int key, String value) {
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

	public static PlayCodeStatusEnum getEnumByKey(int key) {
		for (PlayCodeStatusEnum objEnum : PlayCodeStatusEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

}
