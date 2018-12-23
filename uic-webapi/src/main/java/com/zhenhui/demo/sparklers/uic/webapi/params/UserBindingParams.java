package com.zhenhui.demo.sparklers.uic.webapi.params;

import lombok.Data;

@Data
public class UserBindingParams {

    private String token;

    private String phone;

    private String captcha;

}

