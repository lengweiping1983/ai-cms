package com.ai.common.enums;

public enum CategoryItemTypeEnum {
	PROGRAM(1, "节目"), SERIES(2, "剧头"), ALBUM(3, "专题"), CHANNEL(7, "频道"), STAR(
			9, "明星"), CLUB(10, "俱乐部"), LEAGUE_SEASON(12, "赛季"), LEAGUE_MATCH(
			13, "赛事");

	int key;
	String value;

	CategoryItemTypeEnum(int key, String value) {
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

	public static CategoryItemTypeEnum getEnumByKey(int key) {
		for (CategoryItemTypeEnum objEnum : CategoryItemTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}
}
