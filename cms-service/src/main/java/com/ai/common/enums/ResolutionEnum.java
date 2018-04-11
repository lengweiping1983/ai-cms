package com.ai.common.enums;

import org.apache.commons.lang3.StringUtils;

public enum ResolutionEnum {
    UNKNOWN(0, "未知"), R576I(1, "576i"), R720P(2, "720P"), R1080P(3, "1080P"), R2K(4, "2K"), R4K(5, "4K");

    int key;
    String value;

    ResolutionEnum(int key, String value) {
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

    public static ResolutionEnum getEnumByKey(int key) {
        for (ResolutionEnum objEnum : ResolutionEnum.values()) {
            if (key == objEnum.getKey()) {
                return objEnum;
            }
        }
        return null;
    }

    public static ResolutionEnum getEnumByValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        for (ResolutionEnum objEnum : ResolutionEnum.values()) {
            if (objEnum.getValue().equalsIgnoreCase(value)) {
                return objEnum;
            }
        }
        return null;
    }
}
