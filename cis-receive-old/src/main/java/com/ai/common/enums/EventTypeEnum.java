package com.ai.common.enums;

public enum EventTypeEnum {
    ALL(1, "全量"), PART(2, "增量");

    int key;
    String value;

    EventTypeEnum(int key, String value) {
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

    public static EventTypeEnum getEnumByKey(int key) {
        for (EventTypeEnum objEnum : EventTypeEnum.values()) {
            if (key == objEnum.getKey()) {
                return objEnum;
            }
        }
        return null;
    }
}
