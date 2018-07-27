package com.zhenhui.demo.sparklers.uic.domain.interactor;

import com.zhenhui.demo.sparklers.uic.common.SocialType;
import com.zhenhui.demo.sparklers.uic.domain.exception.Auth3rdNoUserBondException;
import com.zhenhui.demo.sparklers.uic.domain.exception.Auth3rdUserException;
import com.zhenhui.demo.sparklers.uic.domain.executor.PostExecutionThread;
import com.zhenhui.demo.sparklers.uic.domain.executor.ThreadExecutor;
import com.zhenhui.demo.sparklers.uic.domain.model.User;
import com.zhenhui.demo.sparklers.uic.domain.model.User3rd;
import com.zhenhui.demo.sparklers.uic.domain.repository.User3rdRepository;
import com.zhenhui.demo.sparklers.uic.domain.repository.UserRepository;
import com.zhenhui.demo.sparklers.uic.domain.th3rd.OpenUserInfo;
import com.zhenhui.demo.sparklers.uic.domain.th3rd.Token3rdVerify;
import com.zhenhui.demo.sparklers.uic.security.TokenUtils;
import io.reactivex.Observable;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * exceptions Auth3rdUserException
 */
@Component
public class SigninWithSocial extends UseCase<SigninWithSocial.Params, String> {

    private final UserRepository userRepository;

    private final TokenUtils tokenUtils;

    private final Token3rdVerify token3rdVerify;

    private final User3rdRepository user3rdRepository;

    @Data
    public static class Params {
        private SocialType socialType;
        private String token;
    }

    @Autowired
    public SigninWithSocial(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread,
                            UserRepository userRepository,
                            TokenUtils tokenUtils,
                            Token3rdVerify token3rdVerify,
                            User3rdRepository user3rdRepository) {
        super(threadExecutor, postExecutionThread);
        this.userRepository = userRepository;
        this.tokenUtils = tokenUtils;
        this.token3rdVerify = token3rdVerify;
        this.user3rdRepository = user3rdRepository;
    }

    @Override
    Observable<String> buildObservable(Params params) {

        return Observable.create((emitter) -> {
            final OpenUserInfo userInfo = token3rdVerify.verify3rdToken(params.socialType, params.token);
            if (null == userInfo) {
                emitter.onError(new Auth3rdUserException("认证失败,请重试"));
                return;
            }

            final User3rd user3rd = user3rdRepository.get3rdUser(userInfo.getSocialType(), userInfo.getOpenId());
            if (user3rd != null) {
                if (user3rd.getUserId() > 0) {
                    final User user = userRepository.getUser(user3rd.getUserId());
                    if (user != null) {
                        emitter.onNext(tokenUtils.createToken(user.toPrincipal()));
                        emitter.onComplete();
                        return;
                    }
                }
            } else {
                user3rdRepository.create3rdUser(userInfo.to3rdUser());
            }

            emitter.onError(new Auth3rdNoUserBondException("用户未绑定"));
        });

    }

}
