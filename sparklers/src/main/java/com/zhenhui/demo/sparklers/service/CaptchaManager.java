package com.zhenhui.demo.sparklers.service;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;


@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
public class CaptchaManager implements InitializingBean {

    @Autowired
    private CacheManager cacheManager;

    private Cache cache;

    private final static RandomStringGenerator generator = new RandomStringGenerator.Builder()
            .withinRange('0', '9').build();

    @Override
    public void afterPropertiesSet() throws Exception {
        cache = cacheManager.getCache("captchas");
    }

    public String createCaptcha(String phone, boolean create) {

        String captcha;

        if (create) {
            captcha = generator.generate(4);
            cache.put(phone, captcha);
        } else {
            return lookupCaptcha(phone);
        }

        return captcha;
    }

    public String lookupCaptcha(String phone) {
        Cache.ValueWrapper element = cache.get(phone);
        if (element != null) {
            return (String) element.get();
        }

        return null;
    }

    public void invalidCaptcha(String phone) {
        cache.evict(phone);
    }

    public void removeAll() {
        cache.clear();
    }

}
