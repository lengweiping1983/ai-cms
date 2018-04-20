package com.ai.common.enums;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public enum AuditStatusEnum {
	EDIT(0, "编辑"), AUDIT_WAIT(1, "待审核"), AUDIT_FIRST_PASS(2, "审核通过"), AUDIT_NOT_PASS(
			9, "审核不通过");

	int key;
	String value;

	AuditStatusEnum(int key, String value) {
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

	public static AuditStatusEnum getEnumByKey(int key) {
		for (AuditStatusEnum objEnum : AuditStatusEnum.values()) {
			if (key == objEnum.getKey()) {
				return objEnum;
			}
		}
		return null;
	}

	public static int getKeyByValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return AuditStatusEnum.EDIT.getKey();
		}
		for (AuditStatusEnum objEnum : AuditStatusEnum.values()) {
			if (objEnum.getValue().equals(value)) {
				return objEnum.key;
			}
		}
		return AuditStatusEnum.EDIT.getKey();
	}

	public static List<AuditStatusEnum> toList() {
		List<AuditStatusEnum> enumList = new ArrayList<AuditStatusEnum>();
		enumList.add(AuditStatusEnum.EDIT);
		enumList.add(AuditStatusEnum.AUDIT_WAIT);
		enumList.add(AuditStatusEnum.AUDIT_NOT_PASS);
		return enumList;
	}
}
