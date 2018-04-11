package com.ai.common.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class BaseResult {
	// 状态码
	private int code = 0;

	// 状态描述
	private String message = "";

	@JsonInclude(Include.NON_NULL)
	private List<OperationObject> operationObjectList;

	public BaseResult() {
	}

	public BaseResult(final int code) {
		this.code = code;
	}

	public BaseResult(final int code, final String message) {
		this.code = code;
		this.message = message;
	}

	public BaseResult(final HttpStatus status) {
		this(status.value(), status.getReasonPhrase());
	}

	public int getCode() {
		return code;
	}

	public BaseResult setCode(int code) {
		this.code = code;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public BaseResult setMessage(String message) {
		this.message = message;
		return this;
	}

	public List<OperationObject> getOperationObjectList() {
		return operationObjectList;
	}

	public BaseResult setOperationObjectList(
			List<OperationObject> operationObjectList) {
		this.operationObjectList = operationObjectList;
		return this;
	}

	public BaseResult addOperationObject(OperationObject operationObject) {
		if (operationObject == null) {
			return this;
		}
		if (this.operationObjectList == null) {
			this.operationObjectList = new ArrayList<OperationObject>();
		}
		if (!this.operationObjectList.contains(operationObject)) {
			this.operationObjectList.add(operationObject);
		}
		return this;
	}

	public BaseResult addOperationObject(
			List<OperationObject> operationObjectList) {
		if (operationObjectList == null || operationObjectList.size() == 0) {
			return this;
		}
		if (this.operationObjectList == null) {
			this.operationObjectList = new ArrayList<OperationObject>();
		}
		this.operationObjectList.addAll(operationObjectList);
		if (StringUtils.isEmpty(message)) {
			this.message = operationObjectList.size() + "条成功！";
		}
		return this;
	}
}
