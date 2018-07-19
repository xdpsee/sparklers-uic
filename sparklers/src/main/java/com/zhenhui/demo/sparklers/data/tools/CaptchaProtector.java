package com.zhenhui.demo.sparklers.data.tools;

import com.zhenhui.library.redis.cache.AbstractValue;
import com.zhenhui.library.redis.serializer.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class CaptchaProtector {

    @Component
    public static class AccessCache extends AbstractValue<String, Integer> {

        @Autowired
        public AccessCache(Serializer<String> keySerializer
                , Serializer<Integer> valueSerializer
                , @Value("${captcha.send.interval}") Integer expireSeconds) {
            super("code-protect", keySerializer, valueSerializer, expireSeconds, TimeUnit.SECONDS);
        }
    }

    @Autowired
    private AccessCache accessCache;

    @Value("${captcha.send.protect}")
    private boolean sendCaptchaProtect;

    public boolean acceptable(String phone) {
        if (!sendCaptchaProtect) {
            return true;
        }

        return accessCache.exists(phone);
    }

    public void pass(String phone) {
        accessCache.put(phone, 1);
    }

}
