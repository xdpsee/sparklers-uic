package com.zhenhui.demo.sparklers.uic.webapi.params;

import lombok.Data;

@Data
public class SigninWithCaptchaParams {

    private String phone;

    private String captcha;

}


