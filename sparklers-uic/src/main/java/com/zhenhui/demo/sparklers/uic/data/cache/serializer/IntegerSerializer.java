package com.zhenhui.demo.sparklers.uic.data.cache.serializer;

import com.zhenhui.library.redis.serializer.Serializer;
import org.springframework.stereotype.Component;

@Component
public class IntegerSerializer implements Serializer<Integer> {

    @Override
    public String serialize(Integer integer) {
        return String.valueOf(integer);
    }

    @Override
    public Integer deserialize(String s) {
        return Integer.valueOf(s);
    }
}
