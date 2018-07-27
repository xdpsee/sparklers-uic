package com.zhenhui.demo.sparklers.uic.domain.exception;

public class UserAlreadyExistException extends Exception {

    public UserAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
