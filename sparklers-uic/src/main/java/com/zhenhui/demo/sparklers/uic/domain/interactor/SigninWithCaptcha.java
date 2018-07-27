package com.zhenhui.demo.sparklers.uic.domain.interactor;

import com.zhenhui.demo.sparklers.uic.domain.exception.CaptchaExpireException;
import com.zhenhui.demo.sparklers.uic.domain.exception.CaptchaMismatchException;
import com.zhenhui.demo.sparklers.uic.domain.exception.CaptchaNotFoundException;
import com.zhenhui.demo.sparklers.uic.domain.exception.UserNotFoundException;
import com.zhenhui.demo.sparklers.uic.domain.executor.PostExecutionThread;
import com.zhenhui.demo.sparklers.uic.domain.executor.ThreadExecutor;
import com.zhenhui.demo.sparklers.uic.domain.model.Captcha;
import com.zhenhui.demo.sparklers.uic.domain.model.User;
import com.zhenhui.demo.sparklers.uic.domain.repository.CaptchaRepository;
import com.zhenhui.demo.sparklers.uic.domain.repository.UserRepository;
import com.zhenhui.demo.sparklers.uic.security.TokenUtils;
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
            final Captcha captcha = captchaRepository.lookupCaptcha(params.phone);
            if (null == captcha) {
                emitter.onError(new CaptchaNotFoundException(params.getCaptcha()));
                return;
            }

            if (captcha.getExpireAt() < System.currentTimeMillis()) {
                emitter.onError(new CaptchaExpireException());
                return;
            }

            if (!captcha.getCode().equals(params.captcha)) {
                emitter.onError(new CaptchaMismatchException());
                return;
            }

            captchaRepository.invalidCaptcha(params.phone, params.getCaptcha());

            final User user = userRepository.getUser(params.phone);
            if (null == user) {
                emitter.onError(new UserNotFoundException(params.phone));

            } else {

                emitter.onNext(tokenUtils.createToken(user.toPrincipal()));
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
