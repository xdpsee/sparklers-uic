package com.zhenhui.demo.sparklers.domain.interactor;

import java.util.Set;

import com.zhenhui.demo.sparklers.domain.exception.UserAlreadyExistException;
import com.zhenhui.demo.sparklers.domain.executor.PostExecutionThread;
import com.zhenhui.demo.sparklers.domain.executor.ThreadExecutor;
import com.zhenhui.demo.sparklers.domain.repository.UserRepository;
import com.zhenhui.demo.sparklers.utils.ExceptionUtils;
import io.reactivex.Observable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * exceptions: UserAlreadyExistException
 */
@Component
public class CreateUser extends UseCase<CreateUser.Params, Boolean> {

    private final UserRepository userRepository;

    @Autowired
    public CreateUser(ThreadExecutor threadExecutor,
                      PostExecutionThread postExecutionThread,
                      UserRepository userRepository) {
        super(threadExecutor, postExecutionThread);
        this.userRepository = userRepository;
    }

    @Override
    Observable<Boolean> buildObservable(Params params) {

        return Observable.create((emitter) -> {
            try {
                boolean success = userRepository.createUser(params.phone, params.secret, params.authorities);
                emitter.onNext(success);
                emitter.onComplete();
            } catch (Exception e) {
                if (ExceptionUtils.hasDuplicateEntryException(e)) {
                    emitter.onError(new UserAlreadyExistException("", e));
                } else {
                    emitter.onError(e);
                }
            }
        });
    }

    @Data
    @AllArgsConstructor
    public static class Params {
        private String phone;
        private String secret;
        private Set<String> authorities;
    }

}
