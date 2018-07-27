package com.zhenhui.demo.sparklers.uic.data.repository;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.zhenhui.demo.sparklers.data.jooq.Tables;
import com.zhenhui.demo.sparklers.data.jooq.tables.records.UserRecord;
import com.zhenhui.demo.sparklers.uic.domain.model.User;
import com.zhenhui.demo.sparklers.uic.domain.repository.UserRepository;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.zhenhui.demo.sparklers.data.jooq.tables.User.USER;
import static org.jooq.impl.DSL.exists;

@Component
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private DSLContext context;

    @Override
    public User getUser(long userId) {

        final UserRecord record = context.selectFrom(Tables.USER)
                .where(Tables.USER.ID.eq(userId))
                .fetchOneInto(UserRecord.class);

        return record != null ? fromRecord(record) : null;
    }

    @Override
    public User getUser(String phone) {
        final UserRecord record = context.selectFrom(Tables.USER)
                .where(Tables.USER.PHONE.eq(phone))
                .fetchOneInto(UserRecord.class);

        return record != null ? fromRecord(record) : null;
    }

    @Override
    public boolean userExists(long userId) {
        return context.fetchExists(Tables.USER, Tables.USER.ID.eq(userId));
    }

    @Override
    public boolean createUser(String phone, String secret, Set<String> authorities) {
        int rows = context.insertInto(USER)
                .set(USER.PHONE, phone)
                .set(USER.SECRET, secret)
                .set(USER.NAME, "")
                .set(USER.AVATAR, "")
                .set(USER.AUTHORITIES, String.join(",", authorities))
                .execute();
        return rows == 1;
    }

    private User fromRecord(UserRecord record) {

        if (record != null) {
            return new User(record.getId()
                    , record.getPhone()
                    , record.getName()
                    , record.getSecret()
                    , record.getAvatar()
                    , Arrays.stream(record.getAuthorities().split(",")).collect(Collectors.toSet()));
        }

        return null;
    }
}

