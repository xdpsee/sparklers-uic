package com.zhenhui.demo.sparklers.uic.data.cache.serializer;

import com.zhenhui.library.redis.serializer.Serializer;
import org.springframework.stereotype.Component;

@Component
public class BooleanSerializer implements Serializer<Boolean> {

    @Override
    public String serialize(Boolean aBoolean) {
        return String.valueOf(aBoolean);
    }

    @Override
    public Boolean deserialize(String s) {
        return Boolean.valueOf(s);
    }
}
