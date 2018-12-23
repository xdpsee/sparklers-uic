package com.zhenhui.demo.sparklers.uic.security.domain;

import com.zhenhui.demo.sparklers.uic.security.Subject;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import static java.util.stream.Collectors.toList;

public final class SecurityToken extends AbstractAuthenticationToken {

    public final Subject subject;

    public SecurityToken(Subject subject) {
        super(subject.getAuthorities().stream().map(GrantedAuthorityImpl::new).collect(toList()));
        this.subject = subject;

        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return subject;
    }

    public static class GrantedAuthorityImpl implements GrantedAuthority {

        private final String authority;

        public GrantedAuthorityImpl(String authority) {
            this.authority = authority;
        }

        @Override
        public String getAuthority() {
            return authority;
        }
    }
}

