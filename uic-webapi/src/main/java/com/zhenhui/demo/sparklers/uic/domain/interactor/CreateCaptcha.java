package com.zhenhui.demo.sparklers.uic.domain.interactor;

import com.zhenhui.demo.sparklers.uic.domain.executor.PostExecutionThread;
import com.zhenhui.demo.sparklers.uic.domain.executor.ThreadExecutor;
import com.zhenhui.demo.sparklers.uic.domain.model.Captcha;
import com.zhenhui.demo.sparklers.uic.domain.repository.CaptchaRepository;
import io.reactivex.Observable;
import org.springframework.stereotype.Component;

@Component
public class CreateCaptcha extends UseCase<String, String> {

    private final CaptchaRepository captchaRepository;

    public CreateCaptcha(ThreadExecutor threadExecutor,
                         PostExecutionThread postExecutionThread,
                         CaptchaRepository captchaRepository) {
        super(threadExecutor, postExecutionThread);
        this.captchaRepository = captchaRepository;
    }

    @Override
    Observable<String> buildObservable(String phone) {

        return Observable.create((emitter) -> {
            try {
                Captcha captcha = captchaRepository.createCaptcha(phone);
                emitter.onNext(captcha.getCode());
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });

    }
}
