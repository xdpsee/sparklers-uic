package com.zhenhui.demo.sparklers.domain.interactor;

import com.zhenhui.demo.sparklers.domain.exception.CaptchaExpireException;
import com.zhenhui.demo.sparklers.domain.exception.CaptchaMismatchException;
import com.zhenhui.demo.sparklers.domain.exception.UserNotFoundException;
import com.zhenhui.demo.sparklers.domain.executor.PostExecutionThread;
import com.zhenhui.demo.sparklers.domain.executor.ThreadExecutor;
import com.zhenhui.demo.sparklers.domain.model.User;
import com.zhenhui.demo.sparklers.domain.repository.CaptchaRepository;
import com.zhenhui.demo.sparklers.domain.repository.UserRepository;
import com.zhenhui.demo.sparklers.security.Principal;
import com.zhenhui.demo.sparklers.security.TokenUtils;
import io.reactivex.Observable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * exceptions: UserNotFoundException,
 */
@Component
public class SigninWithCaptcha extends UseCase<SigninWithCaptcha.Params, String> {

    private final UserRepository userRepository;
    private final TokenUtils tokenUtils;
    private final CaptchaRepository captchaRepository;

    @Autowired
    public SigninWithCaptcha(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             UserRepository userRepository,
                             TokenUtils tokenUtils,
                             CaptchaRepository captchaRepository) {
        super(threadExecutor, postExecutionThread);
        this.userRepository = userRepository;
        this.tokenUtils = tokenUtils;
        this.captchaRepository = captchaRepository;
    }

    @Override
    Observable<String> buildObservable(Params params) {

        return Observable.create((emitter) -> {
            final String captcha = captchaRepository.lookupCaptcha(params.phone);
            if (null == captcha) {
                emitter.onError(new CaptchaExpireException());
                return;
            }

            if (!captcha.equals(params.captcha)) {
                emitter.onError(new CaptchaMismatchException());
                return;
            }

            captchaRepository.invalidCaptcha(params.phone);

            final User user = userRepository.getUser(params.phone);
            if (null == user) {
                emitter.onError(new UserNotFoundException(params.phone));

            } else {

                emitter.onNext(tokenUtils.createToken(Principal.fromUser(user)));
                emitter.onComplete();
            }
        });
    }

    @Data
    @AllArgsConstructor
    public static class Params {
        @NotNull
        private String phone;
        @NotNull
        private String captcha;
    }

}
