package com.zhenhui.demo.sparklers.uic.webapi.params;

import lombok.Data;

@Data
public class ResetSecretParams {

    private String phone;

    private String captcha;

    private String secret;

}
