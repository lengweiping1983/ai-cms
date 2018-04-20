package com.ai.cms.injection.enums;

public enum ProviderTypeEnum {
	DEFAULT(0, "默认"), HUAWEI(1, "华为"), ZTE(2, "中兴"), UT(3, "UT"), FIBERHOME(4,
			"烽火"), YSTEN(5, "易视腾"), ICNTV(6, "未来电视");

	int key;
	String value;

	ProviderTypeEnum(int key, String value) {
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

	public static ProviderTypeEnum getEnumByKey(int key) {
		for (ProviderTypeEnum objEnum : ProviderTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

}
