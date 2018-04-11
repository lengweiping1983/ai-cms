package com.ai.common.enums;

public enum AlbumItemTypeEnum {
	PROGRAM(1, "节目"), SERIES(2, "剧头"), ALBUM(3, "专题"), CATEGORY(5, "栏目"), URI(
			6, "页面"), STAR(9, "明星"), CLUB(10, "俱乐部"), LEAGUE_SEASON(12, "赛季"), LEAGUE_MATCH(
			13, "赛事");

	int key;
	String value;

	AlbumItemTypeEnum(int key, String value) {
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

	public static AlbumItemTypeEnum getEnumByKey(int key) {
		for (AlbumItemTypeEnum objEnum : AlbumItemTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

}
