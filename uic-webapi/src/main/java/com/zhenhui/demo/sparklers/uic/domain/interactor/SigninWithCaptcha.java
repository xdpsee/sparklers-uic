package com.zhenhui.demo.sparklers.uic.domain.interactor;

import com.zhenhui.demo.sparklers.uic.domain.exception.UserNotFoundException;
import com.zhenhui.demo.sparklers.uic.domain.executor.PostExecutionThread;
import com.zhenhui.demo.sparklers.uic.domain.executor.ThreadExecutor;
import com.zhenhui.demo.sparklers.uic.domain.model.User;
import com.zhenhui.demo.sparklers.uic.domain.repository.UserRepository;
import com.zhenhui.demo.sparklers.uic.security.SecurityTokenProducer;
import com.zhenhui.demo.sparklers.uic.utils.CaptchaUtil;
import io.reactivex.Observable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * exceptions: UserNotFoundException,
 */
@Component
public class SigninWithCaptcha extends UseCase<SigninWithCaptcha.Params, String> {

    private final UserRepository userRepository;
    private final SecurityTokenProducer tokenProducer;
    private final CaptchaUtil captchaUtil;

    @Autowired
    public SigninWithCaptcha(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             UserRepository userRepository,
                             SecurityTokenProducer tokenProducer,
                             CaptchaUtil captchaUtil) {
        super(threadExecutor, postExecutionThread);
        this.userRepository = userRepository;
        this.tokenProducer = tokenProducer;
        this.captchaUtil = captchaUtil;
    }

    @Override
    Observable<String> buildObservable(Params params) {

        return Observable.create((emitter) -> {

            captchaUtil.verifyCaptcha(params.phone, params.captcha);

            final User user = userRepository.getUser(params.phone);
            if (null == user) {
                emitter.onError(new UserNotFoundException(params.phone));

            } else {
                captchaUtil.clearCaptcha(params.phone, params.captcha);

                emitter.onNext(tokenProducer.createToken(user.toPrincipal()));
                emitter.onComplete();
            }
        });
    }

    @Data
    @AllArgsConstructor
    public static class Params {

        private String phone;

        private String captcha;

    }

}
