package com.zhenhui.demo.sparklers.uic.data.th3rd;

import com.zhenhui.demo.sparklers.uic.common.SocialType;
import com.zhenhui.demo.sparklers.uic.domain.th3rd.OpenUserInfo;
import com.zhenhui.demo.sparklers.uic.domain.th3rd.Token3rdVerify;
import org.springframework.stereotype.Component;

@Component
public class Token3rdVerifyImpl implements Token3rdVerify {

    @Override
    public OpenUserInfo verify3rdToken(SocialType socialType, String token) {

        switch (socialType) {
            case WEIBO:
                return new WeiboTokenInfoRequest().execute(token);
            default:
                break;

        }

        throw new UnsupportedOperationException("Unsupported SocialType, " + socialType.name());
    }
}
