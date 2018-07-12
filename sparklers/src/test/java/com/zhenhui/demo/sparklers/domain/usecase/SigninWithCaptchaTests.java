package com.zhenhui.demo.sparklers.domain.usecase;

import com.google.common.collect.Sets;
import com.zhenhui.demo.sparklers.TestBase;
import com.zhenhui.demo.sparklers.domain.exception.CaptchaExpireException;
import com.zhenhui.demo.sparklers.domain.exception.CaptchaMismatchException;
import com.zhenhui.demo.sparklers.domain.interactor.CreateUser;
import com.zhenhui.demo.sparklers.domain.interactor.SigninWithCaptcha;
import com.zhenhui.demo.sparklers.domain.repository.CaptchaRepository;
import com.zhenhui.demo.sparklers.domain.repository.UserRepository;
import com.zhenhui.demo.sparklers.security.TokenUtils;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SigninWithCaptchaTests extends TestBase {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private CaptchaRepository captchaRepository;


    @Before
    public void setup() throws Exception  {
        super.setup();

        TestObserver<Boolean> testObserver = new TestObserver<>();

        String captcha = captchaRepository.createCaptcha("13818886666", true);
        CreateUser createUser = new CreateUser(null, null, userRepository, captchaRepository);
        createUser.execute(new CreateUser.Params("13818886666", "12345678", Sets.newHashSet("USER"), captcha), testObserver);

        testObserver.assertResult(true).assertComplete();
    }

    @Test
    public void testCaptchaMismatch() {

        SigninWithCaptcha signinWithCaptcha = new SigninWithCaptcha(null
                , null
                , userRepository
                , tokenUtils
                , captchaRepository);

        TestObserver<String> testObserver = new TestObserver<>();
        signinWithCaptcha.execute(new SigninWithCaptcha.Params("13818886666", "----"), testObserver);

        testObserver.assertError(CaptchaMismatchException.class);

    }

    @Test
    public void testCaptchaExpired() {

        SigninWithCaptcha signinWithCaptcha = new SigninWithCaptcha(null
                , null
                , userRepository
                , tokenUtils
                , captchaRepository);

        String captcha = captchaRepository.createCaptcha("13818886666", true);
        captchaRepository.invalidCaptcha("13818886666");

        TestObserver<String> testObserver = new TestObserver<>();
        signinWithCaptcha.execute(new SigninWithCaptcha.Params("13818886666", captcha), testObserver);

        testObserver.assertError(CaptchaExpireException.class);

    }
}
