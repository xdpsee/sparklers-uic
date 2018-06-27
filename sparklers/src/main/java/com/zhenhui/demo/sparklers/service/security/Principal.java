package com.zhenhui.demo.sparklers.service.security;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class Principal implements Serializable {

    private static final long serialVersionUID = -123490806309753L;

    private long userId;

    private boolean locked = false;

    private Set<String> authorities = new HashSet<>();

}
