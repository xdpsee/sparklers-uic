package com.zhenhui.demo.sparklers.uic.domain.interactor;

import com.zhenhui.demo.sparklers.uic.domain.exception.CaptchaExpireException;
import com.zhenhui.demo.sparklers.uic.domain.exception.CaptchaMismatchException;
import com.zhenhui.demo.sparklers.uic.domain.exception.CaptchaNotFoundException;
import com.zhenhui.demo.sparklers.uic.domain.exception.UserAlreadyExistException;
import com.zhenhui.demo.sparklers.uic.domain.executor.PostExecutionThread;
import com.zhenhui.demo.sparklers.uic.domain.executor.ThreadExecutor;
import com.zhenhui.demo.sparklers.uic.domain.model.Captcha;
import com.zhenhui.demo.sparklers.uic.domain.repository.CaptchaRepository;
import com.zhenhui.demo.sparklers.uic.domain.repository.UserRepository;
import com.zhenhui.demo.sparklers.uic.utils.ExceptionUtils;
import io.reactivex.Observable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * exceptions: UserAlreadyExistException
 */
@Component
public class CreateUser extends UseCase<CreateUser.Params, Boolean> {

    private final UserRepository userRepository;

    private final CaptchaRepository captchaRepository;

    @Autowired
    public CreateUser(ThreadExecutor threadExecutor,
                      PostExecutionThread postExecutionThread,
                      UserRepository userRepository,
                      CaptchaRepository captchaRepository) {
        super(threadExecutor, postExecutionThread);
        this.userRepository = userRepository;
        this.captchaRepository = captchaRepository;
    }

    @Override
    Observable<Boolean> buildObservable(Params params) {

        return Observable.create((emitter) -> {
            try {
                final Captcha captcha = captchaRepository.lookupCaptcha(params.phone);
                if (null == captcha) {
                    emitter.onError(new CaptchaNotFoundException(params.captcha));
                    return;
                }

                if (captcha.getExpireAt() <= System.currentTimeMillis()) {
                    emitter.onError(new CaptchaExpireException());
                    return;
                }

                if (!captcha.getCode().equals(params.captcha)) {
                    emitter.onError(new CaptchaMismatchException());
                    return;
                }

                captchaRepository.invalidCaptcha(params.phone, captcha.getCode());

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
        private String captcha;
    }

}
