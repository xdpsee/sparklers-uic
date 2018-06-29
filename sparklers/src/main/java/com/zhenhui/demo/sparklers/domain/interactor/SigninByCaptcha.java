package com.zhenhui.demo.sparklers.domain.interactor;

import javax.validation.constraints.NotNull;

import com.zhenhui.demo.sparklers.domain.exception.UserNotFoundException;
import com.zhenhui.demo.sparklers.domain.executor.PostExecutionThread;
import com.zhenhui.demo.sparklers.domain.executor.ThreadExecutor;
import com.zhenhui.demo.sparklers.domain.model.SocialType;
import com.zhenhui.demo.sparklers.domain.model.User;
import com.zhenhui.demo.sparklers.domain.repository.UserRepository;
import com.zhenhui.demo.sparklers.security.Principal;
import com.zhenhui.demo.sparklers.security.TokenUtils;
import io.reactivex.Observable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SigninByCaptcha extends UseCase<SigninByCaptcha.Params, String> {

    private final UserRepository userRepository;
    private final TokenUtils tokenUtils;

    @Autowired
    public SigninByCaptcha(ThreadExecutor threadExecutor,
                           PostExecutionThread postExecutionThread,
                           UserRepository userRepository,
                           TokenUtils tokenUtils) {
        super(threadExecutor, postExecutionThread);
        this.userRepository = userRepository;
        this.tokenUtils = tokenUtils;
    }

    @Override
    Observable<String> buildObservable(Params params) {

        return Observable.create((emitter) -> {
            final User user = userRepository.getUser(params.phone);
            if (null == user) {
                emitter.onError(new UserNotFoundException(params.phone));
            } else {
                Principal principal = new Principal();
                principal.setUserId(user.getId());
                principal.setPhone(user.getName());
                principal.setAuthorities(user.getAuthorities());
                principal.setType(SocialType.NONE);

                emitter.onNext(tokenUtils.createToken(principal));
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
