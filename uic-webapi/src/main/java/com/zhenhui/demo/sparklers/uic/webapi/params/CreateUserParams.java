package com.zhenhui.demo.sparklers.uic.webapi.params;

import lombok.Data;

@Data
public class CreateUserParams {

    private String phone;

    private String secret;

    private String captcha;

}
