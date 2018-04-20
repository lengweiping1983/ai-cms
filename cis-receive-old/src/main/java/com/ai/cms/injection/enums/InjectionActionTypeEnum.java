package com.ai.cms.injection.enums;

public enum InjectionActionTypeEnum {
	CREATE(1, "REGIST", "创建"), UPDATE(2, "UPDATE", "修改"), DELETE(3, "DELETE",
			"删除");

	int key;
	String value;
	String name;

	InjectionActionTypeEnum(int key, String value, String name) {
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

	public static InjectionActionTypeEnum getEnumByKey(int key) {
		for (InjectionActionTypeEnum objEnum : InjectionActionTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

	public static InjectionActionTypeEnum getEnumByValue(String value) {
		for (InjectionActionTypeEnum objEnum : InjectionActionTypeEnum.values()) {
			if (objEnum.getValue().equalsIgnoreCase(value)) {
				return objEnum;
			}
		}
		return null;
	}
}
