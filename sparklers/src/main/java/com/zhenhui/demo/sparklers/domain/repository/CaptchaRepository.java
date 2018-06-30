package com.zhenhui.demo.sparklers.domain.repository;

public interface CaptchaRepository {

    String createCaptcha(String phone, boolean create);

    String lookupCaptcha(String phone);

    void invalidCaptcha(String phone);

    void removeAll();

}
