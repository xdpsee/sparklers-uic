package com.zhenhui.demo.sparklers.security;

public interface BlacklistService {

    void block(String token);

    boolean isBlocked(String token);

}

