package com.zhenhui.demo.sparklers.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import static java.util.stream.Collectors.toList;

public class JsonWebTokenAuthentication extends AbstractAuthenticationToken {

    public final Principal principal;

    public JsonWebTokenAuthentication(Principal principal) {
        super(principal.getAuthorities().stream().map(GrantedAuthorityImpl::new).collect(toList()));
        this.principal = principal;

        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
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

