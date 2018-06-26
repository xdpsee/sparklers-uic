package com.zhenhui.demo.sparklers.dal;

import com.zhenhui.demo.sparklers.Application;
import com.zhenhui.demo.sparklers.dal.jooq.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.zhenhui.demo.sparklers.dal.jooq.tables.User.USER;
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

        int rows = context.insertInto(USER)
                .set(USER.PHONE, "13402022080")
                .set(USER.SECRET, "12345678")
                .set(USER.NAME, "HUI")
                .execute();

        assertThat(rows).isEqualTo(1);

        UserRecord record = context.newRecord(USER);
        record.setPhone("18699089878");
        record.setSecret("123456");
        record.setName("Jerry");

        rows = context.insertInto(USER).set(record).execute();
        assertThat(rows).isEqualTo(1);

        List<UserRecord> users = context.selectFrom(USER).fetchInto(UserRecord.class);

        assertThat(users).isNotEmpty().hasSize(2);

    }

}
