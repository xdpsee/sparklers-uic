package com.zhenhui.demo.sparklers.domain.repository;

import com.zhenhui.demo.sparklers.domain.exception.CaptchaSendException;
import com.zhenhui.demo.sparklers.domain.exception.ForbiddenException;
import com.zhenhui.demo.sparklers.domain.model.Captcha;

public interface CaptchaRepository {

    Captcha createCaptcha(String phone) throws ForbiddenException, CaptchaSendException;

    Captcha lookupCaptcha(String phone);

    void invalidCaptcha(String phone, String captcha);

}
