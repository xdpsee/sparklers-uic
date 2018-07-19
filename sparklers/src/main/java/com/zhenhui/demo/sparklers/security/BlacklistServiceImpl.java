package com.zhenhui.demo.sparklers.security;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection,unused")
@Service(interfaceClass = BlacklistService.class)
public class BlacklistServiceImpl implements BlacklistService, InitializingBean {

    @Autowired
    private CacheManager cacheManager;

    private Cache cache;

    @Override
    public void afterPropertiesSet() {
        cache = cacheManager.getCache("blacklist");
    }

    @Override
    public boolean isBlocked(String token) {
        Cache.ValueWrapper wrapper = cache.get(token);
        return wrapper != null && wrapper.get() != null;
    }

    @Override
    public void block(String token) {
        cache.put(token, true);
    }
}
