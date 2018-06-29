package com.zhenhui.demo.sparklers.service.params;

import java.util.Set;

import lombok.Data;

@Data
public class CreateUserParams {

    private String phone;

    private String secret;

    private Set<String> authorities;

}
