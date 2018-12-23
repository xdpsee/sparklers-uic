package com.zhenhui.demo.sparklers.uic.data.repository;

import com.zhenhui.demo.sparklers.uic.data.cache.CaptchaCache;
import com.zhenhui.demo.sparklers.uic.data.tools.CaptchaProtector;
import com.zhenhui.demo.sparklers.uic.data.tools.CaptchaSender;
import com.zhenhui.demo.sparklers.uic.domain.exception.CaptchaSendException;
import com.zhenhui.demo.sparklers.uic.domain.exception.ForbiddenException;
import com.zhenhui.demo.sparklers.uic.domain.model.Captcha;
import com.zhenhui.demo.sparklers.uic.domain.repository.CaptchaRepository;
import com.zhenhui.library.redis.cache.AbstractZSet;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;


@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
public class CaptchaRepositoryImpl implements CaptchaRepository {

    @Autowired
    private CaptchaCache captchaCache;

    @Value("${captcha.expire.minutes}")
    private Integer expireAfterMinutes;

    @Autowired
    private CaptchaProtector captchaProtector;

    @Autowired
    private CaptchaSender captchaSender;

    private final static RandomStringGenerator generator = new RandomStringGenerator.Builder()
            .withinRange('0', '9').build();

    @Override
    public Captcha createCaptcha(String phone) throws ForbiddenException, CaptchaSendException {

        if (!captchaProtector.acceptable(phone)) {
            throw new ForbiddenException("禁止操作,稍后重试");
        }

        long expireAt = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(expireAfterMinutes);

        String captcha = generator.generate(4);

        if (captchaSender.send(phone, captcha)) {
            captchaCache.add(phone, captcha, expireAt);
            captchaProtector.pass(phone);
            return new Captcha(captcha, expireAt);
        }

        throw new CaptchaSendException("验证码发送失败");
    }

    @Override
    public Captcha lookupCaptcha(String phone) {

        List<AbstractZSet.Member<String>> captchas = captchaCache.topWithScore(phone, 1, false);
        if (null == captchas || captchas.isEmpty()) {
            return null;
        }

        return new Captcha(captchas.get(0).getValue(), captchas.get(0).getScore().longValue());
    }

    @Override
    public void invalidCaptcha(String phone, String captcha) {
        captchaCache.remove(phone, captcha);
    }

}
