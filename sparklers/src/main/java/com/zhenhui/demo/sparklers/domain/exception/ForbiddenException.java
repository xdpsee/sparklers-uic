package com.zhenhui.demo.sparklers.domain.exception;

public class ForbiddenException extends Exception {

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
