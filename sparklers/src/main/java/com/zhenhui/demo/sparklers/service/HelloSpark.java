package com.zhenhui.demo.sparklers.service;

import com.zhenhui.demo.sparklers.service.security.AuthenticationFilter;
import com.zhenhui.sparklers.boot.starter.Spark;
import org.springframework.stereotype.Component;

import static spark.Spark.before;
import static spark.Spark.get;

@Component
public class HelloSpark implements Spark {

    private final static AuthenticationFilter authenticationFilter = new AuthenticationFilter();

    @Override
    public void init() {
        get("/hello", (request, response) -> {
            return "hello,spark!";
        });

        before("/user/**", authenticationFilter);
    }
}

