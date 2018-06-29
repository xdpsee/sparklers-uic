package com.zhenhui.demo.sparklers.domain.repository;

import java.util.Set;

import com.zhenhui.demo.sparklers.domain.model.User;

public interface UserRepository {

    User getUser(long userId);

    User getUser(String phone);

    boolean createUser(String phone, String secret, Set<String> authorities);

}


