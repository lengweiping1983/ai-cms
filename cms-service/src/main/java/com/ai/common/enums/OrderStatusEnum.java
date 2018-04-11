package com.ai.common.enums;

public enum OrderStatusEnum {
	NO_ORDER(0, "不可订购"), ORDER(1, "立即订购"), ORDERED(2, "已订购"), UNSUBSCRIBE(3, "退订"), NO_UNSUBSCRIBE(4, "不可退订");
	int key;
	String value;

	OrderStatusEnum(int key, String value) {
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

	public static OrderStatusEnum getEnumByKey(int key) {
		for (OrderStatusEnum objEnum : OrderStatusEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

}
