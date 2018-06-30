package com.zhenhui.demo.sparklers.service;

import com.zhenhui.demo.sparklers.Application;
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
public class CaptchaManagerTests {

    @Autowired
    private CaptchaManager captchaManager;

    @Test
    public void testNormal() {

        String captcha = captchaManager.createCaptcha("13801012020", true);
        assertNotNull(captcha);

        String s = captchaManager.lookupCaptcha("13402022080");
        assertNull(s);

        assertEquals(captcha, captchaManager.lookupCaptcha("13801012020"));

    }

}
