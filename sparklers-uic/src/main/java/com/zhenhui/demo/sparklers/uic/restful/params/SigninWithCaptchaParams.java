package com.zhenhui.demo.sparklers.uic.restful.params;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SigninWithCaptchaParams {

    private String phone;

    private String captcha;

}

