package com.zhenhui.demo.sparklers.usecase;

import java.util.Optional;

import com.google.common.collect.Sets;
import com.zhenhui.demo.sparklers.Application;
import com.zhenhui.demo.sparklers.domain.interactor.CreateUser;
import com.zhenhui.demo.sparklers.domain.interactor.CreateUser.Params;
import com.zhenhui.demo.sparklers.domain.interactor.QueryUserById;
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

import static org.junit.Assert.assertNotNull;

@Transactional(transactionManager = "transactionManager")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class QueryUserByIdTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUserAbsent() {
        QueryUserById queryUserById = new QueryUserById(null, null, userRepository);

        TestObserver<Optional<User>> testObserver = new TestObserver<>();
        queryUserById.execute(1L, testObserver);

        testObserver.assertResult(Optional.empty()).assertComplete();
    }

    @Test
    public void testUserExist() {

        TestObserver<Boolean> createUserObserver = new TestObserver<>();

        CreateUser createUser = new CreateUser(null, null, userRepository);
        createUser.execute(new Params("13402022080", "12345678", Sets.newHashSet("USER")), createUserObserver);
        createUserObserver.assertResult(true).assertComplete();

        User user = userRepository.getUser("13402022080");
        assertNotNull(user);

        TestObserver<Optional<User>> queryUserObserver = new TestObserver<>();
        QueryUserById queryUserById = new QueryUserById(null, null, userRepository);
        queryUserById.execute(user.getId(), queryUserObserver);
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