package com.ai.common.enums;

public enum MediaSyncEnum {
    CLOSE(0, "关闭"), METADATA(1, "同步元数据"), POSTER(10, "同步海报"), STAGE(100, "同步剧照"), AUDIT(1000, "同步审核数据");

    int key;
    String value;

    MediaSyncEnum(int key, String value) {
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

    public static MediaSyncEnum getEnumByKey(int key) {
        for (MediaSyncEnum objEnum : MediaSyncEnum.values()) {
            if (key == objEnum.getKey()) {
                return objEnum;
            }
        }
        return null;
    }

}
