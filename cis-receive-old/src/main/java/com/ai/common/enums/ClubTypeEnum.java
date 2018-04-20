package com.ai.common.enums;

public enum ClubTypeEnum {
    FOOTBALL(1, "足球");

    int key;
    String value;

    ClubTypeEnum(int key, String value) {
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

    public static ClubTypeEnum getEnumByKey(int key) {
        for (ClubTypeEnum objEnum : ClubTypeEnum.values()) {
            if (key == objEnum.getKey()) {
                return objEnum;
            }
        }
        return null;
    }
}
