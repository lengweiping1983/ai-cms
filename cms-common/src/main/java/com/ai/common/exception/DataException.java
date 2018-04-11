package com.ai.common.exception;

public class DataException extends RuntimeException {
	private static final long serialVersionUID = 6362927759978910396L;

	private String message;

	public DataException(String message) {
		this.message = message;
	}

	public DataException(String message, Throwable cause) {
		super(message, cause);
	}

	public String getMessage() {
		return message;
	}

}
