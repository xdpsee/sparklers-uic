package com.zhenhui.demo.sparklers.data.cache.serializer;

import com.zhenhui.library.redis.serializer.Serializer;
import org.springframework.stereotype.Component;

@Component
public class StringSerializer implements Serializer<String> {

    @Override
    public String serialize(String s) {
        return s;
    }

    @Override
    public String deserialize(String s) {
        return s;
    }
}

