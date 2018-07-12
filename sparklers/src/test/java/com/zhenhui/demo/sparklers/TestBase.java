package com.zhenhui.demo.sparklers;

import ai.grakn.redismock.RedisServer;
import org.apache.curator.test.TestingServer;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional(transactionManager = "transactionManager")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class TestBase {

    private RedisServer redisServer;

    private static TestingServer zooKeeperServer;

    @BeforeClass
    public static void setupBeforeClass() throws Exception {
        zooKeeperServer = new TestingServer(12181);
        zooKeeperServer.start();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        zooKeeperServer.stop();
        zooKeeperServer = null;
    }

    @Before
    public void setup() throws Exception {
        redisServer = RedisServer.newRedisServer(9736);
        redisServer.start();
    }

    @After
    public void tearDown() throws Exception {
        redisServer.stop();
        redisServer = null;
    }

    @Test
    public void test() {

    }
}
