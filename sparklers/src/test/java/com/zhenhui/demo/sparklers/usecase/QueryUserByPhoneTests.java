package com.zhenhui.demo.sparklers.usecase;

import java.util.Optional;

import com.google.common.collect.Sets;
import com.zhenhui.demo.sparklers.Application;
import com.zhenhui.demo.sparklers.domain.interactor.CreateUser;
import com.zhenhui.demo.sparklers.domain.interactor.CreateUser.Params;
import com.zhenhui.demo.sparklers.domain.interactor.QueryUserByPhone;
import com.zhenhui.demo.sparklers.domain.model.User;
import com.zhenhui.demo.sparklers.domain.repository.UserRepository;
import io.reactivex.observers.TestObserver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@Transactional(transactionManager = "transactionManager")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class QueryUserByPhoneTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUserAbsent() {
        QueryUserByPhone queryUserByPhone = new QueryUserByPhone(null, null, userRepository);

        TestObserver<Optional<User>> testObserver = new TestObserver<>();
        queryUserByPhone.execute("13812340000", testObserver);

        testObserver.assertResult(Optional.empty()).assertComplete();
    }

    @Test
    public void testUserExist() {

        TestObserver<Boolean> createUserObserver = new TestObserver<>();

        CreateUser createUser = new CreateUser(null, null, userRepository);
        createUser.execute(new Params("13402022080", "12345678", Sets.newHashSet("USER")), createUserObserver);
        createUserObserver.assertResult(true).assertComplete();

        TestObserver<Optional<User>> queryUserObserver = new TestObserver<>();
        QueryUserByPhone queryUserByPhone = new QueryUserByPhone(null, null, userRepository);
        queryUserByPhone.execute("13402022080", queryUserObserver);
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
