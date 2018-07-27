package com.zhenhui.demo.sparklers.uic.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhenhui.demo.sparklers.uic.common.Result;
import com.zhenhui.demo.sparklers.uic.common.Error;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
        throws IOException, ServletException {
        Result.newBuilder()
            .error(Error.PERMISSION_DENIED)
            .message("无数据访问权限")
            .write(response);

    }
}

