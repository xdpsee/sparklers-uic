package com.zhenhui.demo.uic.api.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public class UserDto implements Serializable {

    private static final long serialVersionUID = 6807169681739693341L;

    private long id;

    private String name;

    private String avatar;

    private Set<String> authorities = new HashSet<>();

}
