package com.zhenhui.demo.sparklers.uic.common;

@SuppressWarnings("unused")
public enum Error {
    NONE(0, "成功"),
    INVALID_INPUT(1, "错误请求, 请检查输入"),
    DATA_NOT_FOUND(2, "数据不存在"),
    DATA_EXISTED(3, "数据已存在"),
    DATA_INVALID(4, "数据已失效"),
    LOGIN_REQUIRED(5, "需要认证, 请登录"),
    PERMISSION_DENIED(6, "权限问题, 拒绝访问"),
    INTERNAL_ERROR(7, "服务不可用, 内部错误");

    public final int code;
    public final String comment;

    Error(int code, String comment) {
        this.code = code;
        this.comment = comment;
    }

}

