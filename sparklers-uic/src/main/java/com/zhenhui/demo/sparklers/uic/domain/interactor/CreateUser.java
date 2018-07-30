package com.zhenhui.demo.sparklers.uic.domain.interactor;

import com.zhenhui.demo.sparklers.uic.domain.exception.CaptchaExpireException;
import com.zhenhui.demo.sparklers.uic.domain.exception.CaptchaMismatchException;
import com.zhenhui.demo.sparklers.uic.domain.exception.CaptchaNotFoundException;
import com.zhenhui.demo.sparklers.uic.domain.exception.UserAlreadyExistException;
import com.zhenhui.demo.sparklers.uic.domain.executor.PostExecutionThread;
import com.zhenhui.demo.sparklers.uic.domain.executor.ThreadExecutor;
import com.zhenhui.demo.sparklers.uic.domain.model.Captcha;
import com.zhenhui.demo.sparklers.uic.domain.repository.UserRepository;
import com.zhenhui.demo.sparklers.uic.utils.CaptchaUtil;
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

    private final CaptchaUtil captchaUtil;

    @Autowired
    public CreateUser(ThreadExecutor threadExecutor,
                      PostExecutionThread postExecutionThread,
                      UserRepository userRepository,
                      CaptchaUtil captchaUtil) {
        super(threadExecutor, postExecutionThread);
        this.userRepository = userRepository;
        this.captchaUtil = captchaUtil;
    }

    @Override
    Observable<Boolean> buildObservable(Params params) {

        return Observable.create((emitter) -> {
            try {
                captchaUtil.verifyCaptcha(params.phone, params.captcha);

                boolean success = userRepository.createUser(params.phone, params.secret, params.authorities);
                if (success) {
                    captchaUtil.clearCaptcha(params.phone, params.captcha);
                }

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
