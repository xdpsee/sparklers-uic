package com.zhenhui.demo.sparklers.uic.domain.usecase;

import com.google.common.collect.Sets;
import com.zhenhui.demo.sparklers.uic.TestBase;
import com.zhenhui.demo.sparklers.uic.domain.exception.BadSecretException;
import com.zhenhui.demo.sparklers.uic.domain.exception.UserNotFoundException;
import com.zhenhui.demo.sparklers.uic.domain.interactor.CreateUser;
import com.zhenhui.demo.sparklers.uic.domain.interactor.CreateUser.Params;
import com.zhenhui.demo.sparklers.uic.domain.interactor.SigninWithSecret;
import com.zhenhui.demo.sparklers.uic.domain.model.Captcha;
import com.zhenhui.demo.sparklers.uic.domain.repository.CaptchaRepository;
import com.zhenhui.demo.sparklers.uic.domain.repository.UserRepository;
import com.zhenhui.demo.sparklers.uic.security.SecurityTokenProducer;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SigninWithSecretTests extends TestBase {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CaptchaRepository captchaRepository;
    @Autowired
    private CreateUser createUser;
    @Autowired
    private SecurityTokenProducer tokenProducer;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Before
    public void setup() throws Exception {

        TestObserver<Boolean> testObserver = new TestObserver<>();
        Captcha captcha = captchaRepository.createCaptcha("18000001234");
        createUser.execute(new Params("18000001234", "12345678", Sets.newHashSet("USER"), captcha.getCode()), testObserver);
        testObserver.assertResult(true).assertComplete();
    }


    @Test
    public void testSigninNormal() {
        SigninWithSecret signinWithSecret = new SigninWithSecret(null
                , null
                , userRepository
                , tokenProducer
                , passwordEncoder);

        TestObserver<String> testObserver = new TestObserver<>();
        signinWithSecret.execute(new SigninWithSecret.Params("18000001234", "12345678"), testObserver);

        testObserver.assertOf(o -> {
            if (o.valueCount() != 1) {
                throw new IllegalStateException("");
            }

            o.assertComplete();
            o.assertNoErrors();
        });
    }

    @Test
    public void testSigninBadSecret() {
        SigninWithSecret signinWithSecret = new SigninWithSecret(null
                , null
                , userRepository
                , tokenProducer
                , passwordEncoder);

        TestObserver<String> testObserver = new TestObserver<>();
        signinWithSecret.execute(new SigninWithSecret.Params("18000001234", "12345670"), testObserver);

        testObserver.assertError(BadSecretException.class);
    }

    @Test
    public void testSigninUserAbsent() {
        SigninWithSecret signinWithSecret = new SigninWithSecret(null
                , null
                , userRepository
                , tokenProducer
                , passwordEncoder);

        TestObserver<String> testObserver = new TestObserver<>();
        signinWithSecret.execute(new SigninWithSecret.Params("1800000000", "12345678"), testObserver);

        testObserver.assertError(UserNotFoundException.class);
    }

}
