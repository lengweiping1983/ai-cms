package com.ai.common.enums;

public enum LeagueMatchTypeEnum {
	TEAM(1, "团队"), PERSONAL(2, "单人"), GROUP(3, "群体");

	int key;
	String value;

	LeagueMatchTypeEnum(int key, String value) {
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

	public static LeagueMatchTypeEnum getEnumByKey(int key) {
		for (LeagueMatchTypeEnum objEnum : LeagueMatchTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

}
