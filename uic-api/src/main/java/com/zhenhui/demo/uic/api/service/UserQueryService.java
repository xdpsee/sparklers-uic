package com.zhenhui.demo.uic.api.service;

import com.zhenhui.demo.uic.api.domain.UserDto;

import java.util.Collection;
import java.util.Map;

public interface UserQueryService {

    UserDto queryUser(long userId);

    Map<Long, UserDto> queryUsers(Collection<Long> userIds);

}
