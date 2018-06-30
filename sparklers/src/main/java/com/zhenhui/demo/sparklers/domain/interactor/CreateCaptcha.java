package com.zhenhui.demo.sparklers.domain.interactor;

import com.zhenhui.demo.sparklers.domain.executor.PostExecutionThread;
import com.zhenhui.demo.sparklers.domain.executor.ThreadExecutor;
import com.zhenhui.demo.sparklers.domain.repository.CaptchaRepository;
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
                emitter.onNext(captchaRepository.createCaptcha(phone, true));
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });

    }
}
