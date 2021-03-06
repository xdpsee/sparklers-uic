package com.zhenhui.demo.sparklers.uic.domain.interactor;

import java.util.Optional;

import com.zhenhui.demo.sparklers.uic.domain.executor.PostExecutionThread;
import com.zhenhui.demo.sparklers.uic.domain.executor.ThreadExecutor;
import com.zhenhui.demo.sparklers.uic.domain.model.User;
import com.zhenhui.demo.sparklers.uic.domain.repository.UserRepository;
import io.reactivex.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QueryUserWithPhone extends UseCase<String, Optional<User>>{

    private final UserRepository userRepository;
    @Autowired
    public QueryUserWithPhone(ThreadExecutor threadExecutor,
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

