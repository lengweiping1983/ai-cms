package com.ai.common.enums;

public enum BoardTypeEnum {
    SHOOTER(1, "射手榜"), ASSIST(2, "助攻榜");

    int key;
    String value;

    BoardTypeEnum(int key, String value) {
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

    public static BoardTypeEnum getEnumByKey(int key) {
        for (BoardTypeEnum objEnum : BoardTypeEnum.values()) {
            if (key == objEnum.getKey()) {
                return objEnum;
            }
        }
        return null;
    }

}
