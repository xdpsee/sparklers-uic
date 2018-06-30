package com.zhenhui.demo.sparklers.domain.usecase;

import com.google.common.collect.Sets;
import com.zhenhui.demo.sparklers.Application;
import com.zhenhui.demo.sparklers.domain.exception.CaptchaExpireException;
import com.zhenhui.demo.sparklers.domain.exception.CaptchaMismatchException;
import com.zhenhui.demo.sparklers.domain.interactor.CreateUser;
import com.zhenhui.demo.sparklers.domain.interactor.SigninWithCaptcha;
import com.zhenhui.demo.sparklers.domain.repository.UserRepository;
import com.zhenhui.demo.sparklers.security.TokenUtils;
import com.zhenhui.demo.sparklers.service.CaptchaManager;
import io.reactivex.observers.TestObserver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;

@Transactional(transactionManager = "transactionManager")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SigninWithCaptchaTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CreateUser createUser;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private CaptchaManager captchaManager;

    @Before
    public void setup() {
        TestObserver<Boolean> testObserver = new TestObserver<>();
        createUser.execute(new CreateUser.Params("13818886666", "12345678", Sets.newHashSet("USER")), testObserver);
        testObserver.assertResult(true).assertComplete();
    }

    @After
    public void tearDown() {
        captchaManager.removeAll();
    }

    @Test
    public void testCaptchaExpires() {

        SigninWithCaptcha signinWithCaptcha = new SigninWithCaptcha(null
                , null
                , userRepository
                , tokenUtils
                , captchaManager);

        TestObserver<String> testObserver = new TestObserver<>();
        signinWithCaptcha.execute(new SigninWithCaptcha.Params("13818886666", "1234"), testObserver);

        testObserver.assertError(CaptchaExpireException.class);

    }

    @Test
    public void testCaptchaMismatch() {

        SigninWithCaptcha signinWithCaptcha = new SigninWithCaptcha(null
                , null
                , userRepository
                , tokenUtils
                , captchaManager);

        String excepted = captchaManager.createCaptcha("13818886666", true);
        assertNotNull(excepted);

        TestObserver<String> testObserver = new TestObserver<>();
        signinWithCaptcha.execute(new SigninWithCaptcha.Params("13818886666", "????"), testObserver);

        testObserver.assertError(CaptchaMismatchException.class);

    }
}
