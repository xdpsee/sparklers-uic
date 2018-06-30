package com.zhenhui.demo.sparklers.domain.exception;

public class UserAlreadyExistException extends Exception {

    public UserAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
