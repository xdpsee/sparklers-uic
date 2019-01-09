package com.zhenhui.demo.uic.api.service;

public interface SecurityBlacklistService {

    boolean isBlocked(String token);

    void block(String token);

}
