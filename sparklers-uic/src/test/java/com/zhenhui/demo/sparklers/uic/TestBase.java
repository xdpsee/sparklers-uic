package com.zhenhui.demo.sparklers.uic;

import com.zhenhui.demo.sparklers.uic.Application;
import org.apache.curator.test.TestingServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import redis.embedded.RedisServer;

@Transactional(transactionManager = "transactionManager")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class TestBase {

    private static RedisServer redisServer;

    private static TestingServer zooKeeperServer;

    @BeforeClass
    public static void setupBeforeClass() throws Exception {
        zooKeeperServer = new TestingServer(12181);
        zooKeeperServer.start();

        redisServer = new RedisServer(9736);
        redisServer.start();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        zooKeeperServer.stop();
        zooKeeperServer = null;
        redisServer.stop();
        redisServer = null;
    }

    @Test
    public void test() {

    }
}
