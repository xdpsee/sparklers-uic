package com.zhenhui.demo.sparklers.security;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.zhenhui.demo.sparklers.domain.model.SocialType;
import com.zhenhui.demo.sparklers.domain.model.User;
import lombok.Data;

@Data
public class Principal implements Serializable {

    private static final long serialVersionUID = -123490806309753L;

    private long userId;

    private String phone;

    private SocialType type;

    private long openId;

    private Set<String> authorities = new HashSet<>();

    public static Principal fromUser(User user) {
        Principal principal = new Principal();
        principal.setUserId(user.getId());
        principal.setPhone(user.getName());
        principal.setAuthorities(user.getAuthorities());
        principal.setType(SocialType.NONE);

        return principal;
    }
}



