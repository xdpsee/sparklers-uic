package com.zhenhui.demo.sparklers.domain.interactor;

import com.zhenhui.demo.sparklers.domain.exception.BadSecretException;
import com.zhenhui.demo.sparklers.domain.exception.UserNotFoundException;
import com.zhenhui.demo.sparklers.domain.executor.PostExecutionThread;
import com.zhenhui.demo.sparklers.domain.executor.ThreadExecutor;
import com.zhenhui.demo.sparklers.domain.interactor.SigninWithSecret.Params;
import com.zhenhui.demo.sparklers.domain.model.User;
import com.zhenhui.demo.sparklers.domain.repository.UserRepository;
import com.zhenhui.demo.sparklers.security.Principal;
import com.zhenhui.demo.sparklers.security.TokenUtils;
import io.reactivex.Observable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * exceptions: UserNotFoundException, BadSecretException
 */
@Component
public class SigninWithSecret extends UseCase<Params, String> {

    private final UserRepository userRepository;
    private final TokenUtils tokenUtils;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SigninWithSecret(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread,
                            UserRepository userRepository,
                            TokenUtils tokenUtils,
                            PasswordEncoder passwordEncoder) {
        super(threadExecutor, postExecutionThread);
        this.userRepository = userRepository;
        this.tokenUtils = tokenUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    Observable<String> buildObservable(Params params) {

        return Observable.create((emitter) -> {
            final User user = userRepository.getUser(params.phone);
            if (null == user) {
                emitter.onError(new UserNotFoundException(params.phone));
            } else {
                if (!passwordEncoder.matches(user.getSecret()
                    , passwordEncoder.encode(params.secret))) {
                    emitter.onError(new BadSecretException(""));
                } else {
                    emitter.onNext(tokenUtils.createToken(Principal.fromUser(user)));
                    emitter.onComplete();
                }
            }
        });
    }

    @Data
    @AllArgsConstructor
    public static class Params {
        @NotNull
        private String phone;
        @NotNull
        private String secret;
    }

}
