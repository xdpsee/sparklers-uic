package com.zhenhui.demo.sparklers.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhenhui.demo.sparklers.data.repository.BlacklistRepositoryImpl;
import com.zhenhui.demo.sparklers.security.exception.ExpiresTokenException;
import com.zhenhui.demo.sparklers.security.exception.InvalidTokenException;
import com.zhenhui.demo.sparklers.service.results.Error;
import com.zhenhui.demo.sparklers.service.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JsonWebTokenAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.http-header}")
    private String authHeader;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private BlacklistRepositoryImpl blacklistRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request
        , HttpServletResponse response
        , FilterChain chain) throws ServletException, IOException {

        final String token = parseToken(request);
        if (token != null && !blacklistRepository.isBlocked(token)) {
            try {
                Principal principal = tokenUtils.parseToken(token);
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    JsonWebTokenAuthentication authentication = new JsonWebTokenAuthentication(principal);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    request.setAttribute("token", token);
                }
            } catch (Exception e) {
                if (e instanceof ExpiresTokenException) {
                    Result.newBuilder().error(Error.LOGIN_REQUIRED).message("令牌已过期,请重新登录").write(response);
                } else if (e instanceof InvalidTokenException) {
                    Result.newBuilder().error(Error.LOGIN_REQUIRED).message("无效令牌").write(response);
                } else {
                    Result.newBuilder().error(Error.INTERNAL_ERROR).write(response);
                }

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



