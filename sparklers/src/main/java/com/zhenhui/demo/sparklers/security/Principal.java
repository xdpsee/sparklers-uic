package com.zhenhui.demo.sparklers.security;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.zhenhui.demo.sparklers.domain.model.SocialType;
import lombok.Data;

@Data
public class Principal implements Serializable {

    private static final long serialVersionUID = -123490806309753L;

    private long userId;

    private String phone;

    private SocialType type;

    private long openId;

    private Set<String> authorities = new HashSet<>();
}



