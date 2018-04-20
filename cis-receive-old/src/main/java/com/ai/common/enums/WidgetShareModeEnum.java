package com.ai.common.enums;

public enum WidgetShareModeEnum {
	NONE(0, "普通"), SHARED(1, "分享"), REF_OTHER(2, "引用");

    int key;
    String value;

    WidgetShareModeEnum(int key, String value) {
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

    public static WidgetShareModeEnum getEnumByKey(int key) {
        for (WidgetShareModeEnum objEnum : WidgetShareModeEnum.values()) {
            if (key == objEnum.getKey()) {
                return objEnum;
            }
        }
        return null;
    }
}
