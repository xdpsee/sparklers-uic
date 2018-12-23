package com.zhenhui.demo.sparklers.uic.domain.exception;

public class CaptchaExpireException extends Exception {

    public CaptchaExpireException() {
    }

    public CaptchaExpireException(String message) {
        super(message);
    }
}
