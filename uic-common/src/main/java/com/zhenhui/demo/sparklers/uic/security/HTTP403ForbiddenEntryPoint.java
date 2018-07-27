package com.zhenhui.demo.sparklers.uic.security;

import com.zhenhui.demo.sparklers.uic.common.Error;
import com.zhenhui.demo.sparklers.uic.common.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HTTP403ForbiddenEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        Result.newBuilder()
                .error(Error.PERMISSION_DENIED)
                .message("无数据访问权限")
                .write(response);
    }
}
