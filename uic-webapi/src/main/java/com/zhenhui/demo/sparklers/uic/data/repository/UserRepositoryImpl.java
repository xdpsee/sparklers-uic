package com.zhenhui.demo.sparklers.uic.data.repository;

import com.zhenhui.demo.sparklers.data.jooq.Tables;
import com.zhenhui.demo.sparklers.data.jooq.tables.records.UserRecord;
import com.zhenhui.demo.sparklers.uic.domain.model.User;
import com.zhenhui.demo.sparklers.uic.domain.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static com.zhenhui.demo.sparklers.data.jooq.tables.User.USER;

@Component
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private DSLContext context;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Cacheable(cacheNames = "users", key = "#userId", unless = "#result==null") // userId
    @Override
    public User getUser(long userId) {

        final UserRecord record = context.selectFrom(Tables.USER)
                .where(Tables.USER.ID.eq(userId))
                .fetchOneInto(UserRecord.class);

        return record != null ? fromRecord(record) : null;
    }

    @Override
    @Cacheable(cacheNames = "users", key = "#phone", unless = "#result==null") // phone
    public User getUser(String phone) {
        final UserRecord record = context.selectFrom(Tables.USER)
                .where(Tables.USER.PHONE.eq(phone))
                .fetchOneInto(UserRecord.class);

        return record != null ? fromRecord(record) : null;
    }

    @Override
    @Cacheable(cacheNames = "users", keyGenerator = "userIdsKeyGenerator", unless = "#result.isEmpty()")
    public Collection<User> getUsers(Collection<Long> userIds) {
        final List<UserRecord> records = context.selectFrom(Tables.USER)
                .where(Tables.USER.ID.in(userIds))
                .fetchInto(UserRecord.class);

        return records != null ? records.stream().map(this::fromRecord).collect(Collectors.toList()) : new ArrayList<>();
    }

    @Override
    public boolean createUser(String phone, String secret, Set<String> authorities) {
        int rows = context.insertInto(USER)
                .set(USER.PHONE, phone)
                .set(USER.SECRET, passwordEncoder.encode(secret))
                .set(USER.NAME, "")
                .set(USER.AVATAR, "")
                .set(USER.AUTHORITIES, String.join(",", authorities))
                .execute();
        return rows == 1;
    }

    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "users", key = "#phone"),
                    @CacheEvict(cacheNames = "users", key = "#result.id", condition = "#result!=null"),
            }
    )
    @Transactional(rollbackFor = Exception.class)
    @Override
    public User updateSecret(String phone, String secret) {

        final int rows = context.update(USER)
                .set(USER.SECRET, passwordEncoder.encode(secret))
                .where(USER.PHONE.eq(phone))
                .execute();
        if (rows == 1) {
            return fromRecord(context.selectFrom(Tables.USER)
                    .where(Tables.USER.PHONE.eq(phone))
                    .fetchOneInto(UserRecord.class));
        }

        return null;
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

    @Component("userIdsKeyGenerator")
    public static class UserIdsKeyGenerator implements KeyGenerator {

        @Override
        public Object generate(Object obj, Method method, Object... objects) {

            Collection<Long> userIds = (Collection<Long>) objects[0];

            return "s-" + StringUtils.join(userIds, ",");
        }
    }
}


