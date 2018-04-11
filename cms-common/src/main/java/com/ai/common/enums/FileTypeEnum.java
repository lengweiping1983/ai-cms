package com.ai.common.enums;

public enum FileTypeEnum {
    DIR(1, "目录"), FILE(2, "文件");

    int key;
    String value;

    FileTypeEnum(int key, String value) {
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

    public static FileTypeEnum getEnumByKey(int key) {
        for (FileTypeEnum objEnum : FileTypeEnum.values()) {
            if (key == objEnum.getKey()) {
                return objEnum;
            }
        }
        return null;
    }
}
