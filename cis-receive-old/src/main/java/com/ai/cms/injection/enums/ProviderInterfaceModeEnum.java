package com.ai.cms.injection.enums;

public enum ProviderInterfaceModeEnum {
	C2(1, "C2"), CIS(2, "CIS"), ;

	int key;
	String value;

	ProviderInterfaceModeEnum(int key, String value) {
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

	public static ProviderInterfaceModeEnum getEnumByKey(int key) {
		for (ProviderInterfaceModeEnum objEnum : ProviderInterfaceModeEnum
				.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

}
