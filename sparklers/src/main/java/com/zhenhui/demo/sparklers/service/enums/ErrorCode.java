package com.zhenhui.demo.sparklers.service.enums;

@SuppressWarnings("unused")
public enum ErrorCode {
    NONE(0, "成功"),
    FORMAT_INVALID(1, "错误请求, 请检查格式"),
    DATA_NOT_FOUND(2, "数据不存在"),
    DATA_EXISTED(3, "数据已存在"),
    DATA_INVALID(4, "数据已失效"),
    LOGIN_REQUIRED(5, "需要认证, 请登录"),
    PERMISSION_DENIED(6, "权限问题, 拒绝访问"),
    INTERNAL_ERROR(7, "服务不可用, 内部错误");

    public final int code;
    public final String comment;

    ErrorCode(int code, String comment) {
        this.code = code;
        this.comment = comment;
    }

}

