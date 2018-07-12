package com.zhenhui.demo.sparklers.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class JedisConfig {

    @Bean("jedis.config")
    public JedisPoolConfig jedisPoolConfig(//
                                           @Value("${jedis.pool.min-idle}") int minIdle, //
                                           @Value("${jedis.pool.max-idle}") int maxIdle, //
                                           @Value("${jedis.pool.max-wait}") int maxWaitMillis, //
                                           @Value("${jedis.pool.block-when-exhausted}") boolean blockWhenExhausted, //
                                           @Value("${jedis.pool.max-total}") int maxTotal) {

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMinIdle(minIdle);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setMaxTotal(maxTotal);
        config.setBlockWhenExhausted(blockWhenExhausted);
        config.setJmxEnabled(true);
        return config;
    }

    @Bean
    public JedisPool jedisPool(//
                               @Qualifier("jedis.config") JedisPoolConfig config, //
                               @Value("${jedis.host}") String host, //
                               @Value("${jedis.port}") int port) {
        return new JedisPool(config, host, port);
    }

}
