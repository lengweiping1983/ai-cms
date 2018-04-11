package com.ai.common.enums;

import org.apache.commons.lang3.StringUtils;

public enum MediaFileTypeEnum {
    DEFAULT(1, "正片"), TRAILER(2, "片花");

    int key;
    String value;

    MediaFileTypeEnum(int key, String value) {
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

    public static MediaFileTypeEnum getEnumByKey(int key) {
        for (MediaFileTypeEnum objEnum : MediaFileTypeEnum.values()) {
            if (key == objEnum.getKey()) {
                return objEnum;
            }
        }
        return null;
    }
    
    public static int getKeyByValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return MediaFileTypeEnum.DEFAULT.getKey();
        }
        for (MediaFileTypeEnum objEnum : MediaFileTypeEnum.values()) {
            if (objEnum.getValue().equals(value)) {
                return objEnum.key;
            }
        }
        return MediaFileTypeEnum.DEFAULT.getKey();
    }
}
