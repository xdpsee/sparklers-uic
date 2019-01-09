package com.zhenhui.demo.sparklers.uic.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.common.cache.CacheBuilder;
import com.zhenhui.demo.sparklers.uic.data.cache.TokenBlacklistCache;
import com.zhenhui.demo.uic.api.service.SecurityBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection,unused")
@Service(version = "1.0.0")
public class SecurityBlacklistServiceImpl implements SecurityBlacklistService {

    @Autowired
    private TokenBlacklistCache tokenBlacklistCache;

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

        blocked = tokenBlacklistCache.exists(token);
        memoryCache.put(token, blocked);

        return blocked;
    }

    @Override
    public void block(String token) {

        tokenBlacklistCache.put(token, true);

        memoryCache.invalidate(token);

    }
}

