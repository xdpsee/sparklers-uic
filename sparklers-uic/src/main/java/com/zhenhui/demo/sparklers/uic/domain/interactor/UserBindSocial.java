package com.zhenhui.demo.sparklers.uic.domain.interactor;

import com.google.common.collect.Sets;
import com.zhenhui.demo.sparklers.uic.common.SocialType;
import com.zhenhui.demo.sparklers.uic.domain.exception.Auth3rdUserException;
import com.zhenhui.demo.sparklers.uic.domain.exception.CaptchaExpireException;
import com.zhenhui.demo.sparklers.uic.domain.exception.CaptchaMismatchException;
import com.zhenhui.demo.sparklers.uic.domain.exception.CaptchaNotFoundException;
import com.zhenhui.demo.sparklers.uic.domain.executor.PostExecutionThread;
import com.zhenhui.demo.sparklers.uic.domain.executor.ThreadExecutor;
import com.zhenhui.demo.sparklers.uic.domain.model.Captcha;
import com.zhenhui.demo.sparklers.uic.domain.model.User;
import com.zhenhui.demo.sparklers.uic.domain.model.User3rd;
import com.zhenhui.demo.sparklers.uic.domain.repository.CaptchaRepository;
import com.zhenhui.demo.sparklers.uic.domain.repository.User3rdRepository;
import com.zhenhui.demo.sparklers.uic.domain.repository.UserRepository;
import com.zhenhui.demo.sparklers.uic.domain.th3rd.OpenUserInfo;
import com.zhenhui.demo.sparklers.uic.domain.th3rd.Token3rdVerify;
import com.zhenhui.demo.sparklers.uic.security.TokenUtils;
import io.reactivex.Observable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserBindSocial extends UseCase<UserBindSocial.Params, String> {

    private final UserRepository userRepository;

    private final CaptchaRepository captchaRepository;

    private final TokenUtils tokenUtils;

    private final Token3rdVerify token3rdVerify;

    private final User3rdRepository user3rdRepository;

    private final static RandomStringGenerator generator = new RandomStringGenerator.Builder()
            .withinRange('0', 'z').build();

    @Autowired
    public UserBindSocial(ThreadExecutor threadExecutor,
                          PostExecutionThread postExecutionThread,
                          UserRepository userRepository,
                          CaptchaRepository captchaRepository,
                          TokenUtils tokenUtils,
                          Token3rdVerify token3rdVerify,
                          User3rdRepository user3rdRepository) {
        super(threadExecutor, postExecutionThread);
        this.userRepository = userRepository;
        this.captchaRepository = captchaRepository;
        this.tokenUtils = tokenUtils;
        this.token3rdVerify = token3rdVerify;
        this.user3rdRepository = user3rdRepository;
    }

    @Override
    Observable<String> buildObservable(Params params) {

        return Observable.create((emitter) -> {
            try {
                verifyCaptcha(params.phone, params.captcha);
                final User3rd user3rd = getUser3rd(params.type, params.token);

                User user = userRepository.getUser(user3rd.getUserId());
                if (null == user) {
                    user = fastCreateUser(params.phone);
                } else if (!user.getPhone().equals(params.phone)) {
                    throw new Auth3rdUserException("改账号已被另一手机绑定");
                }

                captchaRepository.invalidCaptcha(params.phone, params.captcha);

                emitter.onNext(tokenUtils.createToken(user.toPrincipal()));
                emitter.onComplete();

            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    private void verifyCaptcha(String phone, String code) throws CaptchaNotFoundException
            , CaptchaExpireException, CaptchaMismatchException {
        final Captcha captcha = captchaRepository.lookupCaptcha(phone);
        if (null == captcha) {
            throw new CaptchaNotFoundException(code);
        }

        if (captcha.getExpireAt() <= System.currentTimeMillis()) {
            throw new CaptchaExpireException(code);
        }

        if (!captcha.getCode().equals(code)) {
            throw new CaptchaMismatchException();
        }
    }

    private User3rd getUser3rd(SocialType type, String token) throws Auth3rdUserException {
        OpenUserInfo userInfo = token3rdVerify.verify3rdToken(type, token);
        if (null == userInfo) {
            throw new Auth3rdUserException("第三方用户验证失败,请重试");
        }

        final User3rd user3rd = user3rdRepository.get3rdUser(type, userInfo.getOpenId());
        if (user3rd == null) {
            throw new Auth3rdUserException("未知错误,请重试");
        }

        return user3rd;
    }

    private User fastCreateUser(String phone) {

        boolean success = userRepository.createUser(phone, generator.generate(8), Sets.newHashSet("USER"));
        if (success) {
            return userRepository.getUser(phone);
        }

        return null;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Params {
        private SocialType type;
        private String token;
        private String phone;
        private String captcha;
    }

}
