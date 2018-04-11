package com.ai.cms.injection.enums;

import org.apache.commons.lang3.StringUtils;

public enum InjectionDirectionEnum {
	SEND(0, "发送"), RECEIVE(1, "接收");

	int key;
	String value;

	InjectionDirectionEnum(int key, String value) {
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

	public static InjectionDirectionEnum getEnumByKey(int key) {
		for (InjectionDirectionEnum objEnum : InjectionDirectionEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

	public static int getKeyByValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return InjectionDirectionEnum.SEND.getKey();
		}
		for (InjectionDirectionEnum objEnum : InjectionDirectionEnum.values()) {
			if (objEnum.getValue().equals(value)) {
				return objEnum.key;
			}
		}
		return InjectionDirectionEnum.SEND.getKey();
	}
}
