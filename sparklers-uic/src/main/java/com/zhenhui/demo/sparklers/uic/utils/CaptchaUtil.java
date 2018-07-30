package com.zhenhui.demo.sparklers.uic.utils;

import com.zhenhui.demo.sparklers.uic.domain.exception.CaptchaExpireException;
import com.zhenhui.demo.sparklers.uic.domain.exception.CaptchaMismatchException;
import com.zhenhui.demo.sparklers.uic.domain.exception.CaptchaNotFoundException;
import com.zhenhui.demo.sparklers.uic.domain.model.Captcha;
import com.zhenhui.demo.sparklers.uic.domain.repository.CaptchaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CaptchaUtil {

    @Autowired
    private CaptchaRepository captchaRepository;

    public void verifyCaptcha(String phone, String code)
            throws CaptchaNotFoundException, CaptchaExpireException, CaptchaMismatchException {

        final Captcha captcha = captchaRepository.lookupCaptcha(phone);
        if (null == captcha) {
            throw new CaptchaNotFoundException(code);
        }

        if (captcha.getExpireAt() <= System.currentTimeMillis()) {
            throw new CaptchaExpireException(code);
        }

        if (!captcha.getCode().equals(code)) {
            throw new CaptchaMismatchException();
        }
    }

    public void clearCaptcha(String phone, String code) {
        captchaRepository.invalidCaptcha(phone, code);
    }

}
