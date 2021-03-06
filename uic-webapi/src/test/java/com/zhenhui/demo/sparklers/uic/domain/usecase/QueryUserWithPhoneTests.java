package com.zhenhui.demo.sparklers.uic.domain.usecase;

import com.google.common.collect.Sets;
import com.zhenhui.demo.sparklers.uic.TestBase;
import com.zhenhui.demo.sparklers.uic.domain.interactor.CreateUser;
import com.zhenhui.demo.sparklers.uic.domain.interactor.CreateUser.Params;
import com.zhenhui.demo.sparklers.uic.domain.interactor.QueryUserWithPhone;
import com.zhenhui.demo.sparklers.uic.domain.model.Captcha;
import com.zhenhui.demo.sparklers.uic.domain.model.User;
import com.zhenhui.demo.sparklers.uic.domain.repository.CaptchaRepository;
import com.zhenhui.demo.sparklers.uic.domain.repository.UserRepository;
import com.zhenhui.demo.sparklers.uic.utils.CaptchaUtil;
import io.reactivex.observers.TestObserver;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class QueryUserWithPhoneTests extends TestBase {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CaptchaRepository captchaRepository;
    @Autowired
    private CaptchaUtil captchaUtil;

    @Test
    public void testUserAbsent() {
        QueryUserWithPhone queryUserWithPhone = new QueryUserWithPhone(null, null, userRepository);

        TestObserver<Optional<User>> testObserver = new TestObserver<>();
        queryUserWithPhone.execute("13812340000", testObserver);

        testObserver.assertResult(Optional.empty()).assertComplete();
    }

    @Test
    public void testUserExist() throws Exception {

        TestObserver<Boolean> createUserObserver = new TestObserver<>();

        CreateUser createUser = new CreateUser(null, null, userRepository, captchaUtil);

        Captcha captcha = captchaRepository.createCaptcha("13402022080");
        createUser.execute(new Params("13402022080", "12345678", Sets.newHashSet("USER"), captcha.getCode()), createUserObserver);
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
