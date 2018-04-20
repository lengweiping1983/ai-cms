package com.ai.common.enums;

public enum ItemTypeEnum {
	PROGRAM(1, "节目"), SERIES(2, "剧头"), ALBUM(3, "专题"), WIDGET(4, "推荐位"), CATEGORY(
			5, "栏目"), URI(6, "页面"), CHANNEL(7, "频道"), SCHEDULE(8, "直播节目"), STAR(
			9, "明星"), CLUB(10, "俱乐部"), LEAGUE(11, "联赛"), LEAGUE_SEASON(12, "赛季"), LEAGUE_MATCH(
			13, "赛事");

	int key;
	String value;

	ItemTypeEnum(int key, String value) {
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

	public static ItemTypeEnum getEnumByKey(int key) {
		for (ItemTypeEnum objEnum : ItemTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

}
