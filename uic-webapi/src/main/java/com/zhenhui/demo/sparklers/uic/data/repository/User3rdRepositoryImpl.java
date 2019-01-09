package com.zhenhui.demo.sparklers.uic.data.repository;

import com.zhenhui.demo.sparklers.data.jooq.tables.records.User3rdRecord;
import com.zhenhui.demo.uic.api.enums.SocialType;
import com.zhenhui.demo.sparklers.uic.domain.model.User3rd;
import com.zhenhui.demo.sparklers.uic.domain.repository.User3rdRepository;
import com.zhenhui.demo.sparklers.uic.utils.ExceptionUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.zhenhui.demo.sparklers.data.jooq.tables.User3rd.USER3RD;

@Component
public class User3rdRepositoryImpl implements User3rdRepository {

    @Autowired
    private DSLContext context;

    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "3users", key = "#user3rd.type+#user3rd.openId"),
                    @CacheEvict(cacheNames = "3users", key = "#user3rd.userId")

            }
    )
    @Override
    public void create3rdUser(User3rd user3rd) {

        try {
            context.insertInto(USER3RD)
                    .set(USER3RD.TYPE, user3rd.getType().name())
                    .set(USER3RD.OPEN_ID, user3rd.getOpenId())
                    .set(USER3RD.NICKNAME, user3rd.getNickname())
                    .set(USER3RD.AVATAR, user3rd.getAvatar())
                    .set(USER3RD.USER_ID, user3rd.getUserId())
                    .execute();
        } catch (Exception e) {
            if (!ExceptionUtils.hasDuplicateEntryException(e)) {
                throw e;
            }
        }
    }

    @Cacheable(cacheNames = "3users", key = "#type+#openId", unless = "#result==null")
    @Override
    public User3rd get3rdUser(SocialType type, String openId) {

        User3rdRecord record = context.selectFrom(USER3RD)
                .where(USER3RD.TYPE.eq(type.name()))
                .and(USER3RD.OPEN_ID.eq(openId))
                .fetchOneInto(User3rdRecord.class);

        if (null == record) {
            return null;
        }

        return new User3rd(SocialType.valueOf(record.getType()), record.getOpenId(), record.getNickname(), record.getAvatar(), record.getUserId());
    }

    @Cacheable(cacheNames = "3users", key = "#userId", unless = "#result==null")
    @Override
    public List<User3rd> get3rdUsers(long userId) {

        return context.selectFrom(USER3RD)
                .where(USER3RD.USER_ID.eq(userId))
                .fetchInto(User3rdRecord.class)
                .stream()
                .map(e -> new User3rd(SocialType.valueOf(e.getType()), e.getOpenId(), e.getNickname(), e.getAvatar(), e.getUserId()))
                .collect(Collectors.toList());
    }

}
