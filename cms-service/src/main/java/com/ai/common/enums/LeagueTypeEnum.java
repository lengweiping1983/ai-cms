package com.ai.common.enums;

public enum LeagueTypeEnum {
    LEAGUE(1, "联赛"), KNOCKOUT(2, "淘汰赛");

    int key;
    String value;

    LeagueTypeEnum(int key, String value) {
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

    public static LeagueTypeEnum getEnumByKey(int key) {
        for (LeagueTypeEnum objEnum : LeagueTypeEnum.values()) {
            if (key == objEnum.getKey()) {
                return objEnum;
            }
        }
        return null;
    }
}
