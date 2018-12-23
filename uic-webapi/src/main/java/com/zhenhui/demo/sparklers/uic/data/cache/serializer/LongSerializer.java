package com.zhenhui.demo.sparklers.uic.data.cache.serializer;

import com.zhenhui.library.redis.serializer.Serializer;
import org.springframework.stereotype.Component;

@Component
public class LongSerializer implements Serializer<Long> {

    @Override
    public String serialize(Long aLong) {
        return String.valueOf(aLong);
    }

    @Override
    public Long deserialize(String s) {
        return Long.parseLong(s);
    }
}
