package com.ai.common.enums;

public enum ValidStatusEnum {
    INVALID(0, "失效"), VALID(1, "生效");

    int key;
    String value;

    ValidStatusEnum(int key, String value) {
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

    public static ValidStatusEnum getEnumByKey(int key) {
        for (ValidStatusEnum objEnum : ValidStatusEnum.values()) {
            if (key == objEnum.getKey()) {
                return objEnum;
            }
        }
        return null;
    }
}
