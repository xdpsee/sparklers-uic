package com.zhenhui.demo.sparklers.data.cache;

import com.zhenhui.library.redis.cache.AbstractValue;
import com.zhenhui.library.redis.serializer.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class BlacklistCache extends AbstractValue<String, Boolean> {

    @Autowired
    public BlacklistCache(Serializer<String> keySerializer
            , Serializer<Boolean> valueSerializer
            , @Value("${jwt.ttl}") Integer expireSeconds) {
        super("", keySerializer, valueSerializer, expireSeconds, TimeUnit.SECONDS);
    }


}
