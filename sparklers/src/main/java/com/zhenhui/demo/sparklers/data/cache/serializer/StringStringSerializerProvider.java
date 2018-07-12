package com.zhenhui.demo.sparklers.data.cache.serializer;

import com.zhenhui.library.redis.cache.support.SerializerProvider;
import com.zhenhui.library.redis.serializer.Serializer;
import com.zhenhui.library.redis.serializer.StringSerializer;
import org.springframework.stereotype.Component;

@Component
public class StringStringSerializerProvider implements SerializerProvider<String, String> {

    @Override
    public Serializer<String> keySerializer() {
        return new StringSerializer();
    }

    @Override
    public Serializer<String> valueSerializer() {
        return new StringSerializer();
    }
}
