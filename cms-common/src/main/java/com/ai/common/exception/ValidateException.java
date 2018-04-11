package com.ai.common.exception;

/**
 * Validate异常.
 */
public class ValidateException extends RuntimeException {
    private static final long serialVersionUID = 6362927759978910396L;

    protected int result;

    protected String description;

    public ValidateException(int result, String description) {
        this.result = result;
        this.description = description;
    }

    public int getResult() {
        return result;
    }

    public String getMessage() {
        return description;
    }

}
