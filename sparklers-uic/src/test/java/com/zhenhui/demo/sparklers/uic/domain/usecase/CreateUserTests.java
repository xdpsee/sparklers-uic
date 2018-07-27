package com.zhenhui.demo.sparklers.uic.domain.usecase;

import com.google.common.collect.Sets;
import com.zhenhui.demo.sparklers.uic.TestBase;
import com.zhenhui.demo.sparklers.uic.domain.exception.UserAlreadyExistException;
import com.zhenhui.demo.sparklers.uic.domain.interactor.CreateUser;
import com.zhenhui.demo.sparklers.uic.domain.interactor.CreateUser.Params;
import com.zhenhui.demo.sparklers.uic.domain.model.Captcha;
import com.zhenhui.demo.sparklers.uic.domain.repository.CaptchaRepository;
import com.zhenhui.demo.sparklers.uic.domain.repository.UserRepository;
import io.reactivex.observers.TestObserver;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateUserTests extends TestBase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CaptchaRepository captchaRepository;

    @Test
    public void testCreate() throws Exception {

        Captcha captcha = captchaRepository.createCaptcha("18621816230");

        CreateUser createUser = new CreateUser(null, null, userRepository, captchaRepository);

        final TestObserver<Boolean> testObserver = new TestObserver<>();

        createUser.execute(new Params("18621816230", "12345678", Sets.newHashSet("USER"), captcha.getCode()), testObserver);

        testObserver.assertResult(true).assertComplete();
    }

    @Test
    public void testDuplicate() throws Exception {

        final TestObserver<Boolean> testObserver = new TestObserver<>();

        CreateUser createUser = new CreateUser(null, null, userRepository, captchaRepository);


        Captcha captcha = captchaRepository.createCaptcha("18621816230");
        createUser.execute(new Params("18621816230", "12345678",Sets.newHashSet("USER"), captcha.getCode()), testObserver);

        testObserver.assertResult(true).assertComplete();

        // again
        final TestObserver<Boolean> testObserver2 = new TestObserver<>();

        Captcha captcha2 = captchaRepository.createCaptcha("18621816230");

        createUser.execute(new Params("18621816230", "12345678",Sets.newHashSet("USER"), captcha2.getCode()), testObserver2);

        testObserver2.assertError(UserAlreadyExistException.class);

    }

}
