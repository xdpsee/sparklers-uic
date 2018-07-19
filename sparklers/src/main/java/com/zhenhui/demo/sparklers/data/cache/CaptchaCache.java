package com.zhenhui.demo.sparklers.data.cache;

import com.zhenhui.demo.sparklers.data.cache.serializer.StringSerializer;
import com.zhenhui.library.redis.cache.AbstractZSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class CaptchaCache extends AbstractZSet<String, String> {

    @Autowired
    public CaptchaCache(StringSerializer keySerializer
            , StringSerializer memberSerializer
            , @Value("${captcha.expire.minutes}") int expireMinutes) {
        super("captcha", keySerializer, memberSerializer, expireMinutes, TimeUnit.MINUTES);
    }


}
