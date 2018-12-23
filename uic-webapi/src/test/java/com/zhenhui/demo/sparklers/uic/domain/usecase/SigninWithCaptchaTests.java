package com.zhenhui.demo.sparklers.uic.domain.usecase;

import com.google.common.collect.Sets;
import com.zhenhui.demo.sparklers.uic.TestBase;
import com.zhenhui.demo.sparklers.uic.domain.exception.CaptchaMismatchException;
import com.zhenhui.demo.sparklers.uic.domain.exception.CaptchaNotFoundException;
import com.zhenhui.demo.sparklers.uic.domain.interactor.CreateUser;
import com.zhenhui.demo.sparklers.uic.domain.interactor.SigninWithCaptcha;
import com.zhenhui.demo.sparklers.uic.domain.model.Captcha;
import com.zhenhui.demo.sparklers.uic.domain.repository.CaptchaRepository;
import com.zhenhui.demo.sparklers.uic.domain.repository.UserRepository;
import com.zhenhui.demo.sparklers.uic.security.SecurityTokenProducer;
import com.zhenhui.demo.sparklers.uic.utils.CaptchaUtil;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SigninWithCaptchaTests extends TestBase {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecurityTokenProducer tokenProducer;
    @Autowired
    private CaptchaRepository captchaRepository;
    @Autowired
    private CaptchaUtil captchaUtil;


    @Before
    public void setup() throws Exception  {

        TestObserver<Boolean> testObserver = new TestObserver<>();

        Captcha captcha = captchaRepository.createCaptcha("13818886666");
        CreateUser createUser = new CreateUser(null, null, userRepository, captchaUtil);
        createUser.execute(new CreateUser.Params("13818886666", "12345678", Sets.newHashSet("USER"), captcha.getCode()), testObserver);

        testObserver.assertResult(true).assertComplete();
    }

    @Test
    public void testCaptchaNotFound() {

        SigninWithCaptcha signinWithCaptcha = new SigninWithCaptcha(null
                , null
                , userRepository
                , tokenProducer
                , captchaUtil);

        TestObserver<String> testObserver = new TestObserver<>();
        signinWithCaptcha.execute(new SigninWithCaptcha.Params("13818886666", "----"), testObserver);

        testObserver.assertError(CaptchaNotFoundException.class);

    }

    @Test
    public void testCaptchaMismatch() throws Exception {

        SigninWithCaptcha signinWithCaptcha = new SigninWithCaptcha(null
                , null
                , userRepository
                , tokenProducer
                , captchaUtil);

        captchaRepository.createCaptcha("13818886666");

        TestObserver<String> testObserver = new TestObserver<>();
        signinWithCaptcha.execute(new SigninWithCaptcha.Params("13818886666", "????"), testObserver);

        testObserver.assertError(CaptchaMismatchException.class);

    }
}
