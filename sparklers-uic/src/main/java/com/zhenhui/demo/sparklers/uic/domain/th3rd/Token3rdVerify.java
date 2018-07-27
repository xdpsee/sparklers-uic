package com.zhenhui.demo.sparklers.uic.domain.th3rd;

import com.zhenhui.demo.sparklers.uic.common.SocialType;

public interface Token3rdVerify {

    OpenUserInfo verify3rdToken(SocialType socialType, String token);

}


