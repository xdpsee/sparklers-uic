package com.zhenhui.demo.sparklers.domain.repository;

import java.util.Optional;

import com.zhenhui.demo.sparklers.domain.model.User;
import io.reactivex.Observable;

public interface UserRepository {

    Observable<Optional<User>> getUser(long userId);

}


