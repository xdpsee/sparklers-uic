package com.zhenhui.demo.sparklers.domain.usecase;

import com.google.common.collect.Sets;
import com.zhenhui.demo.sparklers.TestBase;
import com.zhenhui.demo.sparklers.domain.exception.UserAlreadyExistException;
import com.zhenhui.demo.sparklers.domain.interactor.CreateUser;
import com.zhenhui.demo.sparklers.domain.interactor.CreateUser.Params;
import com.zhenhui.demo.sparklers.domain.repository.CaptchaRepository;
import com.zhenhui.demo.sparklers.domain.repository.UserRepository;
import io.reactivex.observers.TestObserver;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateUserTests extends TestBase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CaptchaRepository captchaRepository;

    @Test
    public void testCreate() {

        String captcha = captchaRepository.createCaptcha("18621816230", true);

        CreateUser createUser = new CreateUser(null, null, userRepository, captchaRepository);

        final TestObserver<Boolean> testObserver = new TestObserver<>();

        createUser.execute(new Params("18621816230", "12345678", Sets.newHashSet("USER"), captcha), testObserver);

        testObserver.assertResult(true).assertComplete();
    }

    @Test
    public void testDuplicate() {

        final TestObserver<Boolean> testObserver = new TestObserver<>();

        CreateUser createUser = new CreateUser(null, null, userRepository, captchaRepository);


        String captcha = captchaRepository.createCaptcha("18621816230", true);
        createUser.execute(new Params("18621816230", "12345678",Sets.newHashSet("USER"), captcha), testObserver);

        testObserver.assertResult(true).assertComplete();

        // again
        final TestObserver<Boolean> testObserver2 = new TestObserver<>();

        String captcha2 = captchaRepository.createCaptcha("18621816230", true);

        createUser.execute(new Params("18621816230", "12345678",Sets.newHashSet("USER"), captcha2), testObserver2);

        testObserver2.assertError(UserAlreadyExistException.class);

    }

}
