package com.zhenhui.demo.sparklers.uic.webapi.params;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SigninWithSecretParams {

    @NotNull
    private String phone;
    @NotNull
    private String secret;

}



