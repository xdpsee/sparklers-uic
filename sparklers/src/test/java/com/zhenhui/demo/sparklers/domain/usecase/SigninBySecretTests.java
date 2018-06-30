package com.zhenhui.demo.sparklers.domain.usecase;

import com.google.common.collect.Sets;
import com.zhenhui.demo.sparklers.Application;
import com.zhenhui.demo.sparklers.domain.exception.BadSecretException;
import com.zhenhui.demo.sparklers.domain.exception.UserNotFoundException;
import com.zhenhui.demo.sparklers.domain.interactor.CreateUser;
import com.zhenhui.demo.sparklers.domain.interactor.CreateUser.Params;
import com.zhenhui.demo.sparklers.domain.interactor.SigninBySecret;
import com.zhenhui.demo.sparklers.domain.repository.UserRepository;
import com.zhenhui.demo.sparklers.security.TokenUtils;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@Transactional(transactionManager = "transactionManager")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SigninBySecretTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CreateUser createUser;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Before
    public void setup() {
        TestObserver<Boolean> testObserver = new TestObserver<>();
        createUser.execute(new Params("18000001234", "12345678", Sets.newHashSet("USER")), testObserver);
        testObserver.assertResult(true).assertComplete();
    }

    @Test
    public void testSigninNormal() {
        SigninBySecret signinBySecret = new SigninBySecret(null
            , null
            , userRepository
            , tokenUtils
            , passwordEncoder);

        TestObserver<String> testObserver = new TestObserver<>();
        signinBySecret.execute(new SigninBySecret.Params("18000001234", "12345678"), testObserver);

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
        SigninBySecret signinBySecret = new SigninBySecret(null
            , null
            , userRepository
            , tokenUtils
            , passwordEncoder);

        TestObserver<String> testObserver = new TestObserver<>();
        signinBySecret.execute(new SigninBySecret.Params("18000001234", "12345670"), testObserver);

        testObserver.assertError(BadSecretException.class);
    }

    @Test
    public void testSigninUserAbsent() {
        SigninBySecret signinBySecret = new SigninBySecret(null
            , null
            , userRepository
            , tokenUtils
            , passwordEncoder);

        TestObserver<String> testObserver = new TestObserver<>();
        signinBySecret.execute(new SigninBySecret.Params("1800000000", "12345678"), testObserver);

        testObserver.assertError(UserNotFoundException.class);
    }

}
