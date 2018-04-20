package com.ai.common.enums;

public enum TemplateParamTypeEnum {
	WIDGET(4, "推荐位"), CATEGORY(5, "栏目"), STAR(11, "明星");

    int key;
    String value;

    TemplateParamTypeEnum(int key, String value) {
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

    public static TemplateParamTypeEnum getEnumByKey(int key) {
        for (TemplateParamTypeEnum objEnum : TemplateParamTypeEnum.values()) {
            if (key == objEnum.getKey()) {
                return objEnum;
            }
        }
        return null;
    }

}
