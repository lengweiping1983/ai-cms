package com.ai.injection.bean;

import java.io.Serializable;

public class WriteBackResponseBean implements Serializable {
	private static final long serialVersionUID = 1L;

	// 状态码
	private String code = "1";

	// 状态描述
	private String message = "";

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
