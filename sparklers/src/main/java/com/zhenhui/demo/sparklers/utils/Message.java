package com.zhenhui.demo.sparklers.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.zhenhui.demo.sparklers.service.enums.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Message<T> {

    private static final Logger logger = LoggerFactory.getLogger(Message.class);

    private final int status;
    private final int code;
    private final String message;
    private final String details;
    private final T data;

    public static Builder newBuilder() {
        return new Builder();
    }

    private Message(ErrorCode code, String message, T data) {
        this.code = code.code;
        this.message = code.comment;
        this.details = message;
        this.status = genStatus(code);
        this.data = data;
    }

    private int genStatus(ErrorCode error) {
        int status = 503;
        switch (error) {
            case NONE:
                status = 200;
                break;
            case FORMAT_INVALID:
            case DATA_EXISTED:
            case DATA_INVALID:
                status = 400;
                break;
            case LOGIN_REQUIRED:
                status = 401;
                break;
            case PERMISSION_DENIED:
                status = 403;
                break;
            case DATA_NOT_FOUND:
                status = 404;
                break;
            case INTERNAL_ERROR:
                status = 500;
                break;
            default:
                break;
        }

        return status;
    }

    public static final class Builder<T> {
        private ErrorCode error;
        private String message = "";
        private T data;

        protected Builder() {}

        public Builder error(ErrorCode error) {
            this.error = error;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder data(T data) {
            this.data = data;
            return this;
        }

        public void write(HttpServletResponse response) {
            Message<T> message = new Message<>(this.error, this.message, this.data);
            String json = JSONUtils.toJsonString(message);
            response.setStatus(message.status);
            response.setHeader("Content-Type", "application/json;charset=utf-8");

            try {
                response.getWriter().write(json);
            } catch (IOException e) {
                logger.error("Message.write error", e);

            }
        }
    }
}
