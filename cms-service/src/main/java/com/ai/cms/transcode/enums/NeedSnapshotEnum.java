package com.ai.cms.transcode.enums;

public enum NeedSnapshotEnum {
	NO(0, "不需要"), YES(1, "需要");

	int key;
	String value;

	NeedSnapshotEnum(int key, String value) {
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

	public static NeedSnapshotEnum getEnumByKey(int key) {
		for (NeedSnapshotEnum objEnum : NeedSnapshotEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}
}
