package com.zhenhui.demo.sparklers.security;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.common.cache.CacheBuilder;
import com.zhenhui.demo.sparklers.data.cache.BlacklistCache;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection,unused")
@Service(interfaceClass = BlacklistService.class)
public class BlacklistServiceImpl implements BlacklistService {

    @Autowired
    private BlacklistCache blacklistCache;

    private final com.google.common.cache.Cache<String, Boolean> memoryCache = CacheBuilder.newBuilder()
            .expireAfterWrite(6, TimeUnit.SECONDS)
            .maximumSize(100000)
            .build();

    @Override
    public boolean isBlocked(String token) {
        Boolean blocked = memoryCache.getIfPresent(token);
        if (blocked != null) {
            return blocked;
        }

        blocked = blacklistCache.exists(token);
        memoryCache.put(token, blocked);

        return blocked;
    }

    @Override
    public void block(String token) {

        blacklistCache.put(token, true);

        memoryCache.invalidate(token);

    }
}

