package com.zhenhui.demo.sparklers.domain.interactor;

import java.util.Optional;
import java.util.concurrent.Executor;

import com.zhenhui.demo.sparklers.domain.model.User;
import com.zhenhui.demo.sparklers.domain.repository.UserRepository;
import io.reactivex.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetUser extends UseCase<Long, Optional<User>> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public GetUser(Executor executor) {
        super(executor);
    }

    @Override
    protected Observable<Optional<User>> run(Long userId) {
        return userRepository.getUser(userId);
    }
}




