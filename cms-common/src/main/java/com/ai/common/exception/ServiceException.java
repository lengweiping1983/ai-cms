package com.ai.common.exception;

/**
 * Service异常.
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 6362927759978910396L;

    private int code = 999999;

    public ServiceException(int code) {
        this.code = code;
    }

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ServiceException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public ServiceException(int code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
