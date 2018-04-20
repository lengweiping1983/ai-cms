package com.ai.common.enums;

public enum ChannelTypeEnum {
    LIVE(1, "直播频道"), VIRTUAL(2, "虚拟频道"), CAROUSEL(3, "轮播频道");

    int key;
    String value;

    ChannelTypeEnum(int key, String value) {
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

    public static ChannelTypeEnum getEnumByKey(int key) {
        for (ChannelTypeEnum objEnum : ChannelTypeEnum.values()) {
            if (key == objEnum.getKey()) {
                return objEnum;
            }
        }
        return null;
    }
}
