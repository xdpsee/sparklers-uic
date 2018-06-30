package com.zhenhui.demo.sparklers.data.repository;

import com.zhenhui.demo.sparklers.domain.repository.BlacklistRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
public class BlacklistRepositoryImpl implements BlacklistRepository, InitializingBean {

    @Autowired
    private CacheManager cacheManager;

    private Cache cache;

    @Override
    public void afterPropertiesSet() throws Exception {
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
