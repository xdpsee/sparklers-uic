package com.zhenhui.demo.sparklers.uic.security;

import com.zhenhui.demo.uic.api.enums.SocialType;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public final class Subject implements Serializable {

    private static final long serialVersionUID = -123490806309753L;

    private long userId;

    private String phone;

    private SocialType type;

    private long openId;

    private Set<String> authorities = new HashSet<>();

}



