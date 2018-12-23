package com.zhenhui.demo.sparklers.common;

@SuppressWarnings("unused")
public enum Error {
    SUCCESS("成功"),
    INVALID_INPUT("错误请求, 请检查输入"),
    DATA_NOT_FOUND("数据不存在"),
    DATA_EXISTED("数据已存在"),
    DATA_INVALID("数据已失效"),
    LOGIN_REQUIRED("需要认证, 请登录"),
    PERMISSION_DENIED("权限问题, 拒绝访问"),
    APPLICATION_ERROR("服务不可用, 内部错误");

    public final String message;

    private Error(String message) {
        this.message = message;
    }

    public String error() {
        return name();
    }
}





