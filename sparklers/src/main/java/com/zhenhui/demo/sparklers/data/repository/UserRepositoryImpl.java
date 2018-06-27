package com.zhenhui.demo.sparklers.data.repository;

import java.util.HashSet;
import java.util.Optional;

import com.zhenhui.demo.sparklers.dal.jooq.Tables;
import com.zhenhui.demo.sparklers.dal.jooq.tables.records.UserRecord;
import com.zhenhui.demo.sparklers.domain.model.User;
import com.zhenhui.demo.sparklers.domain.repository.UserRepository;
import io.reactivex.Observable;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private DSLContext context;

    @Override
    public Observable<Optional<User>> getUser(long userId) {

        return Observable.create((emitter) -> {
            try {
                final UserRecord record = context.selectFrom(Tables.USER)
                    .where(Tables.USER.ID.eq(userId))
                    .fetchOneInto(UserRecord.class);

                User user = null;
                if (record != null) {
                    user = new User(1L, record.getName(), "", new HashSet<>());
                }

                emitter.onNext(Optional.ofNullable(user));
            } catch (Exception e) {
                emitter.onError(e);
            }
        });

    }
}




