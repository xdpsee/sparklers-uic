package com.zhenhui.demo.sparklers.uic.data.th3rd;

import com.zhenhui.demo.sparklers.uic.domain.th3rd.OpenUserInfo;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public interface TokenInfoRequest {

    OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(3, TimeUnit.SECONDS)
            .writeTimeout(3, TimeUnit.SECONDS)
            .build();

    OpenUserInfo execute(String token);

}
