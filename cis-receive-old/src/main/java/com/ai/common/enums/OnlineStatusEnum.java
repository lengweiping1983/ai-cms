package com.ai.common.enums;

public enum OnlineStatusEnum {
    DEFAULT(0, "未上线"), ONLINE(1, "已上线"), OFFLINE(2, "已下线");

    int key;
    String value;

    OnlineStatusEnum(int key, String value) {
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

    public static OnlineStatusEnum getEnumByKey(int key) {
        for (OnlineStatusEnum objEnum : OnlineStatusEnum.values()) {
            if (key == objEnum.getKey()) {
                return objEnum;
            }
        }
        return null;
    }

}
