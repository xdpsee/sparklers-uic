package com.zhenhui.demo.sparklers.domain.usecase;

import com.google.common.collect.Sets;
import com.zhenhui.demo.sparklers.Application;
import com.zhenhui.demo.sparklers.domain.exception.UserAlreadyExistException;
import com.zhenhui.demo.sparklers.domain.interactor.CreateUser;
import com.zhenhui.demo.sparklers.domain.interactor.CreateUser.Params;
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
public class CreateUserTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreate() {

        CreateUser createUser = new CreateUser(null, null, userRepository);

        final TestObserver<Boolean> testObserver = new TestObserver<>();

        createUser.execute(new Params("18621816230", "12345678", Sets.newHashSet("USER")), testObserver);

        testObserver.assertResult(true).assertComplete();

    }

    @Test
    public void testDuplicate() {
        CreateUser createUser = new CreateUser(null, null, userRepository);

        final TestObserver<Boolean> testObserver = new TestObserver<>();

        createUser.execute(new Params("18621816230", "12345678",Sets.newHashSet("USER")), testObserver);

        testObserver.assertResult(true).assertComplete();

        // again
        final TestObserver<Boolean> testObserver2 = new TestObserver<>();

        createUser.execute(new Params("18621816230", "12345678",Sets.newHashSet("USER")), testObserver2);

        testObserver2.assertError(UserAlreadyExistException.class);

    }

}
