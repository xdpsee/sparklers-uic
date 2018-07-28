package com.zhenhui.demo.sparklers.uic.domain.repository;

import com.zhenhui.demo.sparklers.uic.common.SocialType;
import com.zhenhui.demo.sparklers.uic.domain.model.User3rd;

import java.util.List;

public interface User3rdRepository {

    void create3rdUser(User3rd user3rd);

    User3rd get3rdUser(SocialType type, String openId);

    List<User3rd> get3rdUsers(long userId);

}
