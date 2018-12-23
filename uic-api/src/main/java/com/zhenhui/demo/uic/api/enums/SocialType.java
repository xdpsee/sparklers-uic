package com.zhenhui.demo.uic.api.enums;

import java.util.Arrays;

public enum SocialType {
    NONE(0, "无"),
    WECHAT(1, "微信"),
    WEIBO(2,"微博"),
    QQ(3,"QQ"),
    ;

    public final int code;
    public final String comment;

    SocialType(int code, String comment) {
        this.code = code;
        this.comment = comment;
    }

    public static SocialType valueOf(final int code) {
        return Arrays.stream(values())
            .filter(e -> e.code == code)
            .findAny()
            .orElseThrow(IllegalArgumentException::new);
    }
}
