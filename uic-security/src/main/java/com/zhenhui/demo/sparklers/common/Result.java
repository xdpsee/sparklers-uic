package com.zhenhui.demo.sparklers.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Result<T> {

    private int status;

    private String error;

    private String message;

    private T data;

    public static <T> Result success(T data) {
        return Result.builder()
                .status(200)
                .error(Error.SUCCESS.name())
                .data(data)
                .build();
    }

    public static Result error(Error error) {
        return Result.builder()
                .status(200)
                .error(error.name())
                .message(error.message)
                .build();
    }

    public static Result error(String error, String message) {
        return Result.builder()
                .status(200)
                .error(error)
                .message(message)
                .build();
    }

    public static Result error(int status, Error error) {
        return Result.builder()
                .status(status)
                .error(error.name())
                .message(error.message)
                .build();
    }

    public static Result error(int status, String error, String message) {
        return Result.builder()
                .status(status)
                .error(error)
                .message(message)
                .build();
    }
}
