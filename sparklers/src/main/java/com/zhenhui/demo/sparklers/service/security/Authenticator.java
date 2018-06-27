package com.zhenhui.demo.sparklers.service.security;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;


@WebFilter(urlPatterns = "/*", asyncSupported = true)
public final class Authenticator implements Filter {

    final Set<String> authorities = new HashSet<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        Principal principal = (Principal) request.getAttribute(Constants.PRINCIPAL);
        if (null == principal) {

            request.setAttribute(Constants.PRINCIPAL, principal);
        }

        if (null == principal) {

        } else {
            if (!principal.getAuthorities().containsAll(authorities)) {

            }
        }

        chain.doFilter(request, response);
    }

}


