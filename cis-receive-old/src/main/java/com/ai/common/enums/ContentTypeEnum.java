package com.ai.common.enums;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public enum ContentTypeEnum {
    MOVIE(1, "电影"), TV(2, "电视剧"), VARIETY(3, "综艺"), SPORT(4, "体育"), CHILDREN(5, "少儿"), JISHI(6, "纪实"), LIFE(7, "生活"), EDUCATION(8, "教育"), MUSIC(9, "音乐"), NEWS(
            10, "新闻"), FINANCE(11, "财经"), LAW(12, "法治"), GAMING(13, "电竞"), ORIGINAL(14, "原创"), SMOVIE(15, "微电影"), SQUARE_DANCE(16, "广场舞"), DRAMA(17, "戏曲"),
	EURAMERICAN_DRAMA(18, "欧美剧"),
	MILITARY(19, "军事"),
	EAGLE_EYE(20, "鹰眼"),
	HEALTHY(21, "健康"),
    OTHER(99, "其它");

	// 新华社需要如下
	// NEWS(10, "新闻"),
	// FINANCE(11, "财经"),
	// MILITARY(19, "军事"),
	// HEALTHY(21, "健康"),
	// EAGLE_EYE(20, "鹰眼"),
	// JISHI(6, "纪实"),
	// MUSIC(9, "音频");
	
    int key;
    String value;

    ContentTypeEnum(int key, String value) {
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
    
	public static ContentTypeEnum getEnumByKey(int key) {
        for (ContentTypeEnum objEnum : ContentTypeEnum.values()) {
            if (key == objEnum.getKey()) {
                return objEnum;
            }
        }
        return null;
    }

    public static int getKeyByValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return ContentTypeEnum.OTHER.getKey();
        }
        for (ContentTypeEnum objEnum : ContentTypeEnum.values()) {
            if (objEnum.getValue().equals(value)) {
                return objEnum.key;
            }
        }
        return ContentTypeEnum.OTHER.getKey();
    }
    
	public static List<ContentTypeEnum> toList() {
		List<ContentTypeEnum> enumList = new ArrayList<ContentTypeEnum>();
		
		for (ContentTypeEnum e : ContentTypeEnum.values()) {
			enumList.add(e);
		}
		return enumList;
	}

}
