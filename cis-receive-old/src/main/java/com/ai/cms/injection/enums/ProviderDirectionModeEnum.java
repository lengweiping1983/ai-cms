package com.ai.cms.injection.enums;

public enum ProviderDirectionModeEnum {
	SEND(1, "发送"), RECEIVE(2, "接收"), ;

	int key;
	String value;

	ProviderDirectionModeEnum(int key, String value) {
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

	public static ProviderDirectionModeEnum getEnumByKey(int key) {
		for (ProviderDirectionModeEnum objEnum : ProviderDirectionModeEnum
				.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

}
