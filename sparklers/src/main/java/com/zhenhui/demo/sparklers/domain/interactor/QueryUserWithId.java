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
public class QueryUserWithId extends UseCase<Long, Optional<User>> {

    private final UserRepository userRepository;

    @Autowired
    public QueryUserWithId(ThreadExecutor threadExecutor,
                           PostExecutionThread postExecutionThread,
                           UserRepository userRepository) {
        super(threadExecutor, postExecutionThread);
        this.userRepository = userRepository;
    }

    @Override
    Observable<Optional<User>> buildObservable(Long userId) {

        return Observable.create((emitter) -> {
            try {
                final User user = userRepository.getUser(userId);
                emitter.onNext(Optional.ofNullable(user));
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
}




