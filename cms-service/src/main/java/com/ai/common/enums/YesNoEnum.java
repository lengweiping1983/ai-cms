package com.ai.common.enums;

import org.apache.commons.lang3.StringUtils;

public enum YesNoEnum {
    NO(0, "否"), YES(1, "是");

    int key;
    String value;

    YesNoEnum(int key, String value) {
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

    public static YesNoEnum getEnumByKey(int key) {
        for (YesNoEnum objEnum : YesNoEnum.values()) {
            if (key == objEnum.getKey()) {
                return objEnum;
            }
        }
        return null;
    }
    
    public static int getKeyByValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return YesNoEnum.NO.getKey();
        }
        for (YesNoEnum objEnum : YesNoEnum.values()) {
            if (objEnum.getValue().equals(value)) {
                return objEnum.key;
            }
        }
        return YesNoEnum.NO.getKey();
    }
}
