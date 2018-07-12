package com.zhenhui.demo.sparklers.domain.usecase;

import com.google.common.collect.Sets;
import com.zhenhui.demo.sparklers.TestBase;
import com.zhenhui.demo.sparklers.domain.interactor.CreateUser;
import com.zhenhui.demo.sparklers.domain.interactor.CreateUser.Params;
import com.zhenhui.demo.sparklers.domain.interactor.QueryUserWithPhone;
import com.zhenhui.demo.sparklers.domain.model.User;
import com.zhenhui.demo.sparklers.domain.repository.CaptchaRepository;
import com.zhenhui.demo.sparklers.domain.repository.UserRepository;
import io.reactivex.observers.TestObserver;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class QueryUserWithPhoneTests extends TestBase {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CaptchaRepository captchaRepository;

    @Test
    public void testUserAbsent() {
        QueryUserWithPhone queryUserWithPhone = new QueryUserWithPhone(null, null, userRepository);

        TestObserver<Optional<User>> testObserver = new TestObserver<>();
        queryUserWithPhone.execute("13812340000", testObserver);

        testObserver.assertResult(Optional.empty()).assertComplete();
    }

    @Test
    public void testUserExist() {

        TestObserver<Boolean> createUserObserver = new TestObserver<>();

        CreateUser createUser = new CreateUser(null, null, userRepository, captchaRepository);

        String captcha = captchaRepository.createCaptcha("13402022080", true);
        createUser.execute(new Params("13402022080", "12345678", Sets.newHashSet("USER"), captcha), createUserObserver);
        createUserObserver.assertResult(true).assertComplete();

        TestObserver<Optional<User>> queryUserObserver = new TestObserver<>();
        QueryUserWithPhone queryUserWithPhone = new QueryUserWithPhone(null, null, userRepository);
        queryUserWithPhone.execute("13402022080", queryUserObserver);
        queryUserObserver.assertOf(observer -> {
            if (observer.valueCount() != 1) {
                throw new RuntimeException("size != 1");
            }

            Optional<User> user = observer.values().get(0);
            if (!user.isPresent()) {
                throw new IllegalStateException("user should be exist!");
            }
        });


    }

}
