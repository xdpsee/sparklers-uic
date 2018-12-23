package com.zhenhui.demo.sparklers.uic.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Captcha implements Serializable {

    private static final long serialVersionUID = -1L;

    private String code;
    private long expireAt;
}