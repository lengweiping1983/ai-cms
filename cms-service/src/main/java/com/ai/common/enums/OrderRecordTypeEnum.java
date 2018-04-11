package com.ai.common.enums;

public enum OrderRecordTypeEnum {
	GENERAL(1, "普通"), EXPERIENCE(2, "体验"), TEST(3, "测试"), FREE(4, "赠送"), ;

    int key;
    String value;

    OrderRecordTypeEnum(int key, String value) {
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

    public static OrderRecordTypeEnum getEnumByKey(int key) {
        for (OrderRecordTypeEnum objEnum : OrderRecordTypeEnum.values()) {
            if (key == objEnum.getKey()) {
                return objEnum;
            }
        }
        return null;
    }
}
