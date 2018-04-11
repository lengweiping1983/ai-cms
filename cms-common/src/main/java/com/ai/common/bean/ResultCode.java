package com.ai.common.bean;

public enum ResultCode {
    SUCCESS(0, ""), ILLEGAL_ARGUMENT(-1001, "参数不正确"), PROGRAM_NOT_FOUND(-2001, "节目未找到"), SERIES_NOT_FOUND(-2002, "剧头未找到"), SYSTEM_BUSY(-9998, "系统繁忙"), SYSTEM_ERROR(
            -9999, "系统错误"), ;

    private int code;

    private String message;

    private ResultCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    public int value() {
        return code;
    }

    public String message() {
        return message;
    }

}
