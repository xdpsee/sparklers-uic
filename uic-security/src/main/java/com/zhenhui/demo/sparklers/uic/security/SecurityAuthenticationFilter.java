package com.zhenhui.demo.sparklers.uic.security;

import com.zhenhui.demo.sparklers.common.Error;
import com.zhenhui.demo.sparklers.common.Result;
import com.zhenhui.demo.sparklers.uic.security.domain.SecurityToken;
import com.zhenhui.demo.sparklers.uic.security.exception.ExpiresTokenException;
import com.zhenhui.demo.sparklers.uic.security.exception.InvalidTokenException;
import com.zhenhui.demo.sparklers.utils.JsonUtils;
import com.zhenhui.demo.uic.api.service.SecurityBlacklistService;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("unused")
public class SecurityAuthenticationFilter extends OncePerRequestFilter {

    private final String authHeader;

    private final SecurityTokenProducer tokenProducer;

    private final SecurityBlacklistService securityBlacklistService;

    public SecurityAuthenticationFilter(String authHeader
            , SecurityTokenProducer tokenProducer
            , SecurityBlacklistService securityBlacklistService) {
        this.authHeader = authHeader;
        this.tokenProducer = tokenProducer;
        this.securityBlacklistService = securityBlacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request
        , HttpServletResponse response
        , FilterChain chain) throws ServletException, IOException {

        final String token = parseToken(request);
        if (token != null && !securityBlacklistService.isBlocked(token)) {
            try {
                Subject subject = tokenProducer.parseToken(token);
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    SecurityToken authentication = new SecurityToken(subject);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    request.setAttribute("token", token);
                }
            } catch (Exception e) {
                Result result;
                if (e instanceof ExpiresTokenException) {
                    result = Result.error(Error.LOGIN_REQUIRED.error(), "令牌已过期,请重新登录");
                } else if (e instanceof InvalidTokenException) {
                    result = Result.error(Error.LOGIN_REQUIRED.error(), "无效令牌");
                } else {
                    result = Result.error(Error.APPLICATION_ERROR);
                }

                String content = JsonUtils.toJsonString(result);
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                response.getWriter().write(content);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private String parseToken(HttpServletRequest request) {

        String token = request.getHeader(authHeader);
        if (token != null) {
            return token;
        }

        return request.getParameter(authHeader);
    }

}



