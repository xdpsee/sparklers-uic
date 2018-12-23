package com.zhenhui.demo.sparklers.uic.domain.interactor;

import com.zhenhui.demo.sparklers.uic.domain.exception.BadSecretException;
import com.zhenhui.demo.sparklers.uic.domain.exception.UserNotFoundException;
import com.zhenhui.demo.sparklers.uic.domain.executor.PostExecutionThread;
import com.zhenhui.demo.sparklers.uic.domain.executor.ThreadExecutor;
import com.zhenhui.demo.sparklers.uic.domain.interactor.SigninWithSecret.Params;
import com.zhenhui.demo.sparklers.uic.domain.model.User;
import com.zhenhui.demo.sparklers.uic.domain.repository.UserRepository;
import com.zhenhui.demo.sparklers.uic.security.SecurityTokenProducer;
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
    private final SecurityTokenProducer tokenProducer;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SigninWithSecret(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread,
                            UserRepository userRepository,
                            SecurityTokenProducer tokenProducer,
                            PasswordEncoder passwordEncoder) {
        super(threadExecutor, postExecutionThread);
        this.userRepository = userRepository;
        this.tokenProducer = tokenProducer;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    Observable<String> buildObservable(Params params) {

        return Observable.create((emitter) -> {
            final User user = userRepository.getUser(params.phone);
            if (null == user) {
                emitter.onError(new UserNotFoundException(params.phone));
            } else {
                if (!passwordEncoder.matches(params.secret, user.getSecret())) {
                    emitter.onError(new BadSecretException(""));
                } else {
                    emitter.onNext(tokenProducer.createToken(user.toPrincipal()));
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
