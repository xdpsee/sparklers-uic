package com.zhenhui.demo.sparklers.uic.security;

public interface JsonWebTokenBlacklistService {

    void block(String token);

    boolean isBlocked(String token);

}

