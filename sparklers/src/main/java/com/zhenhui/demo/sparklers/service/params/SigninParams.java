package com.zhenhui.demo.sparklers.service.params;

import javax.validation.constraints.NotNull;

import lombok.Data;
import org.hibernate.validator.constraints.ScriptAssert;

@Data
@ScriptAssert(lang = "javascript", script = "_this.secret != null || _this.captcha != null")
public class SigninParams {

    @NotNull
    private String phone;

    private String secret;

    private String captcha;

}



