package com.ai.common.enums;

public enum ChargeTimeUnitEnum {
	HOUR(1, "小时"), DAY(2, "天"), MONTH(3, "月"), YEAR(4, "年");

	int key;
	String value;

	ChargeTimeUnitEnum(int key, String value) {
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

	public static ChargeTimeUnitEnum getEnumByKey(int key) {
		for (ChargeTimeUnitEnum objEnum : ChargeTimeUnitEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

}
