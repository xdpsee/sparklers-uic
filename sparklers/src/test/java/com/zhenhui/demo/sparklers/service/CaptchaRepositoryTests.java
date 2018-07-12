package com.zhenhui.demo.sparklers.service;

import com.zhenhui.demo.sparklers.TestBase;
import com.zhenhui.demo.sparklers.domain.repository.CaptchaRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;


public class CaptchaRepositoryTests extends TestBase {

    @Autowired
    private CaptchaRepository captchaRepository;

    @Test
    public void testNormal() {

        String captcha = captchaRepository.createCaptcha("13801012020", true);
        assertNotNull(captcha);
        assertEquals(captcha, captchaRepository.lookupCaptcha("13801012020"));

        captchaRepository.invalidCaptcha("13801012020");
        String s = captchaRepository.lookupCaptcha("13801012020");
        assertNull(s);


    }

}
