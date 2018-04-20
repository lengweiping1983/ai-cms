package com.ai.cms.injection.enums;

public enum InjectionObjectTypeEnum {
	OBJECT(1, "Object", "操作对象"), MAPPING(2, "Mapping", "映射对象");

	int key;
	String value;
	String name;

	InjectionObjectTypeEnum(int key, String value, String name) {
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

	public static InjectionObjectTypeEnum getEnumByKey(int key) {
		for (InjectionObjectTypeEnum objEnum : InjectionObjectTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

}
