package com.zhenhui.demo.uic.api.service;

import com.zhenhui.demo.uic.api.domain.UserDto;

import java.util.Map;
import java.util.Set;

public interface UserQueryService {

    UserDto queryUser(long userId);

    Map<Long, UserDto> queryUsers(Set<Long> userIds);

}
