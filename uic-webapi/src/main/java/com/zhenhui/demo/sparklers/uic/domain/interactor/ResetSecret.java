package com.zhenhui.demo.sparklers.uic.domain.interactor;

import com.zhenhui.demo.sparklers.uic.domain.exception.UserNotFoundException;
import com.zhenhui.demo.sparklers.uic.domain.executor.PostExecutionThread;
import com.zhenhui.demo.sparklers.uic.domain.executor.ThreadExecutor;
import com.zhenhui.demo.sparklers.uic.domain.model.User;
import com.zhenhui.demo.sparklers.uic.domain.repository.UserRepository;
import com.zhenhui.demo.sparklers.uic.utils.CaptchaUtil;
import io.reactivex.Observable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResetSecret extends UseCase<ResetSecret.Params, Boolean> {

    private final UserRepository userRepository;

    private final CaptchaUtil captchaUtil;

    @Autowired
    public ResetSecret(ThreadExecutor threadExecutor,
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
                final User user = userRepository.getUser(params.phone);
                if (null == user) {
                    throw new UserNotFoundException(params.phone);
                }

                captchaUtil.verifyCaptcha(params.phone, params.captcha);

                emitter.onNext(userRepository.updateSecret(params.phone, params.secret) != null);
                emitter.onComplete();

            } catch (Exception e) {
                emitter.onError(e);
            }

        });

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Params {

        private String phone;

        private String captcha;

        private String secret;

    }

}
