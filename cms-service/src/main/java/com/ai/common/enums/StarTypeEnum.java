package com.ai.common.enums;

public enum StarTypeEnum {
    MOVIE(1, "明星"), SPORT(2, "球星"), UFC(3, "拳王");

    int key;
    String value;

    StarTypeEnum(int key, String value) {
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

    public static StarTypeEnum getEnumByKey(int key) {
        for (StarTypeEnum objEnum : StarTypeEnum.values()) {
            if (key == objEnum.getKey()) {
                return objEnum;
            }
        }
        return null;
    }
}
