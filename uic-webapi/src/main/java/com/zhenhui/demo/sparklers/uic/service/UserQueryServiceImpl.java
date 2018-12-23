package com.zhenhui.demo.sparklers.uic.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Collections2;
import com.zhenhui.demo.sparklers.uic.domain.model.User;
import com.zhenhui.demo.sparklers.uic.domain.repository.UserRepository;
import com.zhenhui.demo.sparklers.utils.JsonUtils;
import com.zhenhui.demo.uic.api.domain.UserDto;
import com.zhenhui.demo.uic.api.service.UserQueryService;
import com.zhenhui.library.redis.cache.AbstractValue;
import com.zhenhui.library.redis.serializer.Serializer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection,unused")
@Service(interfaceClass = UserQueryService.class)
@Component
public class UserQueryServiceImpl implements UserQueryService {

    @Autowired
    private UserCache userCache;
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto queryUser(long userId) {
        UserDto user = userCache.get(userId);
        if (null == user) {
            User u = userRepository.getUser(userId);
            if (u != null) {
                user = new UserDto();
                BeanUtils.copyProperties(u, user);
                userCache.put(userId, user);
            }
        }

        return user;
    }

    @Override
    public Map<Long, UserDto> queryUsers(Collection<Long> userIds) {

        if (CollectionUtils.isEmpty(userIds)) {
            return new HashMap<>();
        }

        Map<Long, UserDto> result = userCache.get(userIds);

        Collection<Long> absentUserIds = Collections2.filter(userIds, id -> !result.containsKey(id));
        if (!absentUserIds.isEmpty()) {
            Map<Long, UserDto> absents;
            Collection<User> users = userRepository.getUsers(absentUserIds);
            if (!userIds.isEmpty()) {
                absents = users.stream().map(u -> {
                    UserDto r = new UserDto();
                    BeanUtils.copyProperties(u, r);
                    return r;
                }).collect(Collectors.toMap(UserDto::getId, Function.identity()));
                userCache.put(absents);
                result.putAll(absents);
            }
        }

        return result;
    }

    @Component
    public static class UserCache extends AbstractValue<Long, UserDto> {

        public UserCache(@Autowired Serializer<Long> keySerializer, @Autowired Serializer<UserDto> valueSerializer) {
            super("user-dto", keySerializer, valueSerializer, 3, TimeUnit.MINUTES);
        }
    }

    @Component
    public static class UserDtoSerializer implements Serializer<UserDto> {

        @Override
        public String serialize(UserDto userDto) {
            return JsonUtils.toJsonString(userDto);
        }

        @Override
        public UserDto deserialize(String s) {
            return JsonUtils.fromJsonString(s, new TypeReference<UserDto>() {
            });
        }
    }
}
