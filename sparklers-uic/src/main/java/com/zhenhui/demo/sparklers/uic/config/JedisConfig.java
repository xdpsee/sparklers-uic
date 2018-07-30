package com.zhenhui.demo.sparklers.uic.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Optional;

@Configuration
public class JedisConfig {

    @Bean
    public JedisPool jedisPool(JedisConnectionFactory connectionFactory) {

        Optional<GenericObjectPoolConfig> poolConfig = connectionFactory.getClientConfiguration().getPoolConfig();

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        if (!poolConfig.isPresent()) {
            throw new BeanCreationException("JedisConnectionFactory has no poolConfig");
        }

        BeanUtils.copyProperties(poolConfig.get(), jedisPoolConfig);

        return new JedisPool(jedisPoolConfig, connectionFactory.getHostName(), connectionFactory.getPort());

    }

}


