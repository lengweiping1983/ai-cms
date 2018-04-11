package com.ai.cms.transcode.enums;

public enum GenProgramNameRuleEnum {
	EPISODE(1, "第%s集"), COURSE(2, "第%s课"), SECTION(3, "第%s节"), SITE(4, "第%s场"), FILENAME(
			5, "使用媒体文件名称"), ;

	int key;
	String value;

	GenProgramNameRuleEnum(int key, String value) {
		this.key = key;
		this.value = value;
	}

	public static GenProgramNameRuleEnum getEnumByKey(int key) {
		for (GenProgramNameRuleEnum objEnum : GenProgramNameRuleEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
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
}
