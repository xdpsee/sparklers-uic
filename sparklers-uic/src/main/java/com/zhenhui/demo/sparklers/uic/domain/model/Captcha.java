package com.zhenhui.demo.sparklers.uic.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Captcha {
    private String code;
    private long expireAt;
}