package com.zhenhui.demo.sparklers.uic.data.repository;

import com.zhenhui.demo.sparklers.uic.common.SocialType;
import com.zhenhui.demo.sparklers.uic.domain.model.User3rd;
import com.zhenhui.demo.sparklers.uic.domain.repository.User3rdRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class User3rdRepositoryImpl implements User3rdRepository {

    @Override
    public User3rd create3rdUser(User3rd user3rd) {
        return null;
    }

    @Override
    public User3rd get3rdUser(SocialType type, String openId) {
        return null;
    }

    @Override
    public List<User3rd> get3rdUsers(long userId) {
        return null;
    }
}
