package com.zhenhui.demo.sparklers.service;

import com.zhenhui.demo.sparklers.TestBase;
import com.zhenhui.demo.sparklers.domain.model.Captcha;
import com.zhenhui.demo.sparklers.domain.repository.CaptchaRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;


public class CaptchaRepositoryTests extends TestBase {

    @Autowired
    private CaptchaRepository captchaRepository;

    @Test
    public void testNormal() throws Exception {

        Captcha captcha = captchaRepository.createCaptcha("13801012020");
        assertNotNull(captcha);
        assertEquals(captcha, captchaRepository.lookupCaptcha("13801012020"));

        captchaRepository.invalidCaptcha("13801012020", captcha.getCode());
        Captcha s = captchaRepository.lookupCaptcha("13801012020");
        assertNull(s);


    }

}
