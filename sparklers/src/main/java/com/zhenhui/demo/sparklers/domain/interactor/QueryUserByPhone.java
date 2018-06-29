package com.zhenhui.demo.sparklers.domain.interactor;

import java.util.Optional;

import com.zhenhui.demo.sparklers.domain.executor.PostExecutionThread;
import com.zhenhui.demo.sparklers.domain.executor.ThreadExecutor;
import com.zhenhui.demo.sparklers.domain.model.User;
import com.zhenhui.demo.sparklers.domain.repository.UserRepository;
import io.reactivex.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueryUserByPhone extends UseCase<String, Optional<User>>{

    private final UserRepository userRepository;
    @Autowired
    public QueryUserByPhone(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread,
                            UserRepository userRepository) {
        super(threadExecutor, postExecutionThread);
        this.userRepository = userRepository;
    }

    @Override
    Observable<Optional<User>> buildObservable(String phone) {
        return Observable.create((emitter) -> {
            try {
                emitter.onNext(Optional.ofNullable(userRepository.getUser(phone)));
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
}

