package com.ai.common.enums;

public enum AvailableStatusEnum {
    NO(0, "不可用"), YES(1, "可用");

    int key;
    String value;

    AvailableStatusEnum(int key, String value) {
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

    public static AvailableStatusEnum getEnumByKey(int key) {
        for (AvailableStatusEnum objEnum : AvailableStatusEnum.values()) {
            if (key == objEnum.getKey()) {
                return objEnum;
            }
        }
        return null;
    }
}
