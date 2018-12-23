package com.zhenhui.demo.sparklers.uic.config;

import com.zhenhui.demo.sparklers.uic.security.SecurityAuthenticationFilter;
import com.zhenhui.demo.sparklers.uic.security.SecurityTokenProducer;
import com.zhenhui.demo.uic.api.service.SecurityBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityTokenProducer tokenUtils(@Value("${jwt.issuer}") String issuer
            , @Value("${jwt.ttl}") String expiresInSeconds
            , @Value("${jwt.secret}") String secret) {
        return new SecurityTokenProducer(issuer, expiresInSeconds, secret);
    }

    @Bean
    public SecurityAuthenticationFilter authenticationFilter(@Value("jwt.http-header") String authHeader
            , @Autowired SecurityTokenProducer tokenProducer
            , @Autowired SecurityBlacklistService securityBlacklistService) {
        return new SecurityAuthenticationFilter(authHeader
                , tokenProducer
                , securityBlacklistService);
    }
}

