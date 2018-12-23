package com.zhenhui.demo.sparklers.uic.utils;

import com.zhenhui.demo.sparklers.common.Result;
import com.zhenhui.demo.sparklers.utils.JsonUtils;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;

public final class ResultUtils {

    public static void write(Result result, HttpServletResponse response) {

        try {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write(JsonUtils.toJsonString(result));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
