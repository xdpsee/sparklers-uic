package com.zhenhui.demo.sparklers.service.params;

import javax.validation.constraints.NotNull;

import lombok.Data;
import org.hibernate.validator.constraints.ScriptAssert;

@Data
@ScriptAssert(lang = "javascript", script = "_this.secret != null || _this.code != null")
public class SigninWithSecretParams {

    @NotNull
    private String phone;
    @NotNull
    private String secret;


}



