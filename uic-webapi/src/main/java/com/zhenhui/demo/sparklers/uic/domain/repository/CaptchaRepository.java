package com.zhenhui.demo.sparklers.uic.domain.repository;

import com.zhenhui.demo.sparklers.uic.domain.exception.CaptchaSendException;
import com.zhenhui.demo.sparklers.uic.domain.exception.ForbiddenException;
import com.zhenhui.demo.sparklers.uic.domain.model.Captcha;

public interface CaptchaRepository {

    Captcha createCaptcha(String phone) throws ForbiddenException, CaptchaSendException;

    Captcha lookupCaptcha(String phone);

    void invalidCaptcha(String phone, String captcha);

}
