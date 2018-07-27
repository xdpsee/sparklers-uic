package com.zhenhui.demo.sparklers.uic.domain.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhenhui.demo.sparklers.uic.common.SocialType;
import com.zhenhui.demo.sparklers.uic.security.Principal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private long id;
    @JsonIgnore
    private String phone;

    private String name;
    @JsonIgnore
    private String secret;

    private String avatar;

    private Set<String> authorities = new HashSet<>();

    public Principal toPrincipal() {

        Principal principal = new Principal();

        principal.setUserId(id);
        principal.setPhone(phone);
        principal.setAuthorities(authorities);
        principal.setType(SocialType.NONE);

        return principal;
    }

}




