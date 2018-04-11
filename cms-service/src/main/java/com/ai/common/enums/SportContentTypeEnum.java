package com.ai.common.enums;

import org.apache.commons.lang3.StringUtils;

public enum SportContentTypeEnum {
    BALL(1, "世界波"), UFC(2, "UFC"),

    OTHER(99, "其它");

    int key;
    String value;

    SportContentTypeEnum(int key, String value) {
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

    public static SportContentTypeEnum getEnumByKey(int key) {
        for (SportContentTypeEnum objEnum : SportContentTypeEnum.values()) {
            if (key == objEnum.getKey()) {
                return objEnum;
            }
        }
        return null;
    }

    public static int getKeyByValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return SportContentTypeEnum.OTHER.getKey();
        }
        for (SportContentTypeEnum objEnum : SportContentTypeEnum.values()) {
            if (objEnum.getValue().equals(value)) {
                return objEnum.key;
            }
        }
        return SportContentTypeEnum.OTHER.getKey();
    }

}
