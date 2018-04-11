package com.ai.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Rest异常.
 * 
 */
public class RestException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    public RestException() {
    }

    public RestException(String message) {
        super(message);
    }

    public RestException(HttpStatus status) {
        this.status = status;
    }

    public RestException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public RestException(HttpStatus status, Throwable cause) {
        super(cause);
        this.status = status;
    }

    public RestException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
