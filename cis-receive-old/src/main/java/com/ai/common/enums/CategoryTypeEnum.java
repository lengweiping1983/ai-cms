package com.ai.common.enums;

public enum CategoryTypeEnum {
	DEFAULT(1, "普通栏目"), PARENT(2, "父栏目");

    int key;
    String value;

    CategoryTypeEnum(int key, String value) {
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

    public static CategoryTypeEnum getEnumByKey(int key) {
        for (CategoryTypeEnum objEnum : CategoryTypeEnum.values()) {
            if (key == objEnum.getKey()) {
                return objEnum;
            }
        }
        return null;
    }
}
