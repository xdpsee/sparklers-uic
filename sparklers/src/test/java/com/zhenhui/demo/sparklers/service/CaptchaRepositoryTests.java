package com.zhenhui.demo.sparklers.service;

import com.zhenhui.demo.sparklers.Application;
import com.zhenhui.demo.sparklers.data.repository.CaptchaRepositoryImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@Transactional(transactionManager = "transactionManager")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class CaptchaRepositoryTests {

    @Autowired
    private CaptchaRepositoryImpl captchaRepository;

    @Test
    public void testNormal() {

        String captcha = captchaRepository.createCaptcha("13801012020", true);
        assertNotNull(captcha);

        String s = captchaRepository.lookupCaptcha("13402022080");
        assertNull(s);

        assertEquals(captcha, captchaRepository.lookupCaptcha("13801012020"));

        captchaRepository.invalidCaptcha("13801012020");
        s = captchaRepository.lookupCaptcha("13801012020");
        assertNull(s);


    }

}
