package com.zhenhui.demo.sparklers.uic.domain.usecase;

import com.google.common.collect.Sets;
import com.zhenhui.demo.sparklers.uic.TestBase;
import com.zhenhui.demo.sparklers.uic.domain.interactor.CreateUser;
import com.zhenhui.demo.sparklers.uic.domain.interactor.CreateUser.Params;
import com.zhenhui.demo.sparklers.uic.domain.interactor.QueryUserWithId;
import com.zhenhui.demo.sparklers.uic.domain.model.Captcha;
import com.zhenhui.demo.sparklers.uic.domain.model.User;
import com.zhenhui.demo.sparklers.uic.domain.repository.CaptchaRepository;
import com.zhenhui.demo.sparklers.uic.domain.repository.UserRepository;
import io.reactivex.observers.TestObserver;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;

public class QueryUserWithIdTests extends TestBase {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CaptchaRepository captchaRepository;

    @Test
    public void testUserAbsent() {
        QueryUserWithId queryUserWithId = new QueryUserWithId(null, null, userRepository);

        TestObserver<Optional<User>> testObserver = new TestObserver<>();
        queryUserWithId.execute(1L, testObserver);

        testObserver.assertResult(Optional.empty()).assertComplete();
    }

    @Test
    public void testUserExist() throws Exception {

        TestObserver<Boolean> createUserObserver = new TestObserver<>();

        CreateUser createUser = new CreateUser(null, null, userRepository, captchaRepository);

        Captcha captcha = captchaRepository.createCaptcha("13402022080");
        createUser.execute(new Params("13402022080", "12345678", Sets.newHashSet("USER"), captcha.getCode()), createUserObserver);
        createUserObserver.assertResult(true).assertComplete();

        User user = userRepository.getUser("13402022080");
        assertNotNull(user);

        TestObserver<Optional<User>> queryUserObserver = new TestObserver<>();
        QueryUserWithId queryUserWithId = new QueryUserWithId(null, null, userRepository);
        queryUserWithId.execute(user.getId(), queryUserObserver);
        queryUserObserver.assertOf(observer -> {
            if (observer.valueCount() != 1) {
                throw new RuntimeException("");
            }

            Optional<User> result = observer.values().get(0);
            if (!result.isPresent()) {
                throw new IllegalStateException("user should be exist!");
            }
        });
    }

}
