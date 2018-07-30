package com.zhenhui.demo.sparklers.uic.domain.repository;

import java.util.Set;

import com.zhenhui.demo.sparklers.uic.domain.model.User;

public interface UserRepository {

    User getUser(long userId);

    User getUser(String phone);

    boolean userExists(long userId);

    boolean createUser(String phone, String secret, Set<String> authorities);

    boolean updateSecret(String phone, String secret);

}


