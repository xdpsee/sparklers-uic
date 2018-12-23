package com.zhenhui.demo.sparklers.uic.data.th3rd;

import com.zhenhui.demo.uic.api.enums.SocialType;
import com.zhenhui.demo.sparklers.uic.domain.th3rd.OpenUserInfo;
import com.zhenhui.demo.sparklers.uic.utils.ExceptionUtils;
import com.zhenhui.demo.sparklers.utils.JsonUtils;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class WeiboTokenInfoRequest implements TokenInfoRequest {

    private static final Logger logger = LoggerFactory.getLogger(WeiboTokenInfoRequest.class);

    private static final String URL_FORMAT = "https://api.weibo.com/2/users/show.json?access_token=%s";

    @Override
    public OpenUserInfo execute(String token) {

        Request request = new Request.Builder()
                .get()
                .url(String.format(URL_FORMAT, token))
                .build();

        Call call = httpClient.newCall(request);
        try {
            Response response = call.execute();
            if (response.code() != 200) {
                logger.error("WeiboTokenInfoRequest.execute response code = " + response.code());
                return null;
            }

            Map<String, Object> result = JsonUtils.toMap(response.body().toString());

            OpenUserInfo userInfo = new OpenUserInfo();
            userInfo.setSocialType(SocialType.WEIBO);
            userInfo.setOpenId(result.get("id").toString());
            userInfo.setNickname(result.get("screen_name").toString());
            userInfo.setAvatar(result.get("avatar_large").toString());

            return userInfo;

        } catch (IOException e) {
            logger.error("WeiboTokenInfoRequest.execute exception code = " + ExceptionUtils.getStackTrace(e));
        }

        return null;
    }

}
