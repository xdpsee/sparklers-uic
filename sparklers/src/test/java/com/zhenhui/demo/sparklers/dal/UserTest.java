package com.zhenhui.demo.sparklers.dal;

import com.zhenhui.demo.sparklers.Application;
import com.zhenhui.demo.sparklers.dal.jooq.Tables;
import com.zhenhui.demo.sparklers.dal.jooq.tables.User;
import com.zhenhui.demo.sparklers.dal.jooq.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("SpringJavaAutowiringInspection")
@SpringBootTest(classes = Application.class)
@Transactional(transactionManager = "transactionManager")
@RunWith(SpringJUnit4ClassRunner.class)
public class UserTest {

    @Autowired
    DSLContext context;

    @Test
    public void testInsert() {

        UserRecord user = context.newRecord(Tables.USER);
        user.setName("HUI");
        user.setPhone("13402022080");
        user.setSecret("12345678");

        int rows = user.insert();

        assertThat(rows).isEqualTo(1);
    }

}
