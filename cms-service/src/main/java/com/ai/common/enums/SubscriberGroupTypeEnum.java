package com.ai.common.enums;

public enum SubscriberGroupTypeEnum {
	GENERAL(1, "普通"), EXPERIENCE(2, "体验"), TEST(3, "测试"), INSIDE_GENERAL(11,
			"内部"), INSIDE_EXPERIENCE(12, "内部体验"), INSIDE_TEST(13, "内部测试"), ;

    int key;
    String value;

    SubscriberGroupTypeEnum(int key, String value) {
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

    public static SubscriberGroupTypeEnum getEnumByKey(int key) {
        for (SubscriberGroupTypeEnum objEnum : SubscriberGroupTypeEnum.values()) {
            if (key == objEnum.getKey()) {
                return objEnum;
            }
        }
        return null;
    }
}
