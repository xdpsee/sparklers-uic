package com.zhenhui.demo.sparklers.data.cache;

import com.zhenhui.library.redis.cache.AbstractValueCache;
import com.zhenhui.library.redis.cache.support.SerializerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class CaptchaCache extends AbstractValueCache<String, String> {

    @Autowired
    public CaptchaCache(SerializerProvider<String, String> serializerProvider) {
        super("captcha", serializerProvider, 5, TimeUnit.MINUTES);
    }



}
