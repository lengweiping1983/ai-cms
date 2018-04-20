package com.ai.cms.injection.enums;

public enum InjectionStatusEnum {
	DEFAULT(0, "未分发"), INJECTION_WAIT(1, "待分发..."), INJECTION_ING(2, "分发中..."), INJECTION_SUCCESS(
			3, "已分发"), INJECTION_FAIL(4, "分发失败"), RECOVERY_WAIT(5, "待回收..."), RECOVERY_ING(
			6, "回收中..."), RECOVERY_SUCCESS(7, "已回收"), RECOVERY_FAIL(8, "回收失败");

	int key;
	String value;

	InjectionStatusEnum(int key, String value) {
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

	public static InjectionStatusEnum getEnumByKey(int key) {
		for (InjectionStatusEnum objEnum : InjectionStatusEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

}
