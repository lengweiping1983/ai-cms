package com.ai.common.enums;

public enum PageTypeEnum {
	DETAIL(10, "详情页面"), PLAY(20, "播放页面"), SEARCH(30, "搜索页面"), TAG(40, "标签页面"), STAR(
			50, "明星页面"), ORDER(80, "订购页面"), ORDER_RESULT(81, "订购结果页面"), ORDER_UNSUBSCRIBE(
			82, "退订页面"), ORDER_UNSUBSCRIBE_RESULT(83, "退订结果页面"), ERROR(90,
			"错误页面");

	int key;
	String value;

	PageTypeEnum(int key, String value) {
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

	public static PageTypeEnum getEnumByKey(int key) {
		for (PageTypeEnum objEnum : PageTypeEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

}
