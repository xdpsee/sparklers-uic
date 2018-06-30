package com.zhenhui.demo.sparklers.domain.repository;

public interface BlacklistRepository {

    boolean isBlocked(String token);

    void block(String token);

}
