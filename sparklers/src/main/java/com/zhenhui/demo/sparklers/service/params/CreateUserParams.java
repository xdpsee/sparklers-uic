package com.zhenhui.demo.sparklers.service.params;

import lombok.Data;

@Data
public class CreateUserParams {

    private String phone;

    private String secret;

    private String captcha;

}
