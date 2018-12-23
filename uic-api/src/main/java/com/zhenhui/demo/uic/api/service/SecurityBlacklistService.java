package com.zhenhui.demo.uic.api.service;

public interface SecurityBlacklistService {

    void block(String token);

    boolean isBlocked(String token);

}

