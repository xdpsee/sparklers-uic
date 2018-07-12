package com.zhenhui.demo.sparklers.data.repository;

import com.zhenhui.demo.sparklers.data.cache.CaptchaCache;
import com.zhenhui.demo.sparklers.domain.repository.CaptchaRepository;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;


@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
public class CaptchaRepositoryImpl implements CaptchaRepository {

    @Autowired
    private CaptchaCache captchaCache;

    private final static RandomStringGenerator generator = new RandomStringGenerator.Builder()
            .withinRange('0', '9').build();

    @Override
    public String createCaptcha(String phone, boolean create) {

        String captcha;

        if (create) {
            captcha = generator.generate(4);
            captchaCache.put(phone, captcha);
        } else {
            return lookupCaptcha(phone);
        }

        return captcha;
    }

    @Override
    public String lookupCaptcha(String phone) {
        return captchaCache.get(phone);
    }

    @Override
    public void invalidCaptcha(String phone) {
        captchaCache.evict(phone);
    }

}
