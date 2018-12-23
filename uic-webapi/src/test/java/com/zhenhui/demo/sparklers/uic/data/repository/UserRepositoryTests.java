package com.zhenhui.demo.sparklers.uic.data.repository;

import com.zhenhui.demo.sparklers.uic.TestBase;
import com.zhenhui.demo.sparklers.data.jooq.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.zhenhui.demo.sparklers.data.jooq.tables.User.USER;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("SpringJavaAutowiringInspection")
public class UserRepositoryTests extends TestBase {

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
