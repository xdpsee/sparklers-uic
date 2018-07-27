package com.zhenhui.demo.sparklers.uic.domain.th3rd;

import com.zhenhui.demo.sparklers.uic.common.SocialType;
import com.zhenhui.demo.sparklers.uic.domain.model.User3rd;
import lombok.Data;

@Data
public class OpenUserInfo {

    private SocialType socialType;

    private String openId;

    private String nickname;

    private String avatar;

    public User3rd to3rdUser() {
        return new User3rd(socialType, openId, nickname, avatar, 0L);
    }
}

