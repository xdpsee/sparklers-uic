package com.zhenhui.demo.sparklers.uic.domain.repository;

import java.util.Set;

import com.zhenhui.demo.sparklers.uic.domain.model.User;

public interface UserRepository {

    User getUser(long userId);

    User getUser(String phone);

    boolean createUser(String phone, String secret, Set<String> authorities);

    User updateSecret(String phone, String secret);

}


